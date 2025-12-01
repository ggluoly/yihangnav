package com.yihangnav.core.service;

import com.yihangnav.core.domain.UserAccount;
import com.yihangnav.core.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final com.yihangnav.core.repository.LoginLogRepository loginLogRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, com.yihangnav.core.repository.LoginLogRepository loginLogRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginLogRepository = loginLogRepository;
    }

    @Transactional
    public UserAccount ensureAdmin(String username, String rawPassword) {
        Optional<UserAccount> existing = userRepository.findByUsername(username);
        if (existing.isPresent()) {
            return existing.get();
        }
        UserAccount user = new UserAccount();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole("ADMIN");
        user.setActive(true);
        return userRepository.save(user);
    }

    public Optional<UserAccount> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserAccount save(UserAccount account) {
        return userRepository.save(account);
    }

    @Transactional
    public void recordLogin(UserAccount account, String ip, String ua) {
        LocalDateTime now = LocalDateTime.now();
        account.setLastLoginAt(now);
        account.setLastLoginIp(ip);
        account.setLastLoginUa(ua);
        userRepository.save(account);
        com.yihangnav.core.domain.LoginLog log = new com.yihangnav.core.domain.LoginLog();
        log.setUsername(account.getUsername());
        log.setIp(ip);
        log.setUa(ua);
        log.setLoginAt(now);
        loginLogRepository.save(log);
    }
}
