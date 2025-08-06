package com.ght666.aiTools.service.impl;

import com.ght666.aiTools.service.IFileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author: 1012ght
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageServiceImpl implements IFileStorageService {
    @Value("${pdf.temp.path}")
    private String tempPath;
    @Override
    public String saveToTemp(MultipartFile file)  {
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String fullTempPath = tempPath + "/" + datePath;
        String filePath = null;
        try {
            Files.createDirectories(Paths.get(fullTempPath));
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            filePath = fullTempPath + "/" + fileName;
            Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            // 文件权限
            Files.setPosixFilePermissions(Paths.get(filePath), PosixFilePermissions.fromString("rw-r--r--"));
        } catch (IOException e) {
            log.error("缓存文件出错 IOException error",e);
            throw new RuntimeException(e);
        }
        log.info("文件已保存到临时目录: {}", filePath);
        return filePath;

    }
    private String generateUniqueFileName(String originalName) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String extension = getFileExtension(originalName);
        return "pdf_" + timestamp + extension;
    }

    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(lastDot) : ".pdf";
    }
}
