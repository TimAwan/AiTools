package com.ght666.aiTools.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ght666.aiTools.entity.po.ChatMessage;
import com.ght666.aiTools.entity.po.Msg;
import com.ght666.aiTools.mapper.ChatMessageMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.ght666.aiTools.constants.DataSourseConstants.CHAT_MEMORY_TTL;
import static com.ght666.aiTools.constants.DataSourseConstants.PREFIX;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisChatMemory implements ChatMemory {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final ChatMessageMapper chatMessageMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(String conversationId, List<Message> messages) {
        if (messages == null || messages.isEmpty()) {
            return;
        }
        // 双写 序列化消息
        List<String> redisMessages = messages.stream()
                .map(Msg::new)
                .map(msg -> {
                    try {
                        return objectMapper.writeValueAsString(msg);
                    } catch (JsonProcessingException e) {
                        log.error("序列化消息出错", e);
                        throw new RuntimeException(e);
                    }
                }).toList();
        // 写入Redis
        String redisKey = PREFIX + conversationId;
        List<ChatMessage> mysqlMessages = messages.stream()
                .map(msg -> {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setChatId(conversationId);
                    chatMessage.setMessageType(msg.getMessageType().name());
                    chatMessage.setContent(msg.getText());
                    chatMessage.setCreatedTime(new Date());
                    return chatMessage;
                }).toList();
        try {
            // redis
            redisTemplate.opsForList().leftPushAll(redisKey, redisMessages);
            redisTemplate.expire(redisKey, CHAT_MEMORY_TTL, TimeUnit.MINUTES);

        }catch (Exception e){
            log.error("Redis写入失败，操作中止。conversationId: {}", conversationId, e);
            // 如果Redis写入失败，不需要补偿，因为MySQL操作还没开始
            throw new RuntimeException("Redis写入失败", e);
        }
        try{
            // MySQL
            chatMessageMapper.insertBatchSomeColumn(mysqlMessages);
        } catch (Exception e) {
            log.error("MySQL写入失败，准备进行补偿。conversationId: {}", conversationId, e);
            // 如果MySQL写入失败（并回滚），手动删除Redis中的数据作为补偿
            redisTemplate.delete(redisKey);
            throw new RuntimeException("MySQL写入失败，已执行补偿", e);
        }
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        String redisKey = PREFIX + conversationId;

        // 旁路缓存读取
        // 优先查询 Redis
        List<String> list = redisTemplate.opsForList().range(redisKey, 0, lastN > 0 ? lastN : -1);

        if (list != null && !list.isEmpty()) {
            // 缓存命中，返回数据并刷新过期时间
            redisTemplate.expire(redisKey, CHAT_MEMORY_TTL, TimeUnit.MINUTES);
            return list.stream()
                    .map(s -> {
                        try {
                            return objectMapper.readValue(s, Msg.class);
                        } catch (JsonProcessingException e) {
                            log.error("反序列化消息出错", e);
                            throw new RuntimeException(e);
                        }
                    })
                    .map(Msg::toMessage)
                    .toList();
        }
        // 缓存未命中，从 MySQL 查询
        log.info("Redis 缓存未命中 对话ID: {}, 从 MySQL 获取.", conversationId);
        QueryWrapper<ChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chat_id", conversationId)
                .orderByDesc("created_time")
                .last("LIMIT " + lastN); // 查询最近N条记录
        List<ChatMessage> mysqlMessages = chatMessageMapper.selectList(queryWrapper);
        if (mysqlMessages == null || mysqlMessages.isEmpty()) {
            redisTemplate.opsForList().leftPush(redisKey, "null");
            redisTemplate.expire(redisKey, 5, TimeUnit.MINUTES);
            log.info("从 MySQL 中未找到数据，已写入空值到 Redis 以防止缓存穿透。");
            return List.of();
        }
        // 回填 Redis 并设置过期时间
        List<String> redisMessages = mysqlMessages.stream()
                .map(msg -> {
                    try {
                        return objectMapper.writeValueAsString(msg);
                    } catch (JsonProcessingException e) {
                        log.error("从 MySQL 序列化消息时出错", e);
                        return null;
                    }
                }).collect(Collectors.toList());
        redisTemplate.opsForList().leftPushAll(redisKey, redisMessages);
        long randomTtl = CHAT_MEMORY_TTL + (long) (Math.random() * 60);
        redisTemplate.expire(redisKey, randomTtl, TimeUnit.MINUTES);
        log.info("MySQL 回填数据至 Redis 中，并设置过期时间为 {} 分钟。", randomTtl);
        // 返回数据
        return mysqlMessages.stream()
                .map(chatMessage -> {
                    // 根据 chatMessage.getMessageType() 的值，返回不同的 Message 子类
                    if (MessageType.USER.name().equals(chatMessage.getMessageType())) {
                        return new UserMessage(chatMessage.getContent());
                    } else if (MessageType.ASSISTANT.name().equals(chatMessage.getMessageType())) {
                        return new AssistantMessage(chatMessage.getContent());
                    } else if (MessageType.SYSTEM.name().equals(chatMessage.getMessageType())) {
                        return new SystemMessage(chatMessage.getContent());
                    } else {
                        return new AssistantMessage(chatMessage.getContent());
                    }
                }).collect(Collectors.toList());
    }

    @Override
    public void clear(String conversationId) {
        // --- 双删逻辑 ---
        // 删除 Redis
        redisTemplate.delete(PREFIX + conversationId);
        // 删除 MySQL
        QueryWrapper<ChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chat_id", conversationId);
        chatMessageMapper.delete(queryWrapper);
    }
}