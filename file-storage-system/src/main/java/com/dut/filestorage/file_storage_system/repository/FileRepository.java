package com.dut.filestorage.file_storage_system.repository;

import com.dut.filestorage.file_storage_system.entity.File;
import com.dut.filestorage.file_storage_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByOwner(User owner);
}