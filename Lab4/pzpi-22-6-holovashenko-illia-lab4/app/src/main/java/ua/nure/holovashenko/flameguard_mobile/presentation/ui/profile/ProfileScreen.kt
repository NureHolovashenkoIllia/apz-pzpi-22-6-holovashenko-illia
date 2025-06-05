package ua.nure.holovashenko.flameguard_mobile.presentation.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import ua.nure.holovashenko.flameguard_mobile.presentation.viewmodel.profile.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    val user = viewModel.profile
    val isLoading = viewModel.isLoading
    val isUpdating = viewModel.isUpdating
    val error = viewModel.errorMessage
    val updated = viewModel.updateSuccess
    val isEditMode = viewModel.isEditMode
    var showSuccessMessage by remember { mutableStateOf(false) }

    LaunchedEffect(updated) {
        if (updated) {
            showSuccessMessage = true
            delay(3000)
            showSuccessMessage = false
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(24.dp)) {

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (user != null) {
            Card(
                modifier = Modifier.align(Alignment.TopCenter),
                elevation = CardDefaults.cardElevation(12.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("User Profile", style = MaterialTheme.typography.headlineMedium)

                    OutlinedTextField(
                        value = user.firstName,
                        onValueChange = { if (isEditMode) viewModel.profile = user.copy(firstName = it) },
                        label = { Text("First Name") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = isEditMode
                    )

                    OutlinedTextField(
                        value = user.lastName,
                        onValueChange = { if (isEditMode) viewModel.profile = user.copy(lastName = it) },
                        label = { Text("Last Name") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = isEditMode
                    )

                    OutlinedTextField(
                        value = user.phoneNumber ?: "",
                        onValueChange = { if (isEditMode) viewModel.profile = user.copy(phoneNumber = it) },
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = isEditMode
                    )

                    OutlinedTextField(
                        value = user.email,
                        onValueChange = {},
                        label = { Text("Email") },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (!error.isNullOrBlank()) {
                        Text(error, color = MaterialTheme.colorScheme.error)
                    }

                    if (showSuccessMessage) {
                        Text("Profile updated successfully!", color = MaterialTheme.colorScheme.primary)
                    }

                    if (isEditMode) {
                        Button(
                            onClick = { viewModel.updateProfile() },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isUpdating
                        ) {
                            if (isUpdating) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else {
                                Text("Save Changes")
                            }
                        }
                    } else {
                        Button(
                            onClick = { viewModel.toggleEditMode() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Edit Profile")
                        }
                    }
                }
            }
        } else {
            Text("No user data found.", modifier = Modifier.align(Alignment.Center))
        }
    }
}
