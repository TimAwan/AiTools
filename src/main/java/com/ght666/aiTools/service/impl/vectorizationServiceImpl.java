package com.ght666.aiTools.service.impl;

import com.ght666.aiTools.elasticsearch.PdfDocumentRepository;
import com.ght666.aiTools.entity.elasticsearch.PdfDocument;
import com.ght666.aiTools.entity.po.TextChunk;
import com.ght666.aiTools.service.IVectorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author: 1012ght
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class vectorizationServiceImpl implements IVectorizationService {

    private final EmbeddingModel embeddingModel;
    private final PdfDocumentRepository pdfDocumentRepository;

    @Override
    public void vectorizeAndSave(String chatId, List<TextChunk> chunks) {
        log.info("开始向量化处理 ：chatId={},chunksCount{}", chatId, chunks.size());
        for (TextChunk chunk : chunks) {
            try {
                List<Float> embedding = generateEmbedding(chunk.getContent());
                PdfDocument pdfDocument = createPdfDocument(chatId, chunk, embedding);
                pdfDocumentRepository.save(pdfDocument);
                log.info("向量化、向量存储已完成");
            } catch (Exception e) {
                log.error("向量化、向量存储失败", e);
                throw new RuntimeException(e);
            }
        }
    }

    // 生成向量
    private List<Float> generateEmbedding(String text) {
        try {
            float[] embeddingArray = embeddingModel.embed(text);
            List<Float> embedding = new ArrayList<>();
            for (float value : embeddingArray) {
                embedding.add(value);
            }
            log.debug("生成向量嵌入成功: textLength={}, embeddingSize={}", text.length(), embedding.size());
            return embedding;
        } catch (Exception e) {
            log.error("生成向量嵌入失败 ");
            throw new RuntimeException(e);
        }
    }

    //创建PDF文档实体
    private PdfDocument createPdfDocument(String chatId, TextChunk chunk, List<Float> embedding) {
        Integer pageNumber = (Integer) chunk.getMetadata().get("pageNumber");
        return PdfDocument.builder()
                .id(generateDocumentId(chatId, pageNumber))
                .chatId(chatId)
                .pageNumber(pageNumber)
                .content(chunk.getContent())
                .embedding(embedding)
                .contentLength(chunk.getLength())
                .metadata(chunk.getMetadata())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // 文档id
    private String generateDocumentId(String chatId, Integer pageNumber) {
        return chatId + "_" + pageNumber + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
}
