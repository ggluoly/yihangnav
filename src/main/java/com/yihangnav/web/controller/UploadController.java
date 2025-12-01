package com.yihangnav.web.controller;

import com.yihangnav.web.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/upload")
public class UploadController {

    private final String uploadDir;

    public UploadController(@Value("${app.upload.dir}") String uploadDir) {
        this.uploadDir = uploadDir;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> upload(MultipartFile file, HttpServletRequest request) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.fail("file is empty"));
        }
        try {
            String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String name = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()) + "-" + UUID.randomUUID().toString() + (ext != null ? "." + ext : "");
            Path targetDir = Paths.get(uploadDir);
            Files.createDirectories(targetDir);
            Path target = targetDir.resolve(name);
            file.transferTo(target.toFile());
            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/uploads/" + name;
            return ResponseEntity.ok(ApiResponse.ok(url));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail("upload failed: " + e.getMessage()));
        }
    }
}
