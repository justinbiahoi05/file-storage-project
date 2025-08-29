package com.dut.filestorage.file_storage_system.controller;

import com.dut.filestorage.file_storage_system.entity.File;
import com.dut.filestorage.file_storage_system.entity.User;
import com.dut.filestorage.file_storage_system.repository.FileRepository;
import com.dut.filestorage.file_storage_system.repository.UserRepository;
import com.dut.filestorage.file_storage_system.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileStorageService storageService;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    // Cập nhật API upload để tích hợp CSDL
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, 
                                        @RequestParam("user_id") Long user_id) { // Tạm thời nhận userId để test
        
        // BƯỚC 1: Kiểm tra xem người dùng có tồn tại không
        Optional<User> userOptional = userRepository.findById(user_id);
        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Error: User not found!");
        }
        User owner = userOptional.get();

        try {
            // BƯỚC 2: Gọi "kỹ sư kho vận" lưu file vật lý
            String storedFileName = storageService.save(file);
            String storedPath = "uploads/" + storedFileName;

            // BƯỚC 3: Tạo "hồ sơ" cho file (tạo đối tượng File Entity)
            File fileEntity = new File();
            fileEntity.setFileName(file.getOriginalFilename());
            fileEntity.setStoredPath(storedPath);
            fileEntity.setFileSize(file.getSize());
            fileEntity.setFileType(file.getContentType());
            fileEntity.setUploadDate(LocalDateTime.now());
            fileEntity.setOwner(owner); // Gán chủ sở hữu cho file

            // BƯỚC 4: Lưu "hồ sơ" file vào CSDL
            fileRepository.save(fileEntity);
            
            String message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.ok(message);

        } catch (Exception e) {
            String message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            return ResponseEntity.internalServerError().body(message);
        }
    }
}