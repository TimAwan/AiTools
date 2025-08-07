package com.ght666.aiTools.entity.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class PageInfo {
        private int pageNumber;
        private String content;
        private int length;
        private int totalPages;
    }