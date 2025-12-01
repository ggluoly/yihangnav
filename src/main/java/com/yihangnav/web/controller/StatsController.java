package com.yihangnav.web.controller;

import com.yihangnav.core.domain.UserAccount;
import com.yihangnav.core.repository.BackupRecordRepository;
import com.yihangnav.core.repository.SearchLogRepository;
import com.yihangnav.core.repository.UserRepository;
import com.yihangnav.web.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/stats")
public class StatsController {

    private final UserRepository userRepository;
    private final SearchLogRepository searchLogRepository;
    private final BackupRecordRepository backupRecordRepository;

    public StatsController(UserRepository userRepository,
                           SearchLogRepository searchLogRepository,
                           BackupRecordRepository backupRecordRepository) {
        this.userRepository = userRepository;
        this.searchLogRepository = searchLogRepository;
        this.backupRecordRepository = backupRecordRepository;
    }

    @GetMapping
    public ApiResponse<Map<String, Object>> stats() {
        Map<String, Object> data = new HashMap<>();
        data.put("userCount", userRepository.count());
        data.put("searchCount", searchLogRepository.count());
        data.put("backupCount", backupRecordRepository.count());
        UserAccount latest = userRepository.findAll().stream()
                .filter(u -> u.getLastLoginAt() != null)
                .max((a, b) -> a.getLastLoginAt().compareTo(b.getLastLoginAt()))
                .orElse(null);
        if (latest != null) {
            Map<String, Object> login = new HashMap<>();
            login.put("username", latest.getUsername());
            login.put("lastLoginAt", latest.getLastLoginAt());
            login.put("lastLoginIp", latest.getLastLoginIp());
            data.put("lastLogin", login);
        } else {
            data.put("lastLogin", new HashMap<>());
        }
        data.put("generatedAt", LocalDateTime.now());
        return ApiResponse.ok(data);
    }
}
