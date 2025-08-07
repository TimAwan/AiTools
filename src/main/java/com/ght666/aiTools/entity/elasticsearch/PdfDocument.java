package com.ght666.aiTools.entity.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "pdf_documents")
public class PdfDocument {
    
    @Id
    private String id;
    
    /**
     * 聊天ID
     */
    @Field(type = FieldType.Keyword)
    private String chatId;
    
    /**
     * 页码
     */
    @Field(type = FieldType.Integer)
    private Integer pageNumber;
    
    /**
     * 文本内容
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String content;
    
    /**
     * 向量嵌入
     */
    @Field(type = FieldType.Dense_Vector, dims = 1536)
    private List<Float> embedding;
    
    /**
     * 内容长度
     */
    @Field(type = FieldType.Integer)
    private Integer contentLength;
    
    /**
     * 元数据
     */
    @Field(type = FieldType.Object)
    private Map<String, Object> metadata;
    
    /**
     * 创建时间
     */
    @Field(type = FieldType.Date)
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @Field(type = FieldType.Date)
    private LocalDateTime updatedAt;
}