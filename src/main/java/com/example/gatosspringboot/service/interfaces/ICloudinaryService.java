package com.example.gatosspringboot.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface ICloudinaryService {
    Map upload(MultipartFile file) throws IOException;
    Map delete(String id) throws IOException;
}
