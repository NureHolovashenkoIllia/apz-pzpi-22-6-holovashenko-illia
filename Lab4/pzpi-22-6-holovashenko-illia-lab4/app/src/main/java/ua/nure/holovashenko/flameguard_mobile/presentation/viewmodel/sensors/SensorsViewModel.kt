package ua.nure.holovashenko.flameguard_mobile.presentation.viewmodel.sensors

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ua.nure.holovashenko.flameguard_mobile.data.dto.BuildingDto
import ua.nure.holovashenko.flameguard_mobile.data.dto.SensorDto
import ua.nure.holovashenko.flameguard_mobile.data.remote.BuildingApi
import ua.nure.holovashenko.flameguard_mobile.data.remote.SensorApi
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject


@HiltViewModel
class SensorsViewModel @Inject constructor(
    private val sensorApi: SensorApi,
    private val buildingApi: BuildingApi
) : ViewModel() {

    var building by mutableStateOf<BuildingDto?>(null)
    var sensors by mutableStateOf<List<SensorDto>>(emptyList())

    var isLoading by mutableStateOf(false)
    var isDialogOpen by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    var sensorName by mutableStateOf("")
    var sensorType by mutableStateOf("temperature")
    var sensorStatus by mutableStateOf("enabled")

    fun loadData(buildingId: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                building = buildingApi.getBuildingById(buildingId)
                sensors = sensorApi.getSensorsByBuildingId(buildingId)
            } catch (e: Exception) {
                error = "Failed to load sensors: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    fun createSensor(buildingId: Int, onSuccess: () -> Unit) {
        if (sensorName.isBlank() || sensorType.isBlank()) {
            error = "Please fill all fields"
            return
        }

        val newSensor = SensorDto(
            sensorName = sensorName,
            sensorType = sensorType,
            sensorStatus = sensorStatus,
            lastDataReceived = null,
            dateAdded = LocalDateTime.now().toString(),
            buildingId = buildingId
        )

        viewModelScope.launch {
            try {
                sensorApi.createSensor(newSensor)
                isDialogOpen = false
                clearForm()
                loadData(buildingId)
                onSuccess()
            } catch (e: Exception) {
                error = "Failed to add sensor: ${e.localizedMessage}"
            }
        }
    }

    fun clearForm() {
        sensorName = ""
        sensorType = ""
        sensorStatus = "enabled"
        error = null
    }
}
