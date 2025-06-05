package ua.nure.holovashenko.flameguard_mobile.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ua.nure.holovashenko.flameguard_mobile.presentation.ui.auth.LoginScreen
import ua.nure.holovashenko.flameguard_mobile.presentation.ui.auth.RegisterScreen
import ua.nure.holovashenko.flameguard_mobile.presentation.ui.main.MainTabHost
import ua.nure.holovashenko.flameguard_mobile.presentation.ui.measurements.MeasurementsScreen
import ua.nure.holovashenko.flameguard_mobile.presentation.ui.sensors.SensorsScreen

@Composable
fun FlameGuardNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController, startDestination = Screen.Login.route) {

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Screen.Buildings.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }},
                onRegisterClick = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onLoginClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Buildings.route) {
            MainTabHost(navController)
        }

        composable(Screen.Sensors.route) { backStackEntry ->
            val buildingId = backStackEntry.arguments?.getString("buildingId")?.toIntOrNull()

            if (buildingId != null) {
                SensorsScreen(
                    buildingId = buildingId,
                    onViewMeasurements = { sensorId ->
                        navController.navigate(Screen.Measurements.createRoute(sensorId))
                    }
                )
            } else {
                Text("Invalid building ID", modifier = Modifier.fillMaxSize())
            }
        }

        composable(Screen.Measurements.route) {backStackEntry ->
            val sensorId = backStackEntry.arguments?.getString("sensorId")?.toIntOrNull()

            if (sensorId != null) {
                MeasurementsScreen(
                    sensorId = sensorId
                )
            } else {
                Text("Invalid sensor ID", modifier = Modifier.fillMaxSize())
            }
        }
    }
}
