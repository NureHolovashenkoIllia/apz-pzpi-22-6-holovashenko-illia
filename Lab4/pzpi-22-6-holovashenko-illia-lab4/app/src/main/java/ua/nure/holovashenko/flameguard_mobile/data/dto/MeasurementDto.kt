package ua.nure.holovashenko.flameguard_mobile.data.dto

data class MeasurementDto(
    val measurementId: Int? = null,
    val measurementValue: Float,
    val measurementUnit: String,
    val dateTimeReceived: String,
    val sensorId: Int
)