package com.dut.filestorage.file_storage_system.controller;

import com.dut.filestorage.file_storage_system.entity.File;
import com.dut.filestorage.file_storage_system.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * Endpoint để upload file.
     * Chỉ người dùng đã xác thực mới có thể truy cập.
     */
    @PostMapping("/upload")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, Principal principal) {
        try {
            // Ủy quyền hoàn toàn cho FileService xử lý
            fileService.uploadFile(file, principal.getName());
            return ResponseEntity.ok("Uploaded the file successfully: " + file.getOriginalFilename());
        } catch (Exception e) {
            // Trả về lỗi kèm theo thông báo chi tiết từ Service
            return ResponseEntity.badRequest().body("Could not upload the file. Error: " + e.getMessage());
        }
    }

    /**
     * Endpoint để lấy danh sách file của người dùng đang đăng nhập.
     */
    @GetMapping("/my-files")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<File>> getMyFiles(Principal principal) {
        // Gọi service để lấy danh sách và trả về
        List<File> files = fileService.getFilesByUsername(principal.getName());
        return ResponseEntity.ok(files);
    }

    /**
     * Endpoint để download một file cụ thể.
     * Đã được sửa lại để tinh gọn hơn.
     */
    @GetMapping("/download/{fileId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> downloadFile(@PathVariable Long fileId, Principal principal) {
        try {
            // CHỈ CẦN MỘT DÒNG DUY NHẤT
            // FileService đã đóng gói sẵn ResponseEntity, Controller chỉ việc trả nó về.
            return fileService.downloadFile(fileId, principal.getName());
        } catch (Exception e) {
            // Nếu có lỗi (không tìm thấy file, không có quyền), trả về lỗi kèm thông báo
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}