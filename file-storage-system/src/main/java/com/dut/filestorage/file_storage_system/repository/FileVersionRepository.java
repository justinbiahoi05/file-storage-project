package com.dut.filestorage.file_storage_system.repository;

import com.dut.filestorage.file_storage_system.entity.FileVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileVersionRepository extends JpaRepository<FileVersion, Long> {
}