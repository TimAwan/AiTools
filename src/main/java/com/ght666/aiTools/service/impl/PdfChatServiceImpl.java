package com.ght666.aiTools.service.impl;

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

import java.io.IOException;
import java.util.Objects;

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
    public Result uploadPdf(String chatId, MultipartFile file) throws IOException {
        // PDF 校验
        ValidationResult validationResult = fileValidator.validatePdfFile(file);
        if (!validationResult.isValid()) {
            return Result.fail("文件校验失败："+String.join(",", validationResult.getErrors()));
        }
        // 保存文件到临时目录
        String tempFilePath = fileStorageService.saveToTemp(file);
        // 构建消息
        return null;
    }
}
