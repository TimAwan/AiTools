package com.ght666.aiTools.elasticsearch;

import com.ght666.aiTools.entity.elasticsearch.PdfDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PdfDocumentRepository extends ElasticsearchRepository<PdfDocument, String> {
    
    /**
     * 根据聊天ID查找文档
     */
    List<PdfDocument> findByChatId(String chatId);
    
    /**
     * 根据聊天ID和页码查找文档
     */
    PdfDocument findByChatIdAndPageNumber(String chatId, Integer pageNumber);
    
    /**
     * 删除聊天ID相关的所有文档
     */
    void deleteByChatId(String chatId);
}