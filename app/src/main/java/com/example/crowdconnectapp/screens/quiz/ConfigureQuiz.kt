package com.example.crowdconnectapp.screens.quiz

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crowdconnectapp.components.Picker
import com.example.crowdconnectapp.components.rememberPickerState
import androidx.compose.ui.text.TextStyle
import com.example.crowdconnectapp.components.LabeledCheckbox
import com.example.crowdconnectapp.components.customDatePicker
import com.example.crowdconnectapp.components.customTimePicker

@Composable
fun ConfigureQuiz() {
    App()
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun App() {
    var isScheduleEnabled by remember { mutableStateOf(false) }
    var isDurationEnabled by remember { mutableStateOf(false) }
    var isTimeoutEnabled by remember { mutableStateOf(false) }
    var isShuffleQuestionsEnabled by remember { mutableStateOf(false) }
    var isShuffleOptionsEnabled by remember { mutableStateOf(false) }
    var isEvaluateEnabled by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var showDateDialog by remember { mutableStateOf(false) }
    var showTimeDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }


        OutlinedTextField(value = title, onValueChange = { text ->
            title = text
        }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth(), maxLines = 1
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = description,
            onValueChange = { text ->
                description = text
            },
            label = { Text("Description") },
            minLines = 4,
            maxLines = 5,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Schedule Session")
            Switch(modifier = Modifier.scale(0.8f),
                checked = isScheduleEnabled,
                onCheckedChange = { isChecked ->
                    isScheduleEnabled = isChecked
                },
                thumbContent = if (isScheduleEnabled) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    null
                })
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(value = selectedDate,
                onValueChange = {
                    selectedDate = it
                },
                enabled = false,
                singleLine = true,
                readOnly = true,
                modifier = Modifier
                    .clickable(isScheduleEnabled) {
                        showDateDialog = isScheduleEnabled
                    }
                    .padding(horizontal = 15.dp, vertical = 5.dp)
                    .width(150.dp),
                textStyle = TextStyle(color = if (isScheduleEnabled) Color.Black else Color.Gray),
                label = {
                    Text(
                        text = "Date: ",
                        style = TextStyle(color = if (isScheduleEnabled) Color.Black else Color.Gray)
                    )
                })
            if (showDateDialog) {
                val result = customDatePicker(showDateDialog)
                showDateDialog = result.showDialog
                selectedDate = result.selectedDate
            }
            OutlinedTextField(value = selectedTime,
                onValueChange = {
                    selectedTime = it
                },
                enabled = false,
                singleLine = true,
                readOnly = true,
                modifier = Modifier
                    .clickable(isScheduleEnabled) {
                        showTimeDialog = isScheduleEnabled // Show dialog when clicked
                    }
                    .padding(horizontal = 15.dp, vertical = 5.dp)
                    .width(150.dp),
                textStyle = TextStyle(color = if (isScheduleEnabled) Color.Black else Color.Gray),
                label = {
                    Text(
                        text = "Time: ",
                        style = TextStyle(color = if (isScheduleEnabled) Color.Black else Color.Gray)
                    )
                })

            if (showTimeDialog) {
                val result = customTimePicker(showTimeDialog)
                showTimeDialog = result.showDialog
                selectedTime = "${
                    result.timeState.hour.toString().padStart(2, '0')
                }:${result.timeState.minute.toString().padStart(2, '0')}"
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Duration/Question")
            Switch(modifier = Modifier.scale(0.8f),
                checked = isDurationEnabled,
                onCheckedChange = { isChecked ->
                    isDurationEnabled = isChecked
                },
                thumbContent = if (isDurationEnabled) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    null
                })
        }
        NumberPicker(listOf("", "sec", "min", ""),isDurationEnabled)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Session Timeout")
            Switch(modifier = Modifier.scale(0.8f),
                checked = isTimeoutEnabled,
                onCheckedChange = { isChecked ->
                    isTimeoutEnabled = isChecked
                },
                thumbContent = if (isTimeoutEnabled) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    null
                })
        }
        NumberPicker(listOf("", "sec", "min", "hrs", ""),isTimeoutEnabled)

        LabeledCheckbox(text = "Shuffle Questions", isChecked = isShuffleQuestionsEnabled, onCheckedChange = {isShuffleQuestionsEnabled = !isShuffleQuestionsEnabled})
        LabeledCheckbox(text = "Shuffle Options", isChecked = isShuffleOptionsEnabled, onCheckedChange = {isShuffleOptionsEnabled = !isShuffleOptionsEnabled})
        LabeledCheckbox(text = "Evaluate", isChecked = isEvaluateEnabled, onCheckedChange = {isEvaluateEnabled = !isEvaluateEnabled})
    }
}

@Composable
fun NumberPicker(units: List<String>, enable: Boolean) {
    if (enable) {
        var number by remember { mutableIntStateOf(0) }
        Column(modifier = Modifier) {
            Row(
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { number = if (number == 0) 0 else number - 10 },
                    modifier = Modifier.width(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Minus",
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .size(25.dp),
                        tint = Color.Black
                    )
                }
                Text(text = "$number")
                IconButton(onClick = {
                    number += 10
                }, modifier = Modifier.width(40.dp)) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Add",
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .size(25.dp),
                        tint = Color.Black
                    )
                }
                Picker(units)
            }
        }
    }
}

@Composable
fun Picker(units: List<String>) {
    val unitsPickerState = rememberPickerState()
    Picker(
        state = unitsPickerState,
        items = units,
        visibleItemsCount = 3,
        modifier = Modifier.wrapContentSize(),
        textModifier = Modifier.padding(8.dp),
        textStyle = TextStyle(fontSize = 18.sp)
    )
}
