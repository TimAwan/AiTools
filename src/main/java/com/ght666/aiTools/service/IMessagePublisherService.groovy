package com.ght666.aiTools.service

import com.ght666.aiTools.entity.po.PdfUploadMessage
import com.ght666.aiTools.entity.vo.PdfProcessResult

interface IMessagePublisherService {
    void publishPdfUploadMessage(PdfUploadMessage message);
    void publishProcessResult(PdfProcessResult result);

}