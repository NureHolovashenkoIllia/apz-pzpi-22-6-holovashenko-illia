package ua.nure.holovashenko.flameguard_mobile.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ua.nure.holovashenko.flameguard_mobile.data.dto.BuildingDto

interface BuildingApi {

    @GET("api/buildings/user")
    suspend fun getUserBuildings(): List<BuildingDto>

    @GET("api/buildings/{id}")
    suspend fun getBuildingById(@Path("id") id: Int): BuildingDto

    @POST("api/buildings")
    suspend fun createBuilding(@Body buildingDto: BuildingDto): BuildingDto
}
