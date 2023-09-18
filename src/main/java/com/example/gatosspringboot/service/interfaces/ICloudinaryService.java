package com.example.gatosspringboot.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ICloudinaryService {
    List<Map> upload(MultipartFile[] files) throws IOException;
    Map delete(String id) throws IOException;
}
