package com.ght666.aiTools.service;

import com.ght666.aiTools.entity.po.PdfUploadMessage;
import com.ght666.aiTools.entity.vo.PdfProcessResult;

public interface IPdfProcessService {

    /**
     * 异步处理pdf
     */
    PdfProcessResult processPdf(PdfUploadMessage message);
}
