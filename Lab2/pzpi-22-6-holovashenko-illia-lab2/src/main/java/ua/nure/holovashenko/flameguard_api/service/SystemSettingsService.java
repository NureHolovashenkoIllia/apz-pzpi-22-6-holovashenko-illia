package ua.nure.holovashenko.flameguard_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.holovashenko.flameguard_api.entity.SystemSettings;
import ua.nure.holovashenko.flameguard_api.repository.SystemSettingsRepository;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Service
public class SystemSettingsService {

    @Autowired
    private SystemSettingsRepository systemSettingsRepository;

    private final DataSource dataSource;

    private static final Logger logger = LoggerFactory.getLogger(SystemSettingsService.class);

    public SystemSettingsService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<SystemSettings> getAllSettings() {
        return systemSettingsRepository.findAll();
    }

    public Optional<SystemSettings> updateSettingValue(String key, String value) {
        Optional<SystemSettings> settingOptional = systemSettingsRepository.findById(key);
        if (settingOptional.isPresent()) {
            SystemSettings setting = settingOptional.get();
            setting.setSettingValue(value);
            systemSettingsRepository.save(setting);
            return Optional.of(setting);
        }
        return Optional.empty();
    }

    public void createDatabaseBackup(String backupFilePath) {
        backupFilePath = ensureSqlExtension(backupFilePath);
        Path backupPath = prepareBackupFile(backupFilePath);

        String command = String.format("mysqldump -u%s -p%s %s > '%s'",
                "root", "12345678", "FlameGuard", backupPath.toAbsolutePath());

        executeShellCommand(command, "Backup created successfully at %s", "Failed to create database backup");
    }

    public void restoreDatabase(String backupFilePath) {
        if (!backupFilePath.endsWith(".sql")) {
            throw new IllegalArgumentException("Backup file must be a .sql file");
        }

        String command = String.format("mysql -u%s -p%s %s < '%s'",
                "root", "12345678", "FlameGuard", backupFilePath);

        executeShellCommand(command, "Database restored successfully from %s", "Failed to restore database");
    }

    private String ensureSqlExtension(String path) {
        return path.endsWith(".sql") ? path : path + ".sql";
    }

    private Path prepareBackupFile(String backupFilePath) {
        Path backupPath = Paths.get(backupFilePath);
        try {
            if (backupPath.getParent() != null) {
                Files.createDirectories(backupPath.getParent());
            }

            if (!Files.exists(backupPath)) {
                Files.createFile(backupPath);
            }

            if (!Files.isWritable(backupPath)) {
                throw new IOException("No write permission to the backup file path.");
            }

            return backupPath;

        } catch (IOException e) {
            logger.error("Failed to prepare backup file: {}", e.getMessage(), e);
            throw new RuntimeException("Could not prepare backup file", e);
        }
    }

    private void executeShellCommand(String command, String successMessageFormat, String errorMessage) {
        String[] shellCommand = {"bash", "-c", command};

        try {
            ProcessBuilder pb = new ProcessBuilder(shellCommand);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info("[shell] {}", line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                logger.info(successMessageFormat, command);
            } else {
                logger.error("{} with exit code {}", errorMessage, exitCode);
                throw new RuntimeException(errorMessage + ". Exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            logger.error("Error executing command '{}': {}", command, e.getMessage(), e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    public SystemSettings saveOrUpdateSetting(String settingKey, String settingValue) {
        Optional<SystemSettings> existingSetting = systemSettingsRepository.findById(settingKey);

        SystemSettings systemSettings = existingSetting.orElse(new SystemSettings());
        systemSettings.setSettingKey(settingKey);
        systemSettings.setSettingValue(settingValue);

        return systemSettingsRepository.save(systemSettings);
    }

    public String getMeasurementsCheckInterval() {
        return systemSettingsRepository.findById("measurements_check_interval")
                .map(SystemSettings::getSettingValue)
                .orElse("300000"); // Значення за замовчуванням: "1800000" мілісекунд (30 хвилин)
    }
}