package com.dut.filestorage.file_storage_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "stored_path", nullable = false, length = 500)
    private String storedPath;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "file_type", length = 255)
    private String fileType;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate = LocalDateTime.now();

    // Nhiều File thuộc về một User (chủ sở hữu)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // Nhiều File có thể nằm trong một Folder
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private Folder folder;

    // Một File có thể có nhiều phiên bản
    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FileVersion> versions;
}