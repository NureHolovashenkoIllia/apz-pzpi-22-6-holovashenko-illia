package ua.nure.holovashenko.flameguard_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Entity
@Table(name = "sensor_settings")
public class SensorSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sensor_settings_id", nullable = false)
    private Integer sensorSettingsId;

    @Column(name = "sensor_critical_value", nullable = false)
    private Float sensorCriticalValue;

    @Column(name = "measurement_frequency", nullable = false)
    private Integer measurementFrequency;

    @Column(name = "service_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal serviceCost;

    @OneToOne
    @JoinColumn(name = "sensor_id", nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Sensor sensor;

    // Getters and Setters
    public Integer getSensorSettingsId() {
        return sensorSettingsId;
    }

    public void setSensorSettingsId(Integer sensorSettingsId) {
        this.sensorSettingsId = sensorSettingsId;
    }

    public Float getSensorCriticalValue() {
        return sensorCriticalValue;
    }

    public void setSensorCriticalValue(Float sensorCriticalValue) {
        this.sensorCriticalValue = sensorCriticalValue;
    }

    public Integer getMeasurementFrequency() {
        return measurementFrequency;
    }

    public void setMeasurementFrequency(Integer measurementFrequency) {
        this.measurementFrequency = measurementFrequency;
    }

    public BigDecimal getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(BigDecimal serviceCost) {
        this.serviceCost = serviceCost;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
}