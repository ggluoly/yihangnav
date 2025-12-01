package com.yihangnav.web.controller;

import com.yihangnav.core.domain.UserAccount;
import com.yihangnav.core.repository.UserRepository;
import com.yihangnav.core.service.UserService;
import com.yihangnav.web.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class UserAdminController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserAdminController(UserRepository userRepository, UserService userService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ApiResponse<List<UserAccount>> list() {
        return ApiResponse.ok(userRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserAccount>> create(@Valid @RequestBody UserAccount user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body(ApiResponse.fail("username exists"));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.ok(ApiResponse.ok(userService.save(user)));
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@PathVariable Long id, @RequestBody String password) {
        return userRepository.findById(id)
                .map(u -> {
                    u.setPassword(passwordEncoder.encode(password));
                    userRepository.save(u);
                    return ResponseEntity.ok(ApiResponse.<Void>ok(null));
                })
                .orElseGet(() -> ResponseEntity.status(404).body(ApiResponse.<Void>fail("user not found")));
    }
}
