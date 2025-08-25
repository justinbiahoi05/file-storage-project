package com.dut.filestorage.file_storage_system.controller;

import com.dut.filestorage.file_storage_system.entity.User;
import com.dut.filestorage.file_storage_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            return ResponseEntity.ok("Đăng ký thành công!");

        } catch (RuntimeException e) {
            // Bắt lỗi đó và trả về thông báo lỗi cho client
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}