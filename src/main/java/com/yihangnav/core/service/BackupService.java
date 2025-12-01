package com.yihangnav.core.service;

import com.yihangnav.core.domain.BackupRecord;
import com.yihangnav.core.repository.BackupRecordRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import java.util.stream.Collectors;

@Service
public class BackupService {

    private final BackupRecordRepository backupRecordRepository;
    private final String databasePath;
    private final String backupDir;
    private final String uploadDir;
    private final int maxCopies;
    private final int keepDays;
    private final int keepMonths;

    public BackupService(BackupRecordRepository backupRecordRepository,
                         @Value("${spring.datasource.url}") String databaseUrl,
                         @Value("${app.backup.dir}") String backupDir,
                         @Value("${app.upload.dir}") String uploadDir,
                         @Value("${app.backup.max-copies}") int maxCopies,
                         @Value("${app.backup.keep-days}") int keepDays,
                         @Value("${app.backup.keep-months}") int keepMonths) {
        this.backupRecordRepository = backupRecordRepository;
        this.databasePath = databaseUrl.replace("jdbc:sqlite:", "");
        this.backupDir = backupDir;
        this.uploadDir = uploadDir;
        this.maxCopies = maxCopies;
        this.keepDays = keepDays;
        this.keepMonths = keepMonths;
    }

    @Transactional
    public BackupRecord manualBackup() throws Exception {
        return createBackup("MANUAL");
    }

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void autoBackup() {
        try {
            createBackup("AUTO");
            cleanupHistory();
        } catch (Exception ignored) {
            // avoid failing scheduler
        }
    }

    private BackupRecord createBackup(String type) throws Exception {
        Path dbPath = Path.of(databasePath);
        Files.createDirectories(Path.of(backupDir));
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String fileName = "backup-" + timestamp + ".zip";
        Path target = Path.of(backupDir, fileName);
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(target.toFile()))) {
            addFileToZip(zos, dbPath.toFile(), dbPath.getFileName().toString());
            Path uploadRoot = Path.of(uploadDir);
            if (Files.exists(uploadRoot)) {
                Files.walk(uploadRoot)
                        .filter(Files::isRegularFile)
                        .forEach(p -> {
                            String relative = uploadRoot.relativize(p).toString().replace("\\", "/");
                            addFileToZipUnchecked(zos, p.toFile(), "uploads/" + relative);
                        });
            }
        }

        BackupRecord record = new BackupRecord();
        record.setFileName(fileName);
        record.setFilePath(target.toAbsolutePath().toString());
        record.setFileSize(Files.size(target));
        record.setType(type);
        BackupRecord saved = backupRecordRepository.save(record);
        cleanupByCount();
        return saved;
    }

    private void addFileToZip(ZipOutputStream zos, File file, String entryName) throws Exception {
        addFileToZipUnchecked(zos, file, entryName);
    }

    private void addFileToZipUnchecked(ZipOutputStream zos, File file, String entryName) {
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry entry = new ZipEntry(entryName);
            zos.putNextEntry(entry);
            byte[] buffer = new byte[4096];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            zos.closeEntry();
        } catch (Exception ignored) {
        }
    }

    private void cleanupByCount() {
        List<BackupRecord> records = backupRecordRepository.findAllOrderByCreated();
        if (records.size() <= maxCopies) {
            return;
        }
        List<BackupRecord> toDelete = records.subList(maxCopies, records.size());
        toDelete.forEach(r -> {
            try {
                Files.deleteIfExists(Path.of(r.getFilePath()));
            } catch (Exception ignored) {
            }
        });
        backupRecordRepository.deleteAll(toDelete);
    }

    private void cleanupHistory() {
        LocalDateTime now = LocalDateTime.now();
        List<BackupRecord> records = backupRecordRepository.findAll();
        List<BackupRecord> expired = records.stream()
                .filter(r -> {
                    LocalDateTime created = r.getCreatedAt();
                    if (created == null) {
                        return false;
                    }
                    long days = java.time.Duration.between(created, now).toDays();
                    long months = days / 30;
                    return days > keepDays && months > keepMonths;
                })
                .collect(Collectors.toList());
        expired.forEach(r -> {
            try {
                Files.deleteIfExists(Path.of(r.getFilePath()));
            } catch (Exception ignored) {
            }
        });
        backupRecordRepository.deleteAll(expired);
    }

    public List<BackupRecord> list() {
        return backupRecordRepository.findAllOrderByCreated();
    }

    @Transactional
    public void delete(Long id) {
        backupRecordRepository.findById(id).ifPresent(r -> {
            try {
                Files.deleteIfExists(Path.of(r.getFilePath()));
            } catch (Exception ignored) {
            }
            backupRecordRepository.delete(r);
        });
    }

    @Transactional
    public void restore(Path source) throws Exception {
        try (InputStream is = Files.newInputStream(source); ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;
            Path dbPath = Path.of(databasePath);
            Path uploadRoot = Path.of(uploadDir);
            Files.createDirectories(dbPath.getParent() == null ? Path.of(".") : dbPath.getParent());
            Files.createDirectories(uploadRoot);
            while ((entry = zis.getNextEntry()) != null) {
                String name = entry.getName();
                if (entry.isDirectory()) {
                    continue;
                }
                if (name.equals(dbPath.getFileName().toString()) || name.endsWith("/" + dbPath.getFileName())) {
                    Files.copy(zis, dbPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                } else if (name.startsWith("uploads/")) {
                    Path target = uploadRoot.resolve(name.substring("uploads/".length()));
                    Files.createDirectories(target.getParent());
                    Files.copy(zis, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }
}
