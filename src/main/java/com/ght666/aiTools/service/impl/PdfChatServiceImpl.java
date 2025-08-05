package com.ght666.aiTools.service.impl;

import com.ght666.aiTools.entity.vo.Result;
import com.ght666.aiTools.repository.FileRepository;
import com.ght666.aiTools.service.IPdfChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * @author: 1012ght
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class PdfChatServiceImpl implements IPdfChatService {

    private final FileRepository fileRepository;

    @Override
    public Result uploadPdf(String chatId, MultipartFile file) {
        if (!Objects.equals(file.getContentType(), "application/pdf")) {
            log.info("PDF数据格式出错");
            return Result.fail("只能上传PDF文件！");
        }
        //file.getSize()



        return null;
    }
}
