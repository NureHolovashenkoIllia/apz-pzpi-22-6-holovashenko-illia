package ua.nure.holovashenko.flameguard_mobile.presentation.ui.buildings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ua.nure.holovashenko.flameguard_mobile.presentation.viewmodel.buildings.BuildingsViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@Composable
fun BuildingsScreen(
    viewModel: BuildingsViewModel = hiltViewModel(),
    onSensorClick: (buildingId: Int) -> Unit,
    userId: Int
) {
    LaunchedEffect(Unit) {
        viewModel.loadBuildings()
    }

    val buildings = viewModel.buildings
    val isLoading = viewModel.isLoading
    val isDialogOpen = viewModel.isDialogOpen

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.isDialogOpen = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add building")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Your Buildings",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }

            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                items(buildings) { building ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(20.dp).fillMaxWidth()) {
                            Text(building.buildingName, style = MaterialTheme.typography.titleLarge)
                            Spacer(Modifier.height(4.dp))
                            Text(building.buildingDescription, style = MaterialTheme.typography.bodyMedium)
                            Spacer(Modifier.height(4.dp))
                            Text("Type: ${building.buildingType}", style = MaterialTheme.typography.labelMedium)
                            Text("Condition: ${building.buildingCondition}", style = MaterialTheme.typography.labelMedium)
                            Spacer(Modifier.height(12.dp))
                            Button(
                                onClick = { onSensorClick(building.buildingId!!) },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("View Sensors")
                            }
                        }
                    }
                }
            }

            if (buildings.isEmpty() && !isLoading) {
                item {
                    Text(
                        text = "You don't have any buildings yet, create your first one now!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        if (isDialogOpen) {
            AddBuildingDialog(
                viewModel = viewModel,
                userId = userId,
                onDismiss = { viewModel.isDialogOpen = false }
            )
        }
    }
}
