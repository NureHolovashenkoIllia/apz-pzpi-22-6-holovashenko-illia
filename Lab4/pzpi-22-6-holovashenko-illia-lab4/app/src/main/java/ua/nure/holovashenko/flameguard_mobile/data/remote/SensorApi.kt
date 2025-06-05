package ua.nure.holovashenko.flameguard_mobile.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ua.nure.holovashenko.flameguard_mobile.data.dto.SensorDto

interface SensorApi {

    @GET("api/sensors/{id}")
    suspend fun getSensorById(@Path("id") id: Int): SensorDto

    @GET("api/sensors/building/{buildingId}")
    suspend fun getSensorsByBuildingId(@Path("buildingId") buildingId: Int): List<SensorDto>

    @POST("api/sensors")
    suspend fun createSensor(@Body sensorDto: SensorDto): SensorDto
}
