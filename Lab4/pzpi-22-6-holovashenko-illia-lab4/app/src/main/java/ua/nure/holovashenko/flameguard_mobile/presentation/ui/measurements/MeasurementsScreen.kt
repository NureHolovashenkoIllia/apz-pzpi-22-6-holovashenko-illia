package ua.nure.holovashenko.flameguard_mobile.presentation.ui.measurements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ua.nure.holovashenko.flameguard_mobile.presentation.viewmodel.measurements.MeasurementsViewModel

@Composable
fun MeasurementsScreen(
    sensorId: Int,
    viewModel: MeasurementsViewModel = hiltViewModel()
) {
    LaunchedEffect(sensorId) {
        viewModel.loadSensorAndMeasurements(sensorId)
    }

    val sensor = viewModel.sensor
    val measurements = viewModel.measurements
    val isLoading = viewModel.isLoading
    val error = viewModel.error

    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            sensor?.let {
                item {
                    Card(
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.cardElevation(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(it.sensorName, style = MaterialTheme.typography.titleLarge)
                            Text("Type: ${it.sensorType}")
                            Text("Status: ${it.sensorStatus}")
                            Text("Added: ${it.dateAdded ?: "-"}")
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Measurements in ${sensor?.sensorName ?: ""}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }


            if (isLoading) {
                item {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                items(measurements) { measurement ->
                    val bgColor = getSeverityColor(sensor?.sensorType ?: "", measurement.measurementValue)
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = bgColor),
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Value: ${measurement.measurementValue} ${measurement.measurementUnit}", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(4.dp))
                            Text("Received: ${measurement.dateTimeReceived}", style = MaterialTheme.typography.bodySmall)
                        }
                    }

                }
            }

            if (measurements.isEmpty() && !isLoading && error == null) {
                item {
                    Text(
                        text = "No measurements found for this sensor.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (error != null) {
                item {
                    Text(error, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

fun getSeverityColor(type: String, value: Float): Color {
    return when (type.lowercase()) {
        "temperature" -> getColorByRange(value, 5f, 65f, 100f)
        "gas" -> getColorByRange(value, 5f, 30f, 150f)
        "smoke" -> getColorByRange(value, 5f, 50f, 120f)
        "humidity" -> getColorByRange(value, 5f, 20f, 90f)
        else -> Color.White
    }
}

fun getColorByRange(value: Float, min: Float, safe: Float, max: Float): Color {
    return when {
        value < min -> Color(0xFFE3F2FD)
        value in min..safe -> Color(0xFFC8E6C9)
        value in safe..max -> Color(0xFFFFF59D)
        else -> Color(0xFFFFCDD2)
    }
}
