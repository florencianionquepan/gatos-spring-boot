package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.service.interfaces.IAmazonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ficha/file")
public class StorageController {

    @Autowired
    public IAmazonService service;

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam(value="file") MultipartFile file){
        return new ResponseEntity<>(service.uploadFile(file), HttpStatus.OK);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName){
        byte[] data= service.downloadFile(fileName);
        ByteArrayResource resource=new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type","application/octet-stream")
                .header("Content-disposition","attachment; filename=\""+fileName+"\"")
                .body(resource);
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName){
        return new ResponseEntity<>(service.deleteFile(fileName),HttpStatus.OK);
    }
}
