package ua.nure.holovashenko.flameguard_mobile.data.dto

data class SensorDto(
    val sensorId: Int? = null,
    val sensorName: String,
    val sensorType: String,
    val sensorStatus: String,
    val lastDataReceived: String?,
    val dateAdded: String?,
    val buildingId: Int
)
