package ua.nure.holovashenko.flameguard_mobile.presentation.viewmodel.buildings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ua.nure.holovashenko.flameguard_mobile.data.dto.BuildingDto
import ua.nure.holovashenko.flameguard_mobile.data.remote.BuildingApi
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class BuildingsViewModel @Inject constructor(
    private val buildingApi: BuildingApi
) : ViewModel() {

    var buildings by mutableStateOf<List<BuildingDto>>(emptyList())
    var isLoading by mutableStateOf(false)
    var isDialogOpen by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    var name by mutableStateOf("")
    var description by mutableStateOf("")
    var type by mutableStateOf("")
    var condition by mutableStateOf("")

    fun loadBuildings() {
        viewModelScope.launch {
            isLoading = true
            try {
                buildings = buildingApi.getUserBuildings()
            } catch (e: Exception) {
                error = "Failed to load buildings: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    fun createBuilding(userId: Int, onSuccess: () -> Unit) {
        if (name.isBlank() || type.isBlank() || condition.isBlank()) {
            error = "All fields must be filled"
            return
        }

        val newBuilding = BuildingDto(
            buildingName = name,
            buildingDescription = description,
            buildingType = type,
            buildingCondition = condition,
            creationDate = LocalDate.now().toString(),
            userAccountId = userId
        )

        viewModelScope.launch {
            try {
                buildingApi.createBuilding(newBuilding)
                isDialogOpen = false
                clearForm()
                loadBuildings()
                onSuccess()
            } catch (e: Exception) {
                error = "Failed to create building: ${e.localizedMessage}"
            }
        }
    }

    fun clearForm() {
        name = ""
        description = ""
        type = ""
        condition = ""
        error = null
    }
}