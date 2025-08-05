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
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisChatMemory implements ChatMemory {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final ChatMessageMapper chatMessageMapper; // 注入数据库Mapper

    private final static String PREFIX = "chat:";
    private final static long CHAT_MEMORY_TTL = 30; // Redis缓存过期时间，例如30分钟

    @Override
    public void add(String conversationId, List<Message> messages) {
        if (messages == null || messages.isEmpty()) {
            return;
        }

        // --- 双写逻辑 ---

        // 1. 序列化消息
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

        // 2. 写入 Redis 并设置/刷新过期时间
        String redisKey = PREFIX + conversationId;
        redisTemplate.opsForList().leftPushAll(redisKey, redisMessages);
        redisTemplate.expire(redisKey, CHAT_MEMORY_TTL, TimeUnit.MINUTES);

        // 3. 写入 MySQL
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
            // 批量插入数据库，提高性能
            chatMessageMapper.insertBatchSomeColumn(mysqlMessages);
        } catch (Exception e) {
            log.error("Failed to save chat messages to database for conversationId: {}", conversationId, e);
            // 这里可以添加重试或告警逻辑
        }
    }


    @Override
    public List<Message> get(String conversationId, int lastN) {
        String redisKey = PREFIX + conversationId;

        // --- 旁路缓存读取逻辑 ---

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
            return List.of();
        }

        // 回填 Redis 并设置过期时间
        List<String> redisMessages = mysqlMessages.stream()
                .map(msg -> {
                    try {
                        return objectMapper.writeValueAsString(msg);
                    } catch (JsonProcessingException e) {
                        log.error("Error serializing message from MySQL", e);
                        return null; // 或者其他处理
                    }
                }).collect(Collectors.toList());

        redisTemplate.opsForList().leftPushAll(redisKey, redisMessages);
        redisTemplate.expire(redisKey, CHAT_MEMORY_TTL, TimeUnit.MINUTES);

        // 返回数据
        return mysqlMessages.stream()
                .map(chatMessage -> {
                    // 根据 chatMessage.getMessageType() 的值，返回不同的 Message 子类
                    if (MessageType.USER.name().equals(chatMessage.getMessageType())) {
                        return new UserMessage(chatMessage.getContent());
                    } else if (MessageType.ASSISTANT.name().equals(chatMessage.getMessageType())) {
                        return new AssistantMessage(chatMessage.getContent());
                    } else if (MessageType.SYSTEM.name().equals(chatMessage.getMessageType())) {
                        return new AssistantMessage(chatMessage.getContent());
                    } else {
                        return new AssistantMessage(chatMessage.getContent());
                    }
                }).collect(Collectors.toList());
    }


    @Override
    public void clear(String conversationId) {
        // --- 双删逻辑 ---

        // 1. 删除 Redis
        redisTemplate.delete(PREFIX + conversationId);

        // 2. 删除 MySQL
        QueryWrapper<ChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chat_id", conversationId);
        chatMessageMapper.delete(queryWrapper);
    }
}