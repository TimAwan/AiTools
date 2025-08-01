package com.ght666.aiTools.entity.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Result<T> {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 时间戳
     */
    private LocalDateTime timestamp;

    /**
     * 请求ID（用于日志追踪）
     */
    private String requestId;

    /**
     * 成功状态
     */
    private Boolean success;

    private Result(Integer code, String message, T data, Boolean success) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> Result<T> ok() {
        return new Result<>(200, "操作成功", null, true);
    }

    /**
     * 成功响应（有数据）
     */
    public static <T> Result<T> ok(T data) {
        return new Result<>(200, "操作成功", data, true);
    }

    /**
     * 成功响应（自定义消息）
     */
    public static <T> Result<T> ok(String message, T data) {
        return new Result<>(200, message, data, true);
    }

    /**
     * 失败响应
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(500, message, null, false);
    }

    /**
     * 失败响应（自定义状态码）
     */
    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null, false);
    }

    /**
     * 失败响应（有数据）
     */
    public static <T> Result<T> fail(Integer code, String message, T data) {
        return new Result<>(code, message, data, false);
    }

    /**
     * 参数错误
     */
    public static <T> Result<T> badRequest(String message) {
        return new Result<>(400, message, null, false);
    }

    /**
     * 未授权
     */
    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(401, message, null, false);
    }

    /**
     * 禁止访问
     */
    public static <T> Result<T> forbidden(String message) {
        return new Result<>(403, message, null, false);
    }

    /**
     * 资源不存在
     */
    public static <T> Result<T> notFound(String message) {
        return new Result<>(404, message, null, false);
    }

    /**
     * 服务器错误
     */
    public static <T> Result<T> serverError(String message) {
        return new Result<>(500, message, null, false);
    }

    /**
     * 设置请求ID
     */
    public Result<T> requestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return Boolean.TRUE.equals(this.success);
    }

    /**
     * 判断是否失败
     */
    public boolean isFail() {
        return !isSuccess();
    }
}