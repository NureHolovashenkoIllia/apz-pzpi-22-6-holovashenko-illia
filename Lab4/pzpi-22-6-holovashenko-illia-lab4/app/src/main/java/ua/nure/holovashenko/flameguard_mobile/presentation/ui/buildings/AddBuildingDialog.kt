package ua.nure.holovashenko.flameguard_mobile.presentation.ui.buildings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.nure.holovashenko.flameguard_mobile.presentation.viewmodel.buildings.BuildingsViewModel

@Composable
fun AddBuildingDialog(
    viewModel: BuildingsViewModel,
    userId: Int,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                viewModel.createBuilding(userId) { onDismiss() }
            }) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("New Building") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = viewModel.name,
                    onValueChange = { viewModel.name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = viewModel.description,
                    onValueChange = { viewModel.description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = viewModel.type,
                    onValueChange = { viewModel.type = it },
                    label = { Text("Type") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = viewModel.condition,
                    onValueChange = { viewModel.condition = it },
                    label = { Text("Condition") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (!viewModel.error.isNullOrBlank()) {
                    Text(
                        text = viewModel.error ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}
