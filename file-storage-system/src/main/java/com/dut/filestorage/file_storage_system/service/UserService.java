package com.dut.filestorage.file_storage_system.service;

import com.dut.filestorage.file_storage_system.entity.User;
import com.dut.filestorage.file_storage_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
               if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                        throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }

        // Mã hóa mật khẩu
        String rawPassword = user.getPassword();
        // ...mã hóa để tạo ra một chuỗi hash
        String encodedPassword = passwordEncoder.encode(rawPassword);
        // Cập nhật lại mật khẩu đã mã hóa user
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }
}