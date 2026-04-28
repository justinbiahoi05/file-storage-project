# File Storage Project

## Mô tả
Ứng dụng quản lý và lưu trữ file được xây dựng trên **Spring Boot 3.5.4**, cung cấp các tính năng upload, download và quản lý file một cách an toàn và hiệu quả.

## Tính năng
- ✅ Upload file
- ✅ Download file
- ✅ Lấy danh sách file
- ✅ Xác thực người dùng (JWT Token)
- ✅ Bảo mật với Spring Security

## Công nghệ sử dụng
- **Java 21**
- **Spring Boot 3.5.4**
- **Spring Data JPA**
- **Spring Security**
- **MySQL Database**
- **JWT (JSON Web Token)**
- **Thymeleaf** (Template Engine)
- **Maven**

## Yêu cầu hệ thống
- Java 21+
- MySQL 8.0+
- Maven 3.6+

## Cài đặt
1. Clone repository
   ```bash
   git clone https://github.com/justinbiahoi05/file-storage-project.git
   cd file-storage-project/file-storage-system
   ```

2. Cấu hình database trong `application.properties` hoặc `application.yml`
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/file_storage
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

3. Chạy ứng dụng
   ```bash
   mvn spring-boot:run
   ```

## Cấu trúc dự án
```
file-storage-project/
├── file-storage-system/
│   ├── src/
│   │   ├── main/java/com/dut/filestorage/
│   │   └── main/resources/
│   ├── pom.xml
│   └── ...
├── README.md
└── .gitignore
```

## API Endpoints
- `POST /upload` - Upload file
- `GET /download/{fileId}` - Download file
- `GET /files` - Lấy danh sách file
- `DELETE /files/{fileId}` - Xóa file

## Tác giả
- **justinbiahoi05**

## License
MIT License
