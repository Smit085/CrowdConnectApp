package com.example.crowdconnectapp.screens.attendee

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.crowdconnectapp.models.QuizViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun StartQuiz(navController: NavController, qrcode: String) {
    val quizViewModel: QuizViewModel = hiltViewModel()
    val isLoading by quizViewModel.isLoading.collectAsState()
    val title = quizViewModel.title
    val description = quizViewModel.description
    val selectedDate = quizViewModel.selectedDate
    val selectedTime = quizViewModel.selectedTime
    val timeoutIn = quizViewModel.timeoutIn
    val timeout = if (timeoutIn == "min") quizViewModel.timeout else quizViewModel.timeout * 60

    var isQuizOver by remember { mutableStateOf(false) }
    var isQuizNotStarted by remember { mutableStateOf(false) }
    var isQuizActive by remember { mutableStateOf(false) }
    var isattempted by remember { mutableStateOf(false) }

    LaunchedEffect(qrcode) {
        quizViewModel.fetchSessionData(qrcode)
    }

    LaunchedEffect(selectedDate, selectedTime) {
        if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val currentDate = Date()
            try {
                val quizStartDate = dateFormat.parse("$selectedDate $selectedTime")

                val calendar = Calendar.getInstance().apply {
                    time = quizStartDate!!
                    add(Calendar.MINUTE, timeout)
                }
                val quizEndDate = calendar.time

                // Update states based on current time
                isQuizOver = currentDate.after(quizEndDate)
                isQuizNotStarted = currentDate.before(quizStartDate)
                isQuizActive = !isQuizNotStarted && !isQuizOver
            } catch (e: Exception) {
                // Handle parsing errors
            }
        } else {
            // Handle missing date or time
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(topBar = {
            TopAppBar(title = { Text("Join Quiz") }, navigationIcon = {
                IconButton(onClick = { navController.navigate("attendeeScreen") }) {
                    Icon(
                        imageVector = Icons.Default.Close, contentDescription = "Close"
                    )
                }
            })
        }, content = { padding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Title: $title",
                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Row {
                            Text(
                                text = "Description: ", style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = description, style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = "Date: ",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = selectedDate, style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Row {
                            Text(
                                text = "Time: ",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = selectedTime, style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = { isattempted = true },
                                enabled = isQuizActive,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text(text = "Start Quiz", color = Color.White)
                            }
                            Button(
                                onClick = { /* TODO: Implement help action */ },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                            ) {
                                Text(text = "Need Help?", color = Color.White)
                            }
                        }
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isQuizOver || isQuizNotStarted) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer,
                        ),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 56.dp
                        ),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = when {
                                    isQuizOver -> Icons.Default.Warning
                                    isQuizNotStarted -> Icons.Default.Schedule
                                    else -> Icons.Default.CheckCircle
                                },
                                contentDescription = null,
                                tint = if (isQuizOver || isQuizNotStarted) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when {
                                    isQuizOver -> "Quiz timings are over."
                                    isQuizNotStarted -> "Quiz has not started yet."
                                    else -> "Quiz is live now."
                                },
                                color = if (isQuizOver || isQuizNotStarted) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        })
    }
    if(isattempted){
        AttendQuiz(navController)
    }
}
