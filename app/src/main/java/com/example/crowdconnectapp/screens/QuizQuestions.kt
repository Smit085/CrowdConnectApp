import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun QuestionScreen() {
    var question by remember { mutableStateOf("") }
    var options by remember { mutableStateOf(listOf("")) }
    var correctAnswerIndex by remember { mutableIntStateOf(-1) }
    var isDeleteDialogVisible by remember { mutableStateOf(false) }
    var optionToDelete by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxSize()
    ) {
        // TextField for the question
        OutlinedTextField(
            value = question,
            onValueChange = { question = it },
            label = { Text("Question") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Display options
        Text("Options", style = MaterialTheme.typography.titleSmall)
        options.forEachIndexed { index, option ->
            OptionItem(
                option = option,
                onOptionChange = { newOption ->
                    if (index < options.size) {
                        options = options.toMutableList().apply { set(index, newOption) }
                    }
                },
                onOptionDelete = {
                    optionToDelete = option
                    isDeleteDialogVisible = true
                },
                isChecked = correctAnswerIndex == index,
                onCheckboxChange = { isChecked ->
                    if (isChecked) {
                        correctAnswerIndex = index
                    } else if (correctAnswerIndex == index) {
                        correctAnswerIndex = -1
                    }
                }
            )
        }

        // Button to add a new option
        IconButton(onClick = {
            if (options.last().isNotBlank()) {
                options = options.toMutableList().apply { add("") }
            }
        }) {
            Icon(Icons.Default.Add, contentDescription = "Add Option")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Button to save the question
        Button(
            onClick = {
                if (question.isNotBlank() && options.size >= 2 && correctAnswerIndex != -1) {
                    // Perform save action here
                }
            },
            enabled = question.isNotBlank() && options.size >= 2 && correctAnswerIndex != -1
        ) {
            Text("Save")
        }

        // Delete option dialog
        if (isDeleteDialogVisible) {
            DeleteOptionDialog(
                option = optionToDelete,
                onDeleteConfirmed = {
                    val indexToDelete = options.indexOf(optionToDelete)
                    if (options.size == 1) {
                        options = listOf("")
                    } else if (indexToDelete >= 0 && indexToDelete < options.size) {
                        options = options.filterIndexed { index, _ -> index != indexToDelete }
                        if (correctAnswerIndex == indexToDelete) {
                            correctAnswerIndex = -1
                        } else if (correctAnswerIndex > indexToDelete) {
                            correctAnswerIndex -= 1
                        }
                    }
                    isDeleteDialogVisible = false
                    optionToDelete = ""
                },
                onDismiss = {
                    isDeleteDialogVisible = false
                    optionToDelete = ""
                }
            )
        }
    }
}

@Composable
fun OptionItem(
    option: String,
    onOptionChange: (String) -> Unit,
    onOptionDelete: () -> Unit,
    isChecked: Boolean,
    onCheckboxChange: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckboxChange
        )
        OutlinedTextField(
            value = option,
            onValueChange = onOptionChange,
            label = { Text("Option") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { state ->
                    if (!state.isFocused) {
                        onOptionChange(option.trim())
                    }
                }
        )
        IconButton(onClick = onOptionDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Option")
        }
    }
}

@Composable
fun DeleteOptionDialog(
    option: String,
    onDeleteConfirmed: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Option") },
        text = { Text("Are you sure you want to delete option \"$option\"?") },
        confirmButton = {
            Button(onClick = onDeleteConfirmed) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}





