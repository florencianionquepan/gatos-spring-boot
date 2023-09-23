package com.example.gatosspringboot.service.imple;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.service.interfaces.IAmazonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class AmazonService implements IAmazonService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    @Override
    public String uploadFile(MultipartFile file) {
        File fileObject=convertMultiPartFileToFile(file);
        String fileName=System.currentTimeMillis()+"_"+file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObject));
        fileObject.delete();
        return fileName;
    }

    @Override
    public byte[] downloadFile(String fileName) {
        S3Object s3Object= s3Client.getObject(bucketName,fileName);
        S3ObjectInputStream inputStream=s3Object.getObjectContent();
        try {
            byte[] content=IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String deleteFile(String fileName) {
        s3Client.deleteObject(bucketName,fileName);
        return fileName+" remove";
    }

    private File convertMultiPartFileToFile(MultipartFile file){
        File convertedFile=new File(file.getOriginalFilename());
        try(FileOutputStream fos=new FileOutputStream(convertedFile)){
            fos.write(file.getBytes());
        }catch(IOException e){
            throw new NonExistingException("Error converting multipart to file: "+e);
        }
        return convertedFile;
    }
}
