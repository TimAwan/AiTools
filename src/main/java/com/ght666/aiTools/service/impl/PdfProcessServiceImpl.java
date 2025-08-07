package com.ght666.aiTools.service.impl;

import com.ght666.aiTools.entity.po.ChatMessage;
import com.ght666.aiTools.entity.po.PdfUploadMessage;
import com.ght666.aiTools.entity.po.TextChunk;
import com.ght666.aiTools.entity.vo.PdfProcessResult;
import com.ght666.aiTools.mapper.ChatMessageMapper;
import com.ght666.aiTools.repository.FileRepository;
import com.ght666.aiTools.service.IPdfProcessService;
import com.ght666.aiTools.service.IVectorizationService;
import com.ght666.aiTools.utils.PDF2StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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

    private final IVectorizationService vectorizationService;
    // 重叠块
    private static final int OVERLAP_CHARS = 50;

    public PdfProcessResult processPdf(PdfUploadMessage message) {
        PdfProcessResult pdfProcessResult = new PdfProcessResult();
        String chatId = message.getChatId();
        String filePath = message.getFilePath();

        try {
            // 数据库
            saveSourceFile(chatId, filePath, message.getFileName());
            // 提取文本 流式处理PDF页面 得到“块”
            List<TextChunk> overlappedChunks = processPdfStreaming(filePath);
            // 向量化
            vectorizationService.vectorizeAndSave(chatId, overlappedChunks);

        } catch (Exception e) {

        }

        return pdfProcessResult;
    }

    private List<TextChunk> processPdfStreaming(String filePath) throws IOException {
        List<TextChunk> chunks = new ArrayList<>();
        List<String> pageBuffer = new ArrayList<>(); // 只缓存3页用于重叠
        AtomicInteger chunkId = new AtomicInteger(0);
        PDF2StringUtil.processPdfPagesStreaming(filePath, pageInfo -> {
            try {
                pageBuffer.add(pageInfo.getContent());
                if (pageBuffer.size() > 3) {
                    pageBuffer.remove(0);
                }
                if (pageBuffer.size() >= 2) {
                    TextChunk textChunk = createOverlappedChunk(pageBuffer, pageInfo.getPageNumber(), chunkId.getAndIncrement());
                    chunks.add(textChunk);
                    log.debug("创建第{}页向量块: 长度={}", pageInfo.getPageNumber(), textChunk.getLength());
                }
            } catch (Exception e) {
                log.error("处理pdf第{}页时出错", pageInfo.getPageNumber(),e);
            }
        });
        // 最后一页
        if (!pageBuffer.isEmpty()) {
            TextChunk lastChunk = createOverlappedChunk(pageBuffer, pageBuffer.size(), chunkId.getAndIncrement());
            chunks.add(lastChunk);
        }
        return chunks;
    }

    // 存到数据库
    private String saveSourceFile(String chatId, String filePath, String fileName) {
        byte[] fileContent;
        try {
            fileContent = Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            log.error("pdf 存到数据库出错 IOException ", e);
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

    private TextChunk createOverlappedChunk(List<String> pageBuffer, int currentPageNumber, int chunkId) {
        // 当前页
        String currentPage = pageBuffer.get(pageBuffer.size() - 1);
        // 上一页尾
        String previousOverlap = "";
        if (pageBuffer.size() > 1) {
            String previousPage = pageBuffer.get(pageBuffer.size() - 2);
            previousOverlap = getLastNChars(previousPage, OVERLAP_CHARS);
        }
        //String nextOverlap = "";
        // todo fk 好像拿不到下一页啊 需要重新更新overlappedText？
        StringBuilder overlappedText = new StringBuilder();
        if (!previousOverlap.isEmpty()) {
            overlappedText.append("【上文】").append(previousOverlap).append("\n\n");
        }
        overlappedText.append("【当前页】").append(currentPage);
        String finalText = overlappedText.toString();
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("pageNumber", currentPageNumber);
        metadata.put("contentType", "pdf_page_with_overlap");
        metadata.put("chunkType", "overlapped_page");
        metadata.put("originalPageLength", currentPage.length());
        metadata.put("previousOverlapLength", previousOverlap.length());
        // metadata.put("nextOverlapLength", nextOverlap.length());
        metadata.put("hasPreviousOverlap", !previousOverlap.isEmpty());
        // metadata.put("hasNextOverlap", !nextOverlap.isEmpty());
        return TextChunk.builder()
                .id(chunkId)
                .content(finalText)
                .length(finalText.length())
                .metadata(metadata)
                .build();
    }

    /**
     * 获取字符串尾部
     */
    private String getLastNChars(String text, int n) {
        if (text == null || text.length() <= n) {
            return text;
        }
        return text.substring(text.length() - n);
    }

    /**
     * 获取字符串头部
     */
    private String getFirstNChars(String text, int n) {
        if (text == null || text.length() <= n) {
            return text;
        }
        return text.substring(0, n);
    }
}
