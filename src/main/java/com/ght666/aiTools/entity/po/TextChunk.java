package com.ght666.aiTools.entity.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextChunk {
    
    /**
     * 块ID
     */
    private Integer id;
    
    /**
     * 文本内容
     */
    private String content;
    
    /**
     * 文本长度
     */
    private Integer length;
    
    /**
     * 元数据
     */
    private Map<String, Object> metadata;
    
    /**
     * 创建成功的文本块
     */
    public static TextChunk success(Integer id, String content, Map<String, Object> metadata) {
        return TextChunk.builder().id(id).content(content).length(content != null ? content.length() : 0).metadata(metadata).build();
    }
    
    /**
     * 创建简单的文本块
     */
    public static TextChunk simple(Integer id, String content) {
        return TextChunk.builder().id(id).content(content).length(content != null ? content.length() : 0).build();
    }
    
    /**
     * 检查文本块是否有效
     */
    public boolean isValid() {
        return content != null && !content.trim().isEmpty() && length > 0;
    }
    
    /**
     * 获取清理后的内容
     */
    public String getCleanedContent() {
        if (content == null) {
            return "";
        }
        return content.trim();
    }
}