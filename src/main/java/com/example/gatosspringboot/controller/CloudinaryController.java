package com.example.gatosspringboot.controller;

import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.service.imple.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cloudinary")
public class CloudinaryController {

    @Autowired
    CloudinaryService cloudinaryService;

    @PostMapping
    public ResponseEntity<?> upload(@RequestParam MultipartFile[] multipartFiles) throws IOException {
        for (MultipartFile multipartFile : multipartFiles){
            BufferedImage bi= ImageIO.read(multipartFile.getInputStream());
            if(bi==null){
                throw new NonExistingException("Alguna imagen no es válida");
            }
        }
        List<Map> results = cloudinaryService.upload(multipartFiles);
        return new ResponseEntity<>(results, HttpStatus.OK);
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
