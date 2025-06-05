package ua.nure.holovashenko.flameguard_mobile.presentation.viewmodel.measurements

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ua.nure.holovashenko.flameguard_mobile.data.dto.MeasurementDto
import ua.nure.holovashenko.flameguard_mobile.data.dto.SensorDto
import ua.nure.holovashenko.flameguard_mobile.data.remote.MeasurementApi
import ua.nure.holovashenko.flameguard_mobile.data.remote.SensorApi
import javax.inject.Inject

@HiltViewModel
class MeasurementsViewModel @Inject constructor(
    private val sensorApi: SensorApi,
    private val measurementApi: MeasurementApi
) : ViewModel() {

    var sensor by mutableStateOf<SensorDto?>(null)
        private set

    var measurements by mutableStateOf<List<MeasurementDto>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun loadSensorAndMeasurements(sensorId: Int) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                sensor = sensorApi.getSensorById(sensorId)
            } catch (e: Exception) {
                error = "Failed to load sensor: ${e.localizedMessage}"
                isLoading = false
                return@launch
            }

            try {
                measurements = measurementApi.getMeasurementsBySensor(sensorId)
            } catch (e: retrofit2.HttpException) {
                if (e.code() == 404) {
                    measurements = emptyList()
                } else {
                    error = "Failed to load measurements: ${e.message()}"
                }
            } catch (e: Exception) {
                error = "Unexpected error: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }
}
