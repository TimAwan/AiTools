package com.ght666.aiTools.service.impl;

import com.ght666.aiTools.entity.po.ChatMessage;
import com.ght666.aiTools.entity.po.PdfUploadMessage;
import com.ght666.aiTools.entity.vo.PdfProcessResult;
import com.ght666.aiTools.mapper.ChatMessageMapper;
import com.ght666.aiTools.repository.FileRepository;
import com.ght666.aiTools.service.IPdfProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;

/**
 * @author: 1012ght
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PdfProcessServiceImpl implements IPdfProcessService {
    private final FileRepository fileRepository;
    private final VectorStore vectorStore;
    private final ChatMessageMapper chatMessageMapper;

    public PdfProcessResult processPdf(PdfUploadMessage message) {
        PdfProcessResult pdfProcessResult = new PdfProcessResult();
        String chatId = message.getChatId();
        String filePath = message.getFilePath();

        try {
            saveSourceFile(chatId,filePath,message.getFileName());
            // 提取文本


        }catch (Exception e){

        }

        return pdfProcessResult;
    }

    // 存到数据库
    private String saveSourceFile(String chatId, String filePath, String fileName) {
        byte[] fileContent;
        try {
            fileContent = Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            log.error("pdf 存到数据库出错 IOException ",e);
            throw new RuntimeException(e);
        }
        ChatMessage sourceFile = new ChatMessage();
        sourceFile.setChatId(chatId);
        sourceFile.setMessageType("PDF_SourceFile");
        sourceFile.setContent(Base64.getEncoder().encodeToString(fileContent));
        sourceFile.setCreatedTime(LocalDateTime.now());
        chatMessageMapper.insert(sourceFile);
        return sourceFile.getId().toString();
    }


}
