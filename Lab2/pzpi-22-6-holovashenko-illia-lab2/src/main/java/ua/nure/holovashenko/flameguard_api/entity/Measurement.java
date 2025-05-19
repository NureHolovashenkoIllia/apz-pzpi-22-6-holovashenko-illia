package ua.nure.holovashenko.flameguard_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "measurement")
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "measurement_id", nullable = false)
    private Integer measurementId;

    @NotNull
    @Column(name = "measurement_value", nullable = false)
    private Float measurementValue;


    @NotNull
    @Size(max = 20)
    @Column(name = "measurement_unit", length = 20, nullable = false)
    private String measurementUnit;

    @NotNull
    @Column(name = "date_time_received", nullable = false)
    private LocalDateTime dateTimeReceived;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    // Getters and Setters
    public Integer getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(Integer measurementId) {
        this.measurementId = measurementId;
    }

    public Float getMeasurementValue() {
        return measurementValue;
    }

    public void setMeasurementValue(Float measurementValue) {
        this.measurementValue = measurementValue;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public LocalDateTime getDateTimeReceived() {
        return dateTimeReceived;
    }

    public void setDateTimeReceived(LocalDateTime dateTimeReceived) {
        this.dateTimeReceived = dateTimeReceived;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
}
