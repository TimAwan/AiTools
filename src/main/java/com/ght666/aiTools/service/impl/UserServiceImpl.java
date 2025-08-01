package com.ght666.aiTools.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ght666.aiTools.entity.po.User;
import com.ght666.aiTools.mapper.UserMapper;
import com.ght666.aiTools.service.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(String username, String password, String email) {
        // 检查用户名是否已存在
        if (findByUsername(username) != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (findByEmail(email) != null) {
            throw new RuntimeException("邮箱已存在");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(username);
        user.setAccountId(generateAccountId());
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setNickname(username);
        user.setStatus(1);
        user.setRole("USER");
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());

        save(user);
        return user;
    }

    @Override
    public User login(String username, String password) {
        User user = findByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        if (user.getStatus() == 0) {
            throw new RuntimeException("账户已被禁用");
        }

        // 更新最后登录时间
        updateLastLoginTime(user.getId());

        return user;
    }

    @Override
    public User findByUsername(String username) {
        return baseMapper.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return baseMapper.findByEmail(email);
    }

    @Override
    public void updateLastLoginTime(Long userId) {
        User user = getById(userId);
        user.setLastLoginTime(LocalDateTime.now());
        updateById(user);
    }

    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = getById(userId);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return updateById(user);
    }

    @Override
    public boolean updateUserInfo(Long userId, String nickname, String avatar) {
        User user = getById(userId);
        user.setNickname(nickname);
        user.setAvatar(avatar);
        return updateById(user);
    }

    private String generateAccountId() {
        return "U" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8);
    }
}