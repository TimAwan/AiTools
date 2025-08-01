package com.ght666.aiTools.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ght666.aiTools.entity.po.User;

public interface IUserService extends IService<User> {

    /**
     * 用户注册
     */
    User register(String username, String password, String email);

    /**
     * 用户登录
     */
    User login(String username, String password);

    /**
     * 根据用户名查找用户
     */
    User findByUsername(String username);

    /**
     * 根据邮箱查找用户
     */
    User findByEmail(String email);

    /**
     * 更新用户最后登录时间
     */
    void updateLastLoginTime(Long userId);

    /**
     * 修改密码
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 更新用户信息
     */
    boolean updateUserInfo(Long userId, String nickname, String avatar);
}