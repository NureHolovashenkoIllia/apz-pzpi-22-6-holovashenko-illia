package ua.nure.holovashenko.flameguard_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.nure.holovashenko.flameguard_api.entity.Sensor;
import ua.nure.holovashenko.flameguard_api.entity.SensorSettings;


import java.util.Optional;

@Repository
public interface SensorSettingsRepository extends JpaRepository<SensorSettings, Integer> {
    Optional<SensorSettings> findBySensor(Sensor sensor);
    Optional<SensorSettings> findBySensor_SensorId(Integer sensorId);

}
