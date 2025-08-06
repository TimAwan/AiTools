// src/main/java/com/ght666/aiTools/entity/vo/ValidationResult.java
package com.ght666.aiTools.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResult {
    /**
     * 验证是否通过
     */
    private boolean valid;
    
    /**
     * 错误信息列表
     */
    private List<String> errors;
    
    /**
     * 额外的元数据信息
     */
    private Map<String, Object> metadata;

    /**
     * 便捷方法：检查是否有效
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * 便捷方法：检查是否无效
     */
    public boolean isInvalid() {
        return !valid;
    }
    
    /**
     * 便捷方法：获取错误信息字符串
     */
    public String getErrorMessage() {
        if (errors == null || errors.isEmpty()) {
            return "";
        }
        return String.join(", ", errors);
    }
    
    /**
     * 创建成功结果
     */
    public static ValidationResult success() {
        return ValidationResult.builder()
                .valid(true)
                .errors(List.of())
                .metadata(Map.of())
                .build();
    }
    
    /**
     * 创建失败结果
     */
    public static ValidationResult failure(String... errors) {
        return ValidationResult.builder()
                .valid(false)
                .errors(List.of(errors))
                .metadata(Map.of())
                .build();
    }
    
    /**
     * 创建失败结果（带数据）
     */
    public static ValidationResult failureWithMetadata(List<String> errors, Map<String, Object> metadata) {
        return ValidationResult.builder()
                .valid(false)
                .errors(errors)
                .metadata(metadata)
                .build();
    }
}