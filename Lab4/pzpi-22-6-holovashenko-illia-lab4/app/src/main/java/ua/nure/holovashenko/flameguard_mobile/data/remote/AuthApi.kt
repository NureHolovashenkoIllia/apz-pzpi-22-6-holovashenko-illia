package ua.nure.holovashenko.flameguard_mobile.data.remote

import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import ua.nure.holovashenko.flameguard_mobile.data.dto.AuthResponse
import ua.nure.holovashenko.flameguard_mobile.data.dto.UserAccountDto

interface AuthApi {

    @POST("api/auth/register")
    suspend fun register(@Body userAccount: UserAccountDto): AuthResponse

    @FormUrlEncoded
    @POST("api/auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): AuthResponse

    @GET("api/auth/profile")
    suspend fun getProfile(): UserAccountDto
}
