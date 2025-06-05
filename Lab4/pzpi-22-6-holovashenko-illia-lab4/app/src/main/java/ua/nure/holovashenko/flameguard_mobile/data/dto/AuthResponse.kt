package ua.nure.holovashenko.flameguard_mobile.data.dto

data class AuthResponse(
    val token: String,
    val id: String,
    val role: String? = null,
    val error: String? = null
)