package com.yihangnav.core.repository;

import com.yihangnav.core.domain.BackupRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BackupRecordRepository extends JpaRepository<BackupRecord, Long> {
    @Query("select b from BackupRecord b order by b.createdAt desc")
    List<BackupRecord> findAllOrderByCreated();
}
