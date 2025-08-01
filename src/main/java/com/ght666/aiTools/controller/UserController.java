package com.ght666.aiTools.controller;

import com.ght666.aiTools.entity.po.User;
import com.ght666.aiTools.entity.vo.LoginResponse;
import com.ght666.aiTools.entity.vo.Result;
import com.ght666.aiTools.entity.vo.UserResponse;
import com.ght666.aiTools.service.IUserService;
import com.ght666.aiTools.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    private final JwtUtil jwtUtil;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<UserResponse> register(@RequestParam String username,
                                         @RequestParam String password,
                                         @RequestParam String email) {
        if (username == null || username.trim().isEmpty()) {
            return Result.fail(400, "用户名不能为空");
        }
        if (password == null || password.length() < 6) {
            return Result.fail(400, "密码长度不能少于6位");
        }
        if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return Result.fail(400, "邮箱格式不正确");
        }
        try {
            User user = userService.register(username, password, email);
            UserResponse userResponse = convertToUserResponse(user);
            return Result.ok("注册成功", userResponse);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestParam String username,
                                       @RequestParam String password) {
        try {
            User user = userService.login(username, password);
            String token = jwtUtil.generateToken(user.getId(), user.getUsername());

            LoginResponse loginResponse = new LoginResponse()
                    .setUser(convertToUserResponse(user))  //  使用转换方法
                    .setAccessToken(token)
                    .setExpiresIn(24 * 60 * 60L);

            return Result.ok("登录成功", loginResponse);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/info")
    public Result<UserResponse> getUserInfo(@RequestParam Long userId) {
        User user = userService.getById(userId);
        if (user != null) {
            UserResponse userResponse = convertToUserResponse(user);  //  使用转换方法
            return Result.ok(userResponse);
        }
        return Result.fail("用户不存在");
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/update-info")
    public Result<UserResponse> updateUserInfo(@RequestParam Long userId,
                                               @RequestParam(required = false) String nickname,
                                               @RequestParam(required = false) String avatar) {
        boolean success = userService.updateUserInfo(userId, nickname, avatar);
        if (success) {
            User user = userService.getById(userId);
            UserResponse userResponse = convertToUserResponse(user);  //  使用转换方法
            return Result.ok("信息更新成功", userResponse);
        }
        return Result.fail("更新失败");
    }

    /**
     * 转换用户实体到响应对象
     */
    private UserResponse convertToUserResponse(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponse()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setAccountId(user.getAccountId())
                .setEmail(user.getEmail())
                .setNickname(user.getNickname())
                .setAvatar(user.getAvatar())
                .setRole(user.getRole())
                .setLastLoginTime(user.getLastLoginTime())
                .setCreatedTime(user.getCreatedTime())
                .setUpdatedTime(user.getUpdatedTime());
    }

}