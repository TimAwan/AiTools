package com.ght666.aiTools.controller;

import com.ght666.aiTools.entity.po.User;
import com.ght666.aiTools.entity.vo.Result;
import com.ght666.aiTools.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<User> register(@RequestParam String username,
                                @RequestParam String password,
                                @RequestParam String email) {
        try {
            User user = userService.register(username, password, email);
            return Result.ok(user);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestParam String username,
                                            @RequestParam String password) {
        try {
            User user = userService.login(username, password);
            
            Map<String, Object> result = new HashMap<>();
            result.put("user", user);
            result.put("token", generateToken(user)); // 这里需要实现JWT token生成
            
            return Result.ok(result);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/info")
    public Result<User> getUserInfo(@RequestParam Long userId) {
        User user = userService.getById(userId);
        if (user != null) {
            // 隐藏敏感信息
            user.setPassword(null);
            return Result.ok(user);
        }
        return Result.fail("用户不存在");
    }

    /**
     * 修改密码
     */
    @PostMapping("/change-password")
    public Result<String> changePassword(@RequestParam Long userId,
                                        @RequestParam String oldPassword,
                                        @RequestParam String newPassword) {
        boolean success = userService.changePassword(userId, oldPassword, newPassword);
        if (success) {
            return Result.ok("密码修改成功");
        }
        return Result.fail("原密码错误");
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/update-info")
    public Result<String> updateUserInfo(@RequestParam Long userId,
                                        @RequestParam(required = false) String nickname,
                                        @RequestParam(required = false) String avatar) {
        boolean success = userService.updateUserInfo(userId, nickname, avatar);
        if (success) {
            return Result.ok("信息更新成功");
        }
        return Result.fail("更新失败");
    }

    private String generateToken(User user) {
        // 这里需要实现JWT token生成逻辑
        return "token_" + user.getId() + "_" + System.currentTimeMillis();
    }
}