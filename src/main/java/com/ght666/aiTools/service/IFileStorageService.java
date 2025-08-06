package com.ght666.aiTools.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileStorageService {
    String saveToTemp(MultipartFile file) ;
}
