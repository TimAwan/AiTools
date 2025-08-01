package com.ght666.aiTools.interceptor;

import com.ght666.aiTools.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

// 修改为 jakarta.servlet 命名空间
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserAuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public UserAuthInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 跳过不需要认证的路径
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api/user/login") || requestURI.startsWith("/api/user/register")) {
            return true;
        }

        // 获取token
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(401);
            return false;
        }

        token = token.substring(7);

        // 验证token
        if (!jwtUtil.validateToken(token)) {
            response.setStatus(401);
            return false;
        }

        // 解析用户信息并设置到request中
        Claims claims = jwtUtil.parseToken(token);
        request.setAttribute("userId", Long.valueOf(claims.getSubject()));
        request.setAttribute("username", claims.get("username"));

        return true;
    }
}
