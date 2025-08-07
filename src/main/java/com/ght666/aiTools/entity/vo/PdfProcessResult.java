package com.ght666.aiTools.entity.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class PdfProcessResult {
    private String chatId;
    private String fileId;
    private String status;           // SUCCESS, FAILED
    private String message;
    private LocalDateTime updateTime;
    private Map<String, Object> resultData;
}