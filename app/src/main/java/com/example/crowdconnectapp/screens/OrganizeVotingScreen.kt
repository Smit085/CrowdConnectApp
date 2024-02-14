import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date

@Preview
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrganizeVotingScreen() {
    var isScheduleSessionEnabled by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var dateResult by remember { mutableStateOf("Date Picker") }
    val datePickerState = rememberDatePickerState()
    val confirmEnabled by remember { derivedStateOf { true } }

    Column {
        Switch(
            checked = isScheduleSessionEnabled,
            onCheckedChange = { isChecked ->
                isScheduleSessionEnabled = isChecked
                // Clear the selected date if schedule session is disabled
                if (!isChecked) selectedDate = ""
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )

        BasicTextField(
            value = TextFieldValue(selectedDate),
            onValueChange = {
                selectedDate = it.text
            },
            enabled = isScheduleSessionEnabled,
            singleLine = true,
            modifier = Modifier
                .clickable {
                    showDialog = true // Show dialog when clicked
                }
                .padding(vertical = 8.dp, horizontal = 16.dp),
            textStyle = TextStyle(color = if (selectedDate.isEmpty()) Color.Gray else Color.Black)
        )

        if (showDialog) {
            DatePickerDialog(
                onDismissRequest = {
                    showDialog = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            var date = "no selection"
                            if (datePickerState.selectedDateMillis != null) {
                                date = convertLongToTime(datePickerState.selectedDateMillis!!)
                                selectedDate = date
                            }
                            dateResult = date
                        },
                        enabled = confirmEnabled
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("dd MM yyyy")
    return format.format(date)
}
