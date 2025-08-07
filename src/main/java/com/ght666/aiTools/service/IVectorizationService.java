package com.ght666.aiTools.service;

import com.ght666.aiTools.entity.po.TextChunk;

import java.util.List;

public interface IVectorizationService {
    void vectorizeAndSave(String chatId, List<TextChunk> overlappedChunks);
}
