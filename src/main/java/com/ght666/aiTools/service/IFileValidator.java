package com.ght666.aiTools.service;

import com.ght666.aiTools.entity.vo.ValidationResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFileValidator {
    /**
     * 验证PDF文件
     */
    ValidationResult validatePdfFile(MultipartFile file);
}