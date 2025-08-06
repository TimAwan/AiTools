package com.ght666.aiTools.service.impl;

import com.ght666.aiTools.entity.vo.ValidationResult;
import com.ght666.aiTools.service.IFileValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: 1012ght
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FileValidatorImpl implements IFileValidator {

    @Value("${pdf.max-file-size}")
    private long maxFileSize;

    @Override
    public ValidationResult validatePdfFile(MultipartFile file) {
        List<String> errors = new ArrayList<>();
        if (file == null || file.isEmpty()) {
            errors.add("文件不能为空");
            log.error("上传pdf文件为空");
            return ValidationResult.builder().valid(false).errors(errors).build();
        }
        if (file.getSize() > maxFileSize) {
            log.error("pdf文件大小超过限制");
            errors.add("文件大小超过限制: " + formatFileSize(maxFileSize));
        }
        if (!isValidPdfContentType(file.getContentType())) {
            log.error("文件类型错误");
            errors.add("不支持的文件类型: " + file.getContentType());
        }
        if (!validateFileContent(file)) {
            log.error("文件类型错误");
            errors.add("文件内容验证失败");
        }
        return ValidationResult.builder()
                .valid(errors.isEmpty())
                .errors(errors)
                .build();
    }

    private String formatFileSize(long size) {
        if (size < 1024) return size + " B";
        if (size < 1024 * 1024) return size / 1024 + " KB";
        return size / (1024 * 1024) + " MB";
    }
    private boolean isValidPdfContentType(String contentType) {
        return "application/pdf".equals(contentType);
    }
    private boolean validateFileContent(MultipartFile file) {
        try {
            byte[] header = new byte[8];
            file.getInputStream().read(header);
            // PDF的文件头是%PDF
            return header[0] == 0x25 && header[1] == 0x50 && header[2] == 0x44 && header[3] == 0x46;
        } catch (Exception e) {
            log.error("文件内容验证失败", e);
            return false;
        }
    }
}
