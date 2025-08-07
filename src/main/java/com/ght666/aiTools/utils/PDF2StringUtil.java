package com.ght666.aiTools.utils;

import com.ght666.aiTools.entity.po.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author: 1012ght
 */
@Slf4j
public class PDF2StringUtil {
    private static final int MIN_CHUNK_LENGTH = 30;  // 最小块长度 todo
    /**
     *提取PDF所有页面的文本 采用流式处理
     */
    public static void processPdfPagesStreaming(String filePath, Consumer<PageInfo> pageProcessor) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            log.error("PDF2StringUtil 错误 PDF文件不存在 ");
            throw new IOException("PDF文件不存在: " + filePath);
        }
        Resource resource = new FileSystemResource(file);
        // 封装pdf页对象
        PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                .withPageExtractedTextFormatter(ExtractedTextFormatter.defaults())
                .withPagesPerDocument(1)
                .build();
        // 阅读器
        PagePdfDocumentReader reader = new PagePdfDocumentReader(resource, config);
        List<Document> documents = reader.read();
        if (documents.isEmpty()) {
            log.warn("PDF文件未提取到任何内容: {}", filePath);
            return ;
        }
        for (int i = 0; i < documents.size(); i++){
            Document document = documents.get(i);
            String pageText = document.getText();
            if (pageText != null && !pageText.trim().isEmpty()){
                String cleanedText = cleanPageText(pageText);
                if (cleanedText.length() > MIN_CHUNK_LENGTH){
                    PageInfo pageInfo = PageInfo.builder()
                            .pageNumber(i + 1)
                            .content(cleanedText)
                            .length(cleanedText.length())
                            .totalPages(documents.size())
                            .build();
                    // 释放内存
                    pageProcessor.accept(pageInfo);
                    log.info("PDF2StringUtil 处理第{}页: 长度={}", i + 1, cleanedText.length());
                }else{
                    log.debug("PDF2StringUtil 跳过第{}页: 内容太短", i + 1);
                }
            }
        }
        log.info("PDF流式处理完成: filePath={}, totalPages={}", filePath, documents.size());
    }

    private static String cleanPageText(String pageText) {
        if (pageText == null) {
            return "";
        }
        return pageText.replaceAll("\\s+", " ")  // 合并多个空白字符
                .replaceAll("\\n\\s*\\n", "\n")  // 合并多个换行
                .trim();
    }

}
