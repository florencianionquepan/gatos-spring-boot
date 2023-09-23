package com.example.gatosspringboot.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface IAmazonService {
    String uploadFile(MultipartFile file);
    byte[] downloadFile(String fileName);
    String deleteFile(String fileName);
}
