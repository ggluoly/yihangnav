package com.yihangnav.web.controller;

import com.yihangnav.core.domain.UserAccount;
import com.yihangnav.core.repository.BackupRecordRepository;
import com.yihangnav.core.repository.CardRepository;
import com.yihangnav.core.repository.CategoryRepository;
import com.yihangnav.core.repository.FriendLinkRepository;
import com.yihangnav.core.repository.LoginLogRepository;
import com.yihangnav.core.repository.SearchLogRepository;
import com.yihangnav.core.repository.UserRepository;
import com.yihangnav.web.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/stats")
public class StatsController {

    private final UserRepository userRepository;
    private final SearchLogRepository searchLogRepository;
    private final BackupRecordRepository backupRecordRepository;
    private final CategoryRepository categoryRepository;
    private final CardRepository cardRepository;
    private final FriendLinkRepository friendLinkRepository;
    private final LoginLogRepository loginLogRepository;

    public StatsController(UserRepository userRepository,
                           SearchLogRepository searchLogRepository,
                           BackupRecordRepository backupRecordRepository,
                           CategoryRepository categoryRepository,
                           CardRepository cardRepository,
                           FriendLinkRepository friendLinkRepository,
                           LoginLogRepository loginLogRepository) {
        this.userRepository = userRepository;
        this.searchLogRepository = searchLogRepository;
        this.backupRecordRepository = backupRecordRepository;
        this.categoryRepository = categoryRepository;
        this.cardRepository = cardRepository;
        this.friendLinkRepository = friendLinkRepository;
        this.loginLogRepository = loginLogRepository;
    }

    @GetMapping
    public ApiResponse<Map<String, Object>> stats() {
        Map<String, Object> data = new HashMap<>();
        data.put("userCount", userRepository.count());
        data.put("searchCount", searchLogRepository.count());
        data.put("backupCount", backupRecordRepository.count());
        data.put("categoryCount", categoryRepository.count());
        data.put("cardCount", cardRepository.count());
        data.put("friendCount", friendLinkRepository.count());
        List<Map<String, Object>> recent = loginLogRepository.findTop15ByOrderByLoginAtDesc().stream()
                .map(u -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("username", u.getUsername());
                    m.put("lastLoginAt", u.getLoginAt());
                    m.put("lastLoginAtFormatted", formatTime(u.getLoginAt()));
                    m.put("lastLoginIp", u.getIp());
                    m.put("lastLoginUa", u.getUa());
                    return m;
                }).collect(Collectors.toList());
        data.put("recentLogins", recent);
        data.put("generatedAt", LocalDateTime.now());
        return ApiResponse.ok(data);
    }

    private String formatTime(LocalDateTime time) {
        if (time == null) {
            return "";
        }
        return time.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }
}
