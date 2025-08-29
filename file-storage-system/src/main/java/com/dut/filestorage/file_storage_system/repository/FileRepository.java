package com.dut.filestorage.file_storage_system.repository;

import com.dut.filestorage.file_storage_system.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
}