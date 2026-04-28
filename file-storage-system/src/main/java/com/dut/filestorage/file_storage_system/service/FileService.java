package com.dut.filestorage.file_storage_system.service;

import com.dut.filestorage.file_storage_system.entity.File;
import com.dut.filestorage.file_storage_system.entity.User;
import com.dut.filestorage.file_storage_system.repository.FileRepository;
import com.dut.filestorage.file_storage_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FileService {
    
    @Autowired 
    private FileRepository fileRepository;
    
    @Autowired 
    private UserRepository userRepository;
    
    // Tiêm "kỹ sư kho vận" chuyên xử lý file vật lý
    @Autowired 
    private FileStorageService fileStorageService; 

    /**
     * Xử lý logic upload file.
     * 1. Lưu file vật lý vào thư mục 'uploads'.
     * 2. Lưu thông tin metadata của file (tên, size, type, chủ sở hữu) vào CSDL.
     * @param multipartFile File được gửi lên từ client.
     * @param username Username của người dùng đang đăng nhập (lấy từ JWT token).
     */
    public void uploadFile(MultipartFile multipartFile, String username) {
        // Tìm đối tượng User chủ sở hữu file
        User owner = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        // Bước 1: Dùng FileStorageService để lưu file vật lý và lấy về tên file đã được mã hóa
        String storedFileName = fileStorageService.save(multipartFile);
        String storedPath = "uploads/" + storedFileName; 

        // Bước 2: Tạo đối tượng File (metadata) để lưu vào CSDL
        File fileMetadata = new File();
        fileMetadata.setFileName(multipartFile.getOriginalFilename());
        fileMetadata.setStoredPath(storedPath);
        fileMetadata.setFileSize(multipartFile.getSize());
        fileMetadata.setFileType(multipartFile.getContentType());
        fileMetadata.setOwner(owner); // Gán chủ sở hữu cho file

        // Bước 3: Lưu metadata vào CSDL
        fileRepository.save(fileMetadata);
    }

    /**
     * Lấy danh sách tất cả các file thuộc sở hữu của một người dùng.
     * @param username Username của người dùng đang đăng nhập.
     * @return Danh sách các đối tượng File.
     */
    public List<File> getFilesByUsername(String username) {
        // Tìm đối tượng User
        User owner = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        // Dùng hàm findByOwner đã được sửa trong FileRepository
        return fileRepository.findByOwner(owner);
    }

    /**
     * Xử lý logic download file.
     * 1. Tìm thông tin file trong CSDL.
     * 2. Kiểm tra quyền của người yêu cầu.
     * 3. Đọc file vật lý từ đường dẫn đã lưu.
     * 4. Đóng gói file vào một ResponseEntity để trả về cho client.
     * @param fileId ID của file cần tải.
     * @param username Username của người yêu cầu (để kiểm tra quyền).
     * @return ResponseEntity chứa file resource để tải về.
     */
    public ResponseEntity<Resource> downloadFile(Long fileId, String username) {
        // Tìm người dùng đang yêu cầu tải file
        User requester = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found!"));

        // Tìm thông tin file trong CSDL
        File fileMetadata = fileRepository.findById(fileId)
            .orElseThrow(() -> new RuntimeException("File not found with id: " + fileId));

        // BƯỚC QUAN TRỌNG: Kiểm tra xem người yêu cầu có phải là chủ sở hữu file không
        if (!fileMetadata.getOwner().getId().equals(requester.getId())) {
            // Nếu không phải, ném ra lỗi. Controller sẽ bắt lỗi này và trả về 400 Bad Request.
            throw new RuntimeException("Error: You don't have permission to download this file!");
        }

        try {
            // Lấy đường dẫn file vật lý
            Path filePath = Paths.get(fileMetadata.getStoredPath());
            Resource resource = new UrlResource(filePath.toUri());

            // Kiểm tra xem file có thực sự tồn tại và có thể đọc được không
            if (resource.exists() && resource.isReadable()) {
                // Nếu có, "đóng gói" file vào ResponseEntity để trả về
                return ResponseEntity.ok()
                    // Header 1: Content-Type -> Cho trình duyệt biết đây là loại file gì (vd: image/png)
                    .contentType(MediaType.parseMediaType(fileMetadata.getFileType()))
                    // Header 2: Content-Disposition -> Cho trình duyệt biết nên tải file về với tên gốc là gì
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileMetadata.getFileName() + "\"")
                    .body(resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}