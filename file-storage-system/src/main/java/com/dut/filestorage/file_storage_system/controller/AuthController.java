package com.dut.filestorage.file_storage_system.controller;

import com.dut.filestorage.file_storage_system.config.JwtUtils;
import com.dut.filestorage.file_storage_system.dto.JwtResponse;
import com.dut.filestorage.file_storage_system.dto.LoginRequest;
import com.dut.filestorage.file_storage_system.dto.RegisterRequest;
import com.dut.filestorage.file_storage_system.entity.User;
import com.dut.filestorage.file_storage_system.repository.UserRepository;
import com.dut.filestorage.file_storage_system.service.UserService; // Import UserService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Bỏ UserRepository và PasswordEncoder, thay bằng UserService
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    // Cần UserRepository ở đây để lấy thông tin đầy đủ sau khi login
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            // Chỉ cần gọi service để xử lý
            userService.registerUser(registerRequest);
            return ResponseEntity.ok("User registered successfully!");
        } catch (RuntimeException e) {
            // Bắt lỗi từ service và trả về thông báo
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Hàm login giữ nguyên, nó đã rất gọn rồi
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).get();

        return ResponseEntity.ok(new JwtResponse(jwt, user.getId(), user.getUsername()));
    }
}