package com.ght666.aiTools.service;

import com.ght666.aiTools.entity.vo.ValidationResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFileValidator {
    /**
     * 验证PDF文件
     */
    ValidationResult validatePdfFile(MultipartFile file);
    
    /**
     * 验证文件大小
     */
    ValidationResult validateFileSize(MultipartFile file, long maxSize);
    
    /**
     * 验证文件类型
     */
    ValidationResult validateFileType(MultipartFile file, List<String> allowedTypes);
}