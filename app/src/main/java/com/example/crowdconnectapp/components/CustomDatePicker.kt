package com.example.crowdconnectapp.components

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.text.SimpleDateFormat
import java.util.Date


data class DateResult(
    val showDialog: Boolean,
    val selectedDate: String
)

@ExperimentalMaterial3Api
@ExperimentalLayoutApi
@Composable
fun customDatePicker(showDialog: Boolean): DateResult {
    var showDialog by remember { mutableStateOf(showDialog) }
    val datePickerState = rememberDatePickerState()
    var selectedDate by remember { mutableStateOf(convertLongToTime(System.currentTimeMillis())) }

    if (showDialog) {
        DatePickerDialog(onDismissRequest = {
            showDialog = false
        }, confirmButton = {
            TextButton(
                onClick = {
                    showDialog = false
                    if (datePickerState.selectedDateMillis != null) {
                        selectedDate = convertLongToTime(datePickerState.selectedDateMillis!!)
                    }
                }, enabled = true
            ) {
                Text("OK")
            }
        }, dismissButton = {
            TextButton(onClick = {
                showDialog = false
            }) {
                Text("Cancel")
            }
        }) {
            DatePicker(state = datePickerState)
        }
    }
    return DateResult(showDialog, selectedDate)
}

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("dd/MM/yyyy")
    return format.format(date)
}

