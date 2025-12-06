package com.openclassroom.apiProj2.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

@Service
public class FileService {
    private final String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images";


    public String saveFile(MultipartFile file) throws IOException {

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalName = file.getOriginalFilename();
        String safeName = System.currentTimeMillis() + "_" + originalName.replaceAll("\\s+", "_");
        
        Path filePath = uploadPath.resolve(safeName);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        return "http://localhost:3001/images/" + safeName;
    }

}
