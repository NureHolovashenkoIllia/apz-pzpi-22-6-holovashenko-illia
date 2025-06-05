package ua.nure.holovashenko.flameguard_mobile.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import ua.nure.holovashenko.flameguard_mobile.data.dto.MeasurementDto

interface MeasurementApi {

    @GET("api/measurements/sensor/{sensorId}")
    suspend fun getMeasurementsBySensor(@Path("sensorId") sensorId: Int): List<MeasurementDto>
}
