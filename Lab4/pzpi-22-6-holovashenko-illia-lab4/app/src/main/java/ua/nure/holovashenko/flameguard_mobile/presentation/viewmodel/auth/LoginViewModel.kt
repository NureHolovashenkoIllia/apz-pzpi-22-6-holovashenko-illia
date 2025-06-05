package ua.nure.holovashenko.flameguard_mobile.presentation.viewmodel.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ua.nure.holovashenko.flameguard_mobile.data.local.TokenDataStore
import ua.nure.holovashenko.flameguard_mobile.data.remote.AuthApi
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authApi: AuthApi,
    private val tokenDataStore: TokenDataStore
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun onLoginClick(onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Email and password must not be empty"
            return
        }

        viewModelScope.launch {
            try {
                isLoading = true
                val response = authApi.login(email, password)
                if (!response.token.isNullOrEmpty() && !response.id.isNullOrEmpty()) {
                    tokenDataStore.saveToken(response.token)
                    tokenDataStore.saveUserId(response.id)
                    onSuccess()
                } else {
                    errorMessage = response.error ?: "Unknown error"
                }
            } catch (e: Exception) {
                errorMessage = "Login failed: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }
}
