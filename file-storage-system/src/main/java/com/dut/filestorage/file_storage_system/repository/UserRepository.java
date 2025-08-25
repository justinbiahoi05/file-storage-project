package com.dut.filestorage.file_storage_system.repository;

import com.dut.filestorage.file_storage_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

}

// `save(User user)`: Lưu một user mới hoặc cập nhật user có sẵn.
// `findById(Long id)`: Tìm user theo ID.
// `findAll()`: Lấy tất cả user.
// `deleteById(Long id)`: Xóa user theo ID.
