/*
package com.ght666.aiTools.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

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
        // 由于使用的是Redis的Set结构，无序的，因此要确保chatId是单调递增的
        redisTemplate.opsForSet().add(CHAT_HISTORY_KEY_PREFIX + type, chatId);
    }

    @Override
    public List<String> getChatIds(String type) {
        Set<String> chatIds = redisTemplate.opsForSet().members(CHAT_HISTORY_KEY_PREFIX + type);
        if(chatIds == null || chatIds.isEmpty()) {
            return Collections.emptyList();
        }
        return chatIds.stream().sorted(String::compareTo).toList();
    }
}*/
