package ua.nure.holovashenko.flameguard_api.dto;

import java.time.LocalDateTime;

public class SensorDto {

    private int sensorId;

    private String sensorName;

    private String sensorType;

    private String sensorStatus;

    private LocalDateTime lastDataReceived;

    private LocalDateTime dateAdded;

    private Integer buildingId;

    public SensorDto(int sensorId, String sensorName, String sensorType, String sensorStatus,
                     LocalDateTime lastDataReceived, LocalDateTime dateAdded, Integer buildingId) {
        this.sensorId = sensorId;
        this.sensorName = sensorName;
        this.sensorType = sensorType;
        this.sensorStatus = sensorStatus;
        this.lastDataReceived = lastDataReceived;
        this.dateAdded = dateAdded;
        this.buildingId = buildingId;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getSensorStatus() {
        return sensorStatus;
    }

    public void setSensorStatus(String sensorStatus) {
        this.sensorStatus = sensorStatus;
    }

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public LocalDateTime getLastDataReceived() {
        return lastDataReceived;
    }

    public void setLastDataReceived(LocalDateTime lastDataReceived) {
        this.lastDataReceived = lastDataReceived;
    }
}
