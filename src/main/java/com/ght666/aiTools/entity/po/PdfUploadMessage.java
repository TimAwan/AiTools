package com.ght666.aiTools.entity.po;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class PdfUploadMessage {
    private String chatId;           // 会话ID
    private String filePath;         // 临时文件路径
    private String fileName;         // 文件名
    private Long fileSize;           // 文件大小
    private String uploadTime;       // 上传时间
    private String processType;      // 处理类型
    private Integer priority;        // 优先级
    private Map<String, Object> metadata; // 元数据
}