package com.dut.filestorage.file_storage_system.service;

import com.dut.filestorage.file_storage_system.dto.RegisterRequest;
import com.dut.filestorage.file_storage_system.entity.User;
import com.dut.filestorage.file_storage_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Tiêm PasswordEncoder vào đây

    public User registerUser(RegisterRequest registerRequest) {
        // 1a. Kiểm tra username đã tồn tại chưa
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        // 1b. Kiểm tra email đã tồn tại chưa (MỚI)
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        // 2a. Tạo đối tượng User mới và gán username
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        
        // 2b. Gán email cho user (MỚI)
        user.setEmail(registerRequest.getEmail());
        
        // 3. Mã hóa mật khẩu
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        
        // 4. Lưu User vào CSDL và trả về
        return userRepository.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}