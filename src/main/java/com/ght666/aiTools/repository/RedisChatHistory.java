package com.ght666.aiTools.repository;

import com.ght666.aiTools.entity.po.ChatHistory;
import com.ght666.aiTools.mapper.ChatHistoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisChatHistory implements ChatHistoryRepository{

    private final StringRedisTemplate redisTemplate;
    private final ChatHistoryMapper chatHistoryMapper; // 注入数据库Mapper

    private final static String CHAT_HISTORY_KEY_PREFIX = "chat:history:";
    private final static long CHAT_HISTORY_TTL = 7; // Redis缓存过期时间，例如7天

    @Override
    public void save(String type, String chatId) {
        // 写入 Redis Sorted Set
        redisTemplate.opsForZSet().add(
                CHAT_HISTORY_KEY_PREFIX + type,
                chatId,
                System.currentTimeMillis()
        );
        // 为会话ID设置独立的TTL，以实现过期检查
        redisTemplate.opsForValue().set(
                CHAT_HISTORY_KEY_PREFIX + type + ":" + chatId, "1", CHAT_HISTORY_TTL, TimeUnit.DAYS
        );

        // 写入 MySQL
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setType(type);
        chatHistory.setChatId(chatId);
        chatHistory.setCreatedTime(new java.util.Date()); // 使用当前时间

        try {
            // 检查数据库中是否已存在该会话ID，防止重复插入
            if (chatHistoryMapper.selectById(chatId) == null) {
                chatHistoryMapper.insert(chatHistory);
            }
        } catch (Exception e) {
            log.error("无法将聊天记录保存到 数据库 chatId {}:: ", chatId, e);
            // 这里可以添加重试或告警逻辑
        }
    }

    @Override
    public List<String> getChatIds(String type) {
        Set<String> chatIds = redisTemplate.opsForZSet()
                .range(CHAT_HISTORY_KEY_PREFIX + type, 0, -1);
        if (chatIds != null) {
            return new ArrayList<>(chatIds);
        }
        return new ArrayList<>();
    }
}