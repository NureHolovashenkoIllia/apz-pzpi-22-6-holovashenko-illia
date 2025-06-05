package ua.nure.holovashenko.flameguard_mobile.presentation.ui.diagrams

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.items
import ua.nure.holovashenko.flameguard_mobile.presentation.viewmodel.diagrams.DiagramsViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun DiagramsScreen(viewModel: DiagramsViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.loadBuildings()
    }

    val buildings = viewModel.buildings
    val selectedBuilding = viewModel.selectedBuilding
    val filteredSensors = viewModel.getFilteredSensors()
    val measurementsMap = viewModel.measurementsMap
    val sensorTypes = viewModel.getSensorTypes()

    val isLoading = viewModel.isLoading
    val error = viewModel.error

    var buildingMenuExpanded by remember { mutableStateOf(false) }
    var sensorMenuExpanded by remember { mutableStateOf(false) }

    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item{
                ExposedDropdownMenuBox(
                    expanded = buildingMenuExpanded,
                    onExpandedChange = { buildingMenuExpanded = !buildingMenuExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedBuilding?.buildingName ?: "Select Building",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Building") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = buildingMenuExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .clickable { buildingMenuExpanded = true }
                    )

                    DropdownMenu(
                        expanded = buildingMenuExpanded,
                        onDismissRequest = { buildingMenuExpanded = false }
                    ) {
                        buildings.forEach { building ->
                            DropdownMenuItem(
                                text = { Text(building.buildingName) },
                                onClick = {
                                    viewModel.onBuildingSelected(building)
                                    buildingMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            if (sensorTypes.size > 1) {
                item {
                    ExposedDropdownMenuBox(
                        expanded = sensorMenuExpanded,
                        onExpandedChange = { sensorMenuExpanded = !sensorMenuExpanded }
                    ) {
                        OutlinedTextField(
                            value = viewModel.sensorTypeFilter,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Sensor Type") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = sensorMenuExpanded)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .clickable { sensorMenuExpanded = true }
                        )
                        DropdownMenu(
                            expanded = sensorMenuExpanded,
                            onDismissRequest = { sensorMenuExpanded = false }
                        ) {
                            sensorTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type) },
                                    onClick = {
                                        viewModel.sensorTypeFilter = type
                                        sensorMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            when {
                isLoading -> item {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                error != null -> item {
                    Text(text = error, color = MaterialTheme.colorScheme.error)
                }

                selectedBuilding != null -> {
                    items(filteredSensors) { sensor ->
                        val formatter = DateTimeFormatter.ofPattern("MM-dd")

                        val data = measurementsMap[sensor.sensorId]?.mapNotNull {
                            try {
                                val parsed = LocalDateTime.parse(it.dateTimeReceived)
                                val label = parsed.format(formatter)
                                label to it.measurementValue.toInt()
                            } catch (e: Exception) {
                                null
                            }
                        } ?: emptyList()

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(sensor.sensorName, style = MaterialTheme.typography.titleMedium)
                            Card(
                                shape = MaterialTheme.shapes.medium,
                                elevation = CardDefaults.cardElevation(6.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(230.dp)
                            ) {
                                BarChart(data = data, modifier = Modifier.padding(12.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
