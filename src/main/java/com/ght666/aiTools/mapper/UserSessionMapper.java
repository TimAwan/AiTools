package com.ght666.aiTools.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ght666.aiTools.entity.po.UserSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserSessionMapper extends BaseMapper<UserSession> {

    /**
     * 根据用户ID和会话类型查找会话列表
     */
    List<UserSession> findByUserIdAndType(@Param("userId") Long userId, @Param("sessionType") String sessionType);

    /**
     * 根据会话ID查找会话
     */
    UserSession findByChatId(@Param("chatId") String chatId);

    /**
     * 根据用户ID查找所有会话
     */
    List<UserSession> findByUserId(@Param("userId") Long userId);

    /**
     * 删除用户的所有会话
     */
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 更新会话标题
     */
    int updateSessionTitle(@Param("chatId") String chatId, @Param("title") String title);

    /**
     * 统计用户会话数量
     */
    int countByUserIdAndType(@Param("userId") Long userId, @Param("sessionType") String sessionType);
}