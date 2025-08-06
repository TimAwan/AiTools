package com.ght666.aiTools.service.impl;

import com.ght666.aiTools.entity.po.PdfUploadMessage;
import com.ght666.aiTools.entity.vo.Result;
import com.ght666.aiTools.entity.vo.ValidationResult;
import com.ght666.aiTools.repository.FileRepository;
import com.ght666.aiTools.service.IFileStorageService;
import com.ght666.aiTools.service.IFileValidator;
import com.ght666.aiTools.service.IPdfChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


/**
 * @author: 1012ght
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class PdfChatServiceImpl implements IPdfChatService {

    private final FileRepository fileRepository;

    private final IFileValidator fileValidator;

    private final IFileStorageService  fileStorageService ;

    @Override
    public Result uploadPdf(String chatId, MultipartFile file)  {
        // PDF 校验
        ValidationResult validationResult = fileValidator.validatePdfFile(file);
        if (!validationResult.isValid()) {
            return Result.fail("文件校验失败："+String.join(",", validationResult.getErrors()));
        }
        // 保存文件到临时目录
        String tempFilePath = fileStorageService.saveToTemp(file);
        // 构建消息
        PdfUploadMessage message = buildUploadMessage(chatId, file, tempFilePath);
        // 发布消息
        return null;
    }

    // 构建消息
    private PdfUploadMessage buildUploadMessage(String chatId, MultipartFile file, String tempFilePath) {
        return PdfUploadMessage.builder()
                .chatId(chatId)
                .filePath(tempFilePath)
                .fileName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .uploadTime(LocalDateTime.now().toString())
                .processType("PDF_PROCESS")
                .priority(calculatePriority(file.getSize()))
                .metadata(buildMetadata(file))
                .build();
    }
    // 优先级 todo 设计
    private Integer calculatePriority(Long fileSize) {
        if (fileSize < 1024 * 1024) return 1; // 小文件高优先级
        if (fileSize < 10 * 1024 * 1024) return 2; // 中等文件
        return 3; // 大文件低优先级
    }
    // 构建元数据
    private Map<String, Object> buildMetadata(MultipartFile file) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("contentType", file.getContentType());
        metadata.put("originalName", file.getOriginalFilename());
        metadata.put("uploadTime", System.currentTimeMillis());
        return metadata;
    }
}
