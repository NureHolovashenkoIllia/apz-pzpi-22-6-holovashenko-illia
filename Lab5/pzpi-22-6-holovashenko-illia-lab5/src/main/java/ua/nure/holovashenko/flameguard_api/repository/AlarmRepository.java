package ua.nure.holovashenko.flameguard_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.nure.holovashenko.flameguard_api.entity.Alarm;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Integer> {
    List<Alarm> findAlarmBySensor_SensorId(Integer sensorId);
    List<Alarm> findAlarmBySensor_Building_BuildingId(Integer buildingId);
}
