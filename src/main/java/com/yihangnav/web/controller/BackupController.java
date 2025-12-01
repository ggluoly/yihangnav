package com.yihangnav.web.controller;

import com.yihangnav.core.domain.BackupRecord;
import com.yihangnav.core.service.BackupService;
import com.yihangnav.web.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/backups")
public class BackupController {

    private final BackupService backupService;

    public BackupController(BackupService backupService) {
        this.backupService = backupService;
    }

    @GetMapping
    public ApiResponse<List<BackupRecord>> list() {
        return ApiResponse.ok(backupService.list());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BackupRecord>> backup() {
        try {
            BackupRecord record = backupService.manualBackup();
            return ResponseEntity.ok(ApiResponse.ok("backup created", record));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail("backup failed: " + e.getMessage()));
        }
    }

    @PostMapping("/restore")
    public ResponseEntity<ApiResponse<Void>> restore(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.fail("file is empty"));
        }
        try {
            java.nio.file.Path tmp = java.nio.file.Files.createTempFile("restore-", file.getOriginalFilename());
            file.transferTo(tmp.toFile());
            backupService.restore(tmp);
            return ResponseEntity.ok(ApiResponse.ok("restored", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail("restore failed: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        backupService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>ok(null));
    }
}
