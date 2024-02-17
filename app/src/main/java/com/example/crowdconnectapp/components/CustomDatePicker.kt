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


data class Result(
    val showDialog: Boolean,
    val dateResult: String,
    val selectedDate: String

)

@ExperimentalMaterial3Api
@ExperimentalLayoutApi
@Composable
fun customDatePicker(showDialog: Boolean): Result {

    var showDialog by remember { mutableStateOf(showDialog) }
    var dateResult by remember { mutableStateOf("Date Picker") }
    val datePickerState = rememberDatePickerState()
    var selectedDate by remember { mutableStateOf(convertLongToTime(System.currentTimeMillis())) }

    if (showDialog) {
        DatePickerDialog(onDismissRequest = {
            showDialog = false
        }, confirmButton = {
            TextButton(
                onClick = {
                    showDialog = false
                    var date = "no selection"
                    if (datePickerState.selectedDateMillis != null) {
                        date = convertLongToTime(datePickerState.selectedDateMillis!!)
                        selectedDate = date
                    }
                    dateResult = date
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
    return Result(showDialog,dateResult,selectedDate)
}

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("dd/MM/yyyy")
    return format.format(date)
}

