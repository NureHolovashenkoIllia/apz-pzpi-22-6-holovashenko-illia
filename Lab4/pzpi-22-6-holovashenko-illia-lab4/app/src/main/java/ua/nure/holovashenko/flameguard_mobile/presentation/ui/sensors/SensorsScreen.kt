package ua.nure.holovashenko.flameguard_mobile.presentation.ui.sensors

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import ua.nure.holovashenko.flameguard_mobile.presentation.viewmodel.sensors.SensorsViewModel

@Composable
fun SensorsScreen(
    buildingId: Int,
    onViewMeasurements: (sensorId: Int) -> Unit,
    viewModel: SensorsViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadData(buildingId)
    }

    val building = viewModel.building
    val sensors = viewModel.sensors
    val isLoading = viewModel.isLoading
    val isDialogOpen = viewModel.isDialogOpen

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.isDialogOpen = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add sensor")
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
                building?.let {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(it.buildingName, style = MaterialTheme.typography.titleLarge)
                            Text(it.buildingDescription, style = MaterialTheme.typography.bodyMedium)
                            Text("Type: ${it.buildingType}")
                            Text("Condition: ${it.buildingCondition}")
                            Text("Created: ${it.creationDate}")
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Sensors in ${building?.buildingName ?: ""}",
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
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                items(sensors) { sensor ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(20.dp).fillMaxWidth()) {
                            Text(sensor.sensorName, style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(4.dp))
                            Text("Type: ${sensor.sensorType}", style = MaterialTheme.typography.labelMedium)
                            Text("Status: ${sensor.sensorStatus}", style = MaterialTheme.typography.labelMedium)
                            Text("Added: ${sensor.dateAdded ?: "N/A"}", style = MaterialTheme.typography.labelSmall)
                            Text(
                                "Last data: ${sensor.lastDataReceived ?: "No data yet"}",
                                style = MaterialTheme.typography.labelSmall
                            )
                            Spacer(Modifier.height(12.dp))
                            Button(
                                onClick = { onViewMeasurements(sensor.sensorId!!) },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("View Measurements")
                            }
                        }
                    }
                }
            }

            if (sensors.isEmpty() && !isLoading) {
                item {
                    Text(
                        text = "No sensors found for this building.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        if (isDialogOpen) {
            AddSensorDialog(
                viewModel = viewModel,
                buildingId = buildingId,
                onDismiss = { viewModel.isDialogOpen = false }
            )
        }
    }
}
