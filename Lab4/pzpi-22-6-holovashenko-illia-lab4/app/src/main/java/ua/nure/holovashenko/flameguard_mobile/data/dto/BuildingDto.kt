package ua.nure.holovashenko.flameguard_mobile.data.dto

data class BuildingDto(
    val buildingId: Int? = null,
    val buildingName: String,
    val buildingDescription: String,
    val buildingType: String,
    val buildingCondition: String,
    val creationDate: String,
    val userAccountId: Int
)