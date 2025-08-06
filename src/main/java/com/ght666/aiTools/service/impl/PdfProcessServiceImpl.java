package com.ght666.aiTools.service.impl;
import com.ght666.aiTools.mapper.ChatMessageMapper;
import com.ght666.aiTools.repository.FileRepository;
import com.ght666.aiTools.service.IPdfProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

/**
 * @author: 1012ght
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PdfProcessServiceImpl implements IPdfProcessService {
    private final FileRepository fileRepository;
    private final VectorStore vectorStore;
    private final ChatMessageMapper chatMessageMapper;

}
