package com.ght666.aiTools.entity.vo;

import java.util.Map;

public class PdfProcessResult {
    private String chatId;
    private String fileId;
    private String status;           // SUCCESS, FAILED
    private String errorMessage;
    private Long processTime;
    private Map<String, Object> resultData;
}