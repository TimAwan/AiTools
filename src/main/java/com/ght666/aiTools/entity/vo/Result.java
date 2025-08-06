package com.ght666.aiTools.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 统一API响应格式
 * @param <T> 数据类型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private Integer code;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private Boolean success;

    // 使用 Builder 模式简化构造
    private Result(Integer code, String message, T data, Boolean success) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
        this.timestamp = LocalDateTime.now(ZoneId.of("Asia/Shanghai")); // 增加时区
    }
    // 成功
    public static <T> Result<T> ok() {
        return ok("操作成功", null);
    }
    public static <T> Result<T> ok(T data) {
        return ok("操作成功", data);
    }
    public static <T> Result<T> ok(String message, T data) {
        return new Result<>(200, message, data, true);
    }
    // 失败
    public static <T> Result<T> fail(String message) {
        return fail(500, message, null);
    }
    public static <T> Result<T> fail(Integer code, String message) {
        return fail(code, message, null);
    }
    public static <T> Result<T> fail(Integer code, String message, T data) {
        return new Result<>(code, message, data, false);
    }
    public boolean isSuccess() {
        return Boolean.TRUE.equals(this.success);
    }
    public boolean isFail() {
        return !isSuccess();
    }
}