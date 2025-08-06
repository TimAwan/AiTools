package com.ght666.aiTools.service;

import com.ght666.aiTools.entity.vo.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IPdfChatService {

    Result uploadPdf(String chatId, MultipartFile file) throws IOException;
}
