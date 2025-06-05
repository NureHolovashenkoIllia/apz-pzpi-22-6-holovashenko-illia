package ua.nure.holovashenko.flameguard_mobile.data.remote

import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path
import ua.nure.holovashenko.flameguard_mobile.data.dto.UserAccountDto

interface UserApi {

    @PUT("api/users/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Body updatedUser: UserAccountDto
    ): UserAccountDto
}