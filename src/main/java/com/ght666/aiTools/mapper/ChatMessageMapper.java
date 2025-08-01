package com.ght666.aiTools.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ght666.aiTools.entity.po.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {

    /**
     * 根据用户ID和会话ID查找消息列表
     */
    List<ChatMessage> findByUserIdAndChatId(@Param("userId") Long userId, @Param("chatId") String chatId);

    /**
     * 根据会话ID查找最近的消息
     */
    List<ChatMessage> findRecentByChatId(@Param("chatId") String chatId, @Param("limit") int limit);

    /**
     * 根据用户ID查找所有消息
     */
    List<ChatMessage> findByUserId(@Param("userId") Long userId);

    /**
     * 删除会话的所有消息
     */
    int deleteByChatId(@Param("chatId") String chatId);

    /**
     * 删除用户的所有消息
     */
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 统计会话消息数量
     */
    int countByChatId(@Param("chatId") String chatId);

    /**
     * 根据时间范围查找消息
     */
    List<ChatMessage> findByTimeRange(@Param("userId") Long userId, 
                                     @Param("startTime") String startTime, 
                                     @Param("endTime") String endTime);

    
}