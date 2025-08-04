package com.ght666.aiTools.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class RedisChatHistory implements ChatHistoryRepository{

    private final StringRedisTemplate redisTemplate;

    private final static String CHAT_HISTORY_KEY_PREFIX = "chat:history:";

    @Override
    public void save(String type, String chatId) {
        // 使用ZSet保证顺序和时间戳
        redisTemplate.opsForZSet().add(
                CHAT_HISTORY_KEY_PREFIX + type,
                chatId,
                System.currentTimeMillis()
        );
    }

    @Override
    public List<String> getChatIds(String type) {
        // 按时间戳排序获取
        Set<String> chatIds = redisTemplate.opsForZSet()
                .range(CHAT_HISTORY_KEY_PREFIX + type, 0, -1);
        return new ArrayList<>(chatIds);
    }
}
