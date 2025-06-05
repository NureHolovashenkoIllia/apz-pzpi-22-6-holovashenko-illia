package ua.nure.holovashenko.flameguard_mobile.presentation.viewmodel.diagrams

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ua.nure.holovashenko.flameguard_mobile.data.dto.BuildingDto
import ua.nure.holovashenko.flameguard_mobile.data.dto.MeasurementDto
import ua.nure.holovashenko.flameguard_mobile.data.dto.SensorDto
import ua.nure.holovashenko.flameguard_mobile.data.remote.BuildingApi
import ua.nure.holovashenko.flameguard_mobile.data.remote.MeasurementApi
import ua.nure.holovashenko.flameguard_mobile.data.remote.SensorApi
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class DiagramsViewModel @Inject constructor(
    private val buildingApi: BuildingApi,
    private val sensorApi: SensorApi,
    private val measurementApi: MeasurementApi
) : ViewModel() {

    var buildings by mutableStateOf<List<BuildingDto>>(emptyList())
        private set
    var selectedBuilding by mutableStateOf<BuildingDto?>(null)

    var sensorTypeFilter by mutableStateOf("All")
    var sensors by mutableStateOf<List<SensorDto>>(emptyList())
    var measurementsMap by mutableStateOf<Map<Int, List<MeasurementDto>>>(emptyMap())

    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    fun loadBuildings() {
        viewModelScope.launch {
            try {
                isLoading = true
                buildings = buildingApi.getUserBuildings()
            } catch (e: Exception) {
                error = "Failed to load buildings: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun onBuildingSelected(building: BuildingDto) {
        selectedBuilding = building
        loadSensorsAndMeasurements(building.buildingId!!)
    }

    fun loadSensorsAndMeasurements(buildingId: Int) {
        viewModelScope.launch {
            try {
                isLoading = true
                sensors = sensorApi.getSensorsByBuildingId(buildingId)
                val map = sensors.associateWith { sensor ->
                    runCatching { measurementApi.getMeasurementsBySensor(sensor.sensorId!!) }
                        .getOrDefault(emptyList())
                }
                measurementsMap = map.mapKeys { it.key.sensorId!! }
            } catch (e: Exception) {
                error = "Failed to load sensors or measurements: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun getFilteredSensors(): List<SensorDto> {
        return if (sensorTypeFilter == "All") sensors
        else sensors.filter { it.sensorType.equals(sensorTypeFilter, ignoreCase = true) }
    }

    fun getSensorTypes(): List<String> {
        return listOf("All") + sensors.map { it.sensorType }.distinct()
    }
}
