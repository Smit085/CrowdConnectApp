import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.crowdconnectapp.components.convertLongToTime
import com.example.crowdconnectapp.components.customDatePicker

@Preview
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrganizeVotingScreen() {
    var isScheduleSessionEnabled by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(convertLongToTime(System.currentTimeMillis())) }
    var showDialog by remember { mutableStateOf(false) }
    var dateResult by remember { mutableStateOf("Date Picker") }

    Column {
        Switch(
            checked = isScheduleSessionEnabled, onCheckedChange = { isChecked ->
                isScheduleSessionEnabled = isChecked

            }, modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        )

        OutlinedTextField(value = selectedDate,
            onValueChange = {
                selectedDate = it
            },
            enabled = false,
            singleLine = true,
            readOnly = true,
            modifier = Modifier
                .clickable(isScheduleSessionEnabled) {
                    showDialog = isScheduleSessionEnabled // Show dialog when clicked
                }
                .padding(horizontal = 15.dp, vertical = 5.dp)
                .wrapContentWidth(),
            textStyle = TextStyle(color = if (isScheduleSessionEnabled) Color.Black else Color.Gray),
            label = { Text(text = "Date: ", style = TextStyle(color = if (isScheduleSessionEnabled) Color.Black else Color.Gray)) }
        )

        if (showDialog) {
            val result = customDatePicker(showDialog)
            showDialog = result.showDialog
            dateResult = result.dateResult
            selectedDate = result.selectedDate
        }
    }
}