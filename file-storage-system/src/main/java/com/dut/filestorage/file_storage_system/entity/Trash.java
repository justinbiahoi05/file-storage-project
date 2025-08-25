package com.dut.filestorage.file_storage_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "trash")
public class Trash {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trash_id")
    private Long id;

    @Column(name = "delete_at")
    private LocalDateTime deleteAt = LocalDateTime.now();

    @Column(name = "expire_at")
    private LocalDateTime expireAt;

    // Nhiều mục trong trash thuộc về một User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // (Các khóa ngoại đến File và Folder sẽ được xử lý ở tầng logic
    // để tránh việc một file bị xóa vĩnh viễn khỏi bảng files mà vẫn còn
    // khóa ngoại ở đây, gây lỗi. Chúng ta chỉ lưu ID của chúng)
    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "folder_id")
    private Long folderId;
}