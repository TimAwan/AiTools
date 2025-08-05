package com.ght666.aiTools.service;

import com.ght666.aiTools.entity.vo.Result;
import org.springframework.web.multipart.MultipartFile;

public interface IPdfChatService {

    Result uploadPdf(String chatId, MultipartFile file);
}
