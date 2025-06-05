package ua.nure.holovashenko.flameguard_mobile.presentation.viewmodel.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ua.nure.holovashenko.flameguard_mobile.data.dto.UserAccountDto
import ua.nure.holovashenko.flameguard_mobile.data.remote.AuthApi
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authApi: AuthApi
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var phone by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun onRegisterClick(onSuccess: () -> Unit) {
        if (!email.contains("@") || password.length < 6 || firstName.isBlank() || lastName.isBlank()) {
            errorMessage = "Please fill in all required fields correctly."
            return
        }

        viewModelScope.launch {
            try {
                isLoading = true
                val user = UserAccountDto(
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = phone,
                    email = email,
                    userPassword = password
                )
                val response = authApi.register(user)
                if (!response.token.isNullOrBlank()) {
                    onSuccess() // Після реєстрації перенаправляємо на логін
                } else {
                    errorMessage = response.error ?: "Unexpected error occurred"
                }
            } catch (e: Exception) {
                errorMessage = "Registration failed: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }
}
