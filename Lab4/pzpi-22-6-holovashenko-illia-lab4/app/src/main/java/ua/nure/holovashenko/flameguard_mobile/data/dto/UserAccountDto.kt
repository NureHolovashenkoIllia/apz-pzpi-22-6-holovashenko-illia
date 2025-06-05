package ua.nure.holovashenko.flameguard_mobile.data.dto

data class UserAccountDto(
    val userAccountId: Int? = null,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String,
    val userPassword: String,
    val userRole: String = "customer"
)