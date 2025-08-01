package com.ght666.aiTools.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ght666.aiTools.entity.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查找用户
     */
    User findByUsername(@Param("username") String username);

    /**
     * 根据邮箱查找用户
     */
    User findByEmail(@Param("email") String email);

    /**
     * 根据账号ID查找用户
     */
    User findByAccountId(@Param("accountId") String accountId);

    /**
     * 根据状态查找用户列表
     */
    List<User> findByStatus(@Param("status") Integer status);

    /**
     * 根据角色查找用户列表
     */
    List<User> findByRole(@Param("role") String role);

    /**
     * 更新用户最后登录时间
     */
    int updateLastLoginTime(@Param("userId") Long userId, @Param("loginTime") String loginTime);

    /**
     * 批量更新用户状态
     */
    int batchUpdateStatus(@Param("userIds") List<Long> userIds, @Param("status") Integer status);

    /**
     * 根据条件查找用户（复杂查询）
     */
    List<User> findUsersByCondition(Map<String, Object> params);

}