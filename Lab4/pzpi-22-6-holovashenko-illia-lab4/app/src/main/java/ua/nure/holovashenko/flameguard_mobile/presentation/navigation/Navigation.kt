package ua.nure.holovashenko.flameguard_mobile.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Buildings : Screen("buildings")
    object Diagrams : Screen("diagrams")
    object Profile : Screen("profile")
    object Sensors : Screen("sensors/{buildingId}") {
        fun createRoute(buildingId: Int) = "sensors/$buildingId"
    }
    object Measurements : Screen("measurements/{sensorId}") {
        fun createRoute(sensorId: Int) = "measurements/$sensorId"
    }
}
