package com.example.crowdconnectapp.screens.quiz

import android.widget.NumberPicker
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.crowdconnectapp.components.LabeledCheckbox
import com.example.crowdconnectapp.components.LabeledSwitch
import com.example.crowdconnectapp.components.Picker
import com.example.crowdconnectapp.components.customDatePicker
import com.example.crowdconnectapp.components.customTimePicker
import com.example.crowdconnectapp.models.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ConfigureQuiz() {
    val quizViewModel: QuizViewModel = hiltViewModel()
    var title by remember { mutableStateOf(quizViewModel.title) }
    var description by remember { mutableStateOf(quizViewModel.description) }
    var isScheduleEnabled by remember { mutableStateOf(quizViewModel.isScheduleEnabled) }
    var isDurationEnabled by remember { mutableStateOf(quizViewModel.isDurationEnabled) }
    var isTimeoutEnabled by remember { mutableStateOf(quizViewModel.isTimeoutEnabled) }
    var isShuffleQuestionsEnabled by remember { mutableStateOf(quizViewModel.isShuffleQuestionsEnabled) }
    var isShuffleOptionsEnabled by remember { mutableStateOf(quizViewModel.isShuffleOptionsEnabled) }
    var isKioskEnabled by remember { mutableStateOf(quizViewModel.isKioskEnabled) }
    var isEvaluateEnabled by remember { mutableStateOf(quizViewModel.isEvaluateEnabled) }

    var showDateDialog by remember { mutableStateOf(false) }
    var showTimeDialog by remember { mutableStateOf(false) }

    var selectedDate by remember { mutableStateOf(quizViewModel.selectedDate) }
    var selectedTime by remember { mutableStateOf(quizViewModel.selectedTime) }
    var duration by remember { mutableIntStateOf(quizViewModel.duration) }
    var timeout by remember { mutableIntStateOf(quizViewModel.timeout) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .verticalScroll(scrollState)
    ) {

        TextField(value = title, onValueChange = {
            title = it
            quizViewModel.title = it
        }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth(), maxLines = 2
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = description,
            onValueChange = {
                description = it
                quizViewModel.description = it
            },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4,
            maxLines = 5
        )

        LabeledSwitch(text = "Schedule Session",
            isChecked = isScheduleEnabled,
            onCheckedChange = { isChecked ->
                isScheduleEnabled = isChecked
                quizViewModel.isScheduleEnabled = isChecked
            })
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
                quizViewModel.selectedDate = result.selectedDate
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
                quizViewModel.selectedTime = selectedTime
            }
        }
        LabeledSwitch(text = "Duration/Question",
            isChecked = isDurationEnabled,
            onCheckedChange = { isChecked ->
                isDurationEnabled = isChecked
                quizViewModel.isDurationEnabled = isChecked
            })
        if (isDurationEnabled) {
            NumberPicker(quizViewModel.duration, listOf("", "sec", "min", ""), onNumberChanged = {
                duration = it
                quizViewModel.duration = it
            })
        }
        LabeledSwitch(text = "Session Timeout",
            isChecked = isTimeoutEnabled,
            onCheckedChange = { isChecked ->
                isTimeoutEnabled = isChecked
                quizViewModel.isTimeoutEnabled = isChecked
            })
        if (isTimeoutEnabled) {
            NumberPicker(
                quizViewModel.timeout,
                listOf("", "sec", "min", "hrs", ""),
                onNumberChanged = {
                    timeout = it
                    quizViewModel.timeout = it
                })
        }
        LabeledCheckbox(text = "Shuffle Questions",
            isChecked = isShuffleQuestionsEnabled,
            onCheckedChange = { isChecked ->
                isShuffleQuestionsEnabled = isChecked
                quizViewModel.isShuffleQuestionsEnabled = isChecked
            })
        LabeledCheckbox(text = "Shuffle Options",
            isChecked = isShuffleOptionsEnabled,
            onCheckedChange = { isChecked ->
                isShuffleOptionsEnabled = isChecked
                quizViewModel.isShuffleOptionsEnabled = isChecked
            })
        LabeledCheckbox(text = "Evaluate",
            isChecked = isEvaluateEnabled,
            onCheckedChange = { isChecked ->
                isEvaluateEnabled = isChecked
                quizViewModel.isEvaluateEnabled = isChecked
            })
        LabeledCheckbox(text = "kiosk mode",
            isChecked = isKioskEnabled,
            onCheckedChange = { isChecked ->
                isKioskEnabled = isChecked
                quizViewModel.isKioskEnabled = isChecked
            })
    }
}

@Composable
fun NumberPicker(number: Int, units: List<String>, onNumberChanged: (Int) -> Unit) {
    var number by remember { mutableIntStateOf(number) }

    Column(modifier = Modifier) {
        Row(
            modifier = Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    number = if (number == 0) 0 else number - 10
                    onNumberChanged(number)
                }, modifier = Modifier.width(40.dp)
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
            IconButton(
                onClick = {
                    number += 10
                    onNumberChanged(number)
                }, modifier = Modifier.width(40.dp)
            ) {
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


