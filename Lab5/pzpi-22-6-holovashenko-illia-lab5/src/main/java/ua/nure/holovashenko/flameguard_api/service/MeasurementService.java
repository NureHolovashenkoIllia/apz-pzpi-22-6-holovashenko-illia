package ua.nure.holovashenko.flameguard_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.holovashenko.flameguard_api.dto.AlarmDto;
import ua.nure.holovashenko.flameguard_api.dto.DefaultSettings;
import ua.nure.holovashenko.flameguard_api.dto.MeasurementDto;
import ua.nure.holovashenko.flameguard_api.entity.*;
import ua.nure.holovashenko.flameguard_api.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MeasurementService {

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SensorSettingsRepository sensorSettingsRepository;

    @Autowired
    private AlarmService alarmService;

    // Retrieve all measurements
    public List<MeasurementDto> getAllMeasurements() {
        return measurementRepository.findAll().stream()
                .map(this::convertToMeasurementDto)
                .toList();
    }

    // Retrieve a specific measurement by ID
    public Optional<MeasurementDto> getMeasurementById(int id) {
        return measurementRepository.findById(id)
                .map(this::convertToMeasurementDto);
    }

    // Retrieve measurements by sensor ID
    public List<MeasurementDto> getMeasurementsBySensorId(int sensorId) {
        return measurementRepository.findMeasurementsBySensor_SensorId(sensorId)
                .stream()
                .map(this::convertToMeasurementDto)
                .toList();
    }

    // Filter measurements by unit for a specific sensor
    public List<MeasurementDto> filterMeasurementsByUnit(int sensorId, String unit) {
        return measurementRepository.findAll()
                .stream()
                .filter(measurement -> measurement.getSensor().getSensorId() == sensorId
                        && unit.equals(measurement.getMeasurementUnit()))
                .map(this::convertToMeasurementDto)
                .toList();
    }

    // Create a new measurement
    public Optional<MeasurementDto> createMeasurement(MeasurementDto measurementDto) {
        Optional<Sensor> sensor = measurementDto.getSensorId() != null
                ? sensorRepository.findById(measurementDto.getSensorId())
                : Optional.empty();

        Measurement measurement = new Measurement();
        measurement.setMeasurementId(measurementDto.getMeasurementId());
        measurement.setMeasurementValue(measurementDto.getMeasurementValue());
        measurement.setMeasurementUnit(measurementDto.getMeasurementUnit());
        measurement.setDateTimeReceived(measurementDto.getDateTimeReceived());
        sensor.ifPresent(measurement::setSensor);

        Measurement savedMeasurement = measurementRepository.save(measurement);

        // Check for critical value
        if (sensor.isPresent() && isCriticalValue(sensor.get(), measurementDto.getMeasurementValue())) {

            AlarmDto alarmDto = new AlarmDto(null, "fire alarm",
                    LocalDateTime.now(), false,
                    null, sensor.get().getSensorId()
            );
            alarmService.createAlarm(alarmDto)
                    .ifPresent(alarm -> {
                        try {
                            notificationService.sendAlarmNotification(alarm.getAlarmId(), savedMeasurement);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to send notification for alarm: " + alarm.getAlarmId(), e);
                        }
                    });
        }

        return Optional.of(convertToMeasurementDto(savedMeasurement));
    }

    // Delete a measurement
    public boolean deleteMeasurement(Integer id) {
        if (measurementRepository.existsById(id)) {
            measurementRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private MeasurementDto convertToMeasurementDto(Measurement measurement) {
        return new MeasurementDto(
                measurement.getMeasurementId(),
                measurement.getMeasurementValue(),
                measurement.getMeasurementUnit(),
                measurement.getDateTimeReceived(),
                measurement.getSensor() != null ? measurement.getSensor().getSensorId() : null
        );
    }

    private boolean isCriticalValue(Sensor sensor, Float value) {
        SensorSettings sensorSettings = getOrCreateSensorSettings(sensor);
        return value >= sensorSettings.getSensorCriticalValue();
    }

    private SensorSettings getOrCreateSensorSettings(Sensor sensor) {
        return sensorSettingsRepository.findBySensor(sensor)
                .orElseGet(() -> createAndSaveDefaultSettings(sensor));
    }

    private SensorSettings createAndSaveDefaultSettings(Sensor sensor) {
        DefaultSettings defaultSettings = DefaultSettings.getDefaultSettings(sensor.getSensorType());

        SensorSettings sensorSettings = new SensorSettings();
        sensorSettings.setSensor(sensor);
        sensorSettings.setSensorCriticalValue(defaultSettings.getCriticalValue());
        sensorSettings.setMeasurementFrequency(defaultSettings.getMeasurementFrequency());
        sensorSettings.setServiceCost(defaultSettings.getServiceCost());

        return sensorSettingsRepository.save(sensorSettings);
    }
}

