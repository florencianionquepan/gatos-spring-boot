package com.example.gatosspringboot.service.imple;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.gatosspringboot.exception.NonExistingException;
import com.example.gatosspringboot.service.interfaces.ICloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryService implements ICloudinaryService {

    Cloudinary cloudinary;
    private Map<String,String> valuesMap= new HashMap<>();


    @Override
    public List<Map> upload(MultipartFile[] multipartFiles)  {
        List<Map> results = new ArrayList<>();
        try {
            for (MultipartFile multipartFile : multipartFiles) {
                File file = this.convert(multipartFile);
                Map result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
                //para no guardar las imagenes en el servidor!!
                file.delete();
                results.add(result);
            }
        } catch (IOException e) {
            throw new NonExistingException("Ocurrio un error: "+e);
        }
        return results;
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
