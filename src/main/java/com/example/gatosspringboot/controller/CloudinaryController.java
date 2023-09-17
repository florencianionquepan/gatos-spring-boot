package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.service.imple.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/cloudinary")
public class CloudinaryController {

    @Autowired
    CloudinaryService cloudinaryService;

    @PostMapping
    public ResponseEntity<Map> upload(@RequestParam MultipartFile multipartFile){
        Map result= null;
        try {
            result = cloudinaryService.upload(multipartFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map> delete(@PathVariable String id){
        Map result= null;
        try {
            result = cloudinaryService.delete(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
