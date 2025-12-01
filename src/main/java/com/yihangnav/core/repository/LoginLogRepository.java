package com.yihangnav.core.repository;

import com.yihangnav.core.domain.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {
    List<LoginLog> findTop15ByOrderByLoginAtDesc();
}
