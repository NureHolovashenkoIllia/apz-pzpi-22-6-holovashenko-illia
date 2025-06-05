package ua.nure.holovashenko.flameguard_mobile.presentation.viewmodel.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ua.nure.holovashenko.flameguard_mobile.data.dto.UserAccountDto
import ua.nure.holovashenko.flameguard_mobile.data.remote.AuthApi
import ua.nure.holovashenko.flameguard_mobile.data.remote.UserApi
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authApi: AuthApi,
    private val userApi: UserApi
) : ViewModel() {

    var profile by mutableStateOf<UserAccountDto?>(null)
    var isLoading by mutableStateOf(true)
    var errorMessage by mutableStateOf<String?>(null)
    var isUpdating by mutableStateOf(false)
    var updateSuccess by mutableStateOf(false)
    var isEditMode by mutableStateOf(false)

    fun loadProfile() {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                profile = authApi.getProfile()
            } catch (e: Exception) {
                errorMessage = "Failed to load profile: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    fun updateProfile() {
        val user = profile ?: return

        viewModelScope.launch {
            try {
                isUpdating = true
                updateSuccess = false
                userApi.updateUser(user.userAccountId!!, user)
                updateSuccess = true
                isEditMode = false
            } catch (e: Exception) {
                errorMessage = "Failed to update profile: ${e.localizedMessage}"
            } finally {
                isUpdating = false
            }
        }
    }

    fun toggleEditMode() {
        isEditMode = !isEditMode
        updateSuccess = false
    }
}
