package com.example.gatosspringboot.service.imple;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.gatosspringboot.service.interfaces.ICloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService implements ICloudinaryService {

    Cloudinary cloudinary;
    private Map<String,String> valuesMap= new HashMap<>();


    @Override
    public Map upload(MultipartFile multipartFile) throws IOException {
        File file= this.convert(multipartFile);
        Map result= cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        //para no guardar las imagenes en el servidor!!
        file.delete();
        return result;
    }

    @Override
    public Map delete(String id) throws IOException {
        Map result = cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
        return result;
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        File file=new File(multipartFile.getOriginalFilename());
        FileOutputStream fo= new FileOutputStream(file);
        fo.write(multipartFile.getBytes());
        fo.close();
        return file;
    }
}
