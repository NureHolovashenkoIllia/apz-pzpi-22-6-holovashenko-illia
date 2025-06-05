package ua.nure.holovashenko.flameguard_mobile.presentation.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ua.nure.holovashenko.flameguard_mobile.data.local.TokenDataStore
import ua.nure.holovashenko.flameguard_mobile.presentation.navigation.Screen
import ua.nure.holovashenko.flameguard_mobile.presentation.ui.buildings.BuildingsScreen
import ua.nure.holovashenko.flameguard_mobile.presentation.ui.diagrams.DiagramsScreen
import ua.nure.holovashenko.flameguard_mobile.presentation.ui.profile.ProfileScreen

@Composable
fun MainTabHost(
    rootNavController: NavHostController
) {
    val context = LocalContext.current
    val tokenDataStore = remember { TokenDataStore(context) }
    val tabNavController = rememberNavController()
    val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var userId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        userId = tokenDataStore.getUserId()?.toIntOrNull()
    }

    if (userId == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Buildings",
                        modifier = Modifier.size(28.dp)
                    ) },
                    selected = currentRoute == Screen.Buildings.route,
                    onClick = {
                        if (currentRoute != Screen.Buildings.route) {
                            tabNavController.navigate(Screen.Buildings.route) {
                                popUpTo(tabNavController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    )
                )
                NavigationBarItem(
                    icon = { Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "Diagrams",
                        modifier = Modifier.size(28.dp)
                    ) },
                    selected = currentRoute == Screen.Diagrams.route,
                    onClick = {
                        if (currentRoute != Screen.Diagrams.route) {
                            tabNavController.navigate(Screen.Diagrams.route) {
                                popUpTo(tabNavController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    )
                )
                NavigationBarItem(
                    icon = { Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        modifier = Modifier.size(28.dp)
                    ) },
                    selected = currentRoute == Screen.Profile.route,
                    onClick = {
                        if (currentRoute != Screen.Profile.route) {
                            tabNavController.navigate(Screen.Profile.route) {
                                popUpTo(tabNavController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    )
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = tabNavController,
            startDestination = Screen.Buildings.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Buildings.route) {
                BuildingsScreen(
                    onSensorClick = { buildingId ->
                        rootNavController.navigate(Screen.Sensors.createRoute(buildingId))
                    },
                    userId = userId!!
                )
            }
            composable(Screen.Diagrams.route) {
                DiagramsScreen()
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
        }
    }
}
