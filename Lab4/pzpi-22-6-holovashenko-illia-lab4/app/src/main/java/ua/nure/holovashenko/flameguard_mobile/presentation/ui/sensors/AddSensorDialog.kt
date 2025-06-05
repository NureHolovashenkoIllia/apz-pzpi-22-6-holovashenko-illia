package ua.nure.holovashenko.flameguard_mobile.presentation.ui.sensors

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
import ua.nure.holovashenko.flameguard_mobile.presentation.viewmodel.sensors.SensorsViewModel

@Composable
fun AddSensorDialog(
    viewModel: SensorsViewModel,
    buildingId: Int,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                viewModel.createSensor(buildingId) { onDismiss() }
            }) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Add Sensor") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = viewModel.sensorName,
                    onValueChange = { viewModel.sensorName = it },
                    label = { Text("Sensor Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = viewModel.sensorType,
                    onValueChange = { viewModel.sensorType = it },
                    label = { Text("Sensor Type") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = viewModel.sensorStatus,
                    onValueChange = { viewModel.sensorStatus = it },
                    label = { Text("Sensor Status") },
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
