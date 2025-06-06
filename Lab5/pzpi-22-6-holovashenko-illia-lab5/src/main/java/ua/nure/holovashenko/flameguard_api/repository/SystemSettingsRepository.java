package ua.nure.holovashenko.flameguard_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.nure.holovashenko.flameguard_api.entity.SystemSettings;

import java.util.Optional;

@Repository
public interface SystemSettingsRepository extends JpaRepository<SystemSettings, String> {
    Optional<SystemSettings> findBySettingKey(String settingKey);
}