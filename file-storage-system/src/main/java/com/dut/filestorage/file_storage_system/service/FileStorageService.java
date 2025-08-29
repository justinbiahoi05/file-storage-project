// Chỉ lưu file vật lý và trả về tên file đã được mã hóa.

package com.dut.filestorage.file_storage_system.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {
    
    private final Path root = Paths.get("uploads");

    public FileStorageService() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    public String save(MultipartFile file) {
        try {
            String randomFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            
            Files.copy(file.getInputStream(), this.root.resolve(randomFileName));

            return randomFileName; // Chỉ trả về tên file đã lưu
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }
}