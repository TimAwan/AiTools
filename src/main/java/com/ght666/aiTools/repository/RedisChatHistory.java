package com.ght666.aiTools.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ght666.aiTools.entity.po.ChatHistory;
import com.ght666.aiTools.mapper.ChatHistoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import static com.ght666.aiTools.constants.DataSourseConstants.CHAT_HISTORY_KEY_PREFIX;
import static com.ght666.aiTools.constants.DataSourseConstants.CHAT_HISTORY_TTL;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisChatHistory implements ChatHistoryRepository {

    private final StringRedisTemplate redisTemplate;
    private final ChatHistoryMapper chatHistoryMapper;

    @Override
    public void save(String type, String chatId) {
        // 写入 Redis Sorted Set
        redisTemplate.opsForZSet().add(
                CHAT_HISTORY_KEY_PREFIX + type,
                chatId,
                System.currentTimeMillis()
        );
        redisTemplate.opsForValue().set(
                CHAT_HISTORY_KEY_PREFIX + type + ":" + chatId, "1", CHAT_HISTORY_TTL, TimeUnit.DAYS
        );

        // 写入 MySQL
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setType(type);
        chatHistory.setChatId(chatId);
        chatHistory.setCreatedTime(new java.util.Date());

        try {
            // 检查数据库中是否已存在该会话ID，防止重复插入
            if (chatHistoryMapper.selectById(chatId) == null) {
                chatHistoryMapper.insert(chatHistory);
            }
        } catch (Exception e) {
            log.error("无法将聊天记录保存到 数据库 chatId {}:: ", chatId, e);
            // 添加重试或告警逻辑
        }
    }

    @Override
    public List<String> getChatIds(String type) {
        Set<String> chatIds = redisTemplate.opsForZSet()
                .range(CHAT_HISTORY_KEY_PREFIX + type, 0, -1);
        if (chatIds != null && !chatIds.isEmpty()) {
            return new ArrayList<>(chatIds);
        }
        log.info("Redis 缓存未命中，从 MySQL 查询聊天历史列表，type: {}", type);
        QueryWrapper<ChatHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type).orderByDesc("created_time");
        List<ChatHistory> historyList = chatHistoryMapper.selectList(queryWrapper);
        if (historyList == null || historyList.isEmpty()) {
            log.info("数据库中也没有聊天历史列表，type: {}", type);
            return new ArrayList<>();
        }
        // 回填redis
        Set<ZSetOperations.TypedTuple<String>> tuples = historyList.stream()
                .map(history -> new DefaultTypedTuple<>(history.getChatId(), (double) history.getCreatedTime().getTime()))
                .collect(Collectors.toSet());
        redisTemplate.opsForZSet().add(CHAT_HISTORY_KEY_PREFIX + type, tuples);
        redisTemplate.expire(CHAT_HISTORY_KEY_PREFIX + type, CHAT_HISTORY_TTL, TimeUnit.DAYS);
        log.info("Mysql 回填数据至redis中");
        return historyList.stream()
                .map(ChatHistory::getChatId)
                .collect(Collectors.toList());
    }
}