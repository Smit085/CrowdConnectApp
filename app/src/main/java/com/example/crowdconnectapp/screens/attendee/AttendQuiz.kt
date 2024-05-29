package com.example.crowdconnectapp.screens.attendee

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.crowdconnectapp.models.QuizViewModel
import kotlinx.coroutines.delay

@ExperimentalComposeUiApi
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AttendQuiz(navController: NavController) {
    val quizViewModel: QuizViewModel = hiltViewModel()
    val questions by quizViewModel.questions.collectAsState()
    val isLoading by quizViewModel.isLoading.collectAsState()

    // Assume these values are fetched from the database
    val duration = 20
    val durationIn = "sec"
    val isDurationEnabled = true

    val totalTime = when (durationIn) {
        "sec" -> duration * 1L
        "min" -> duration * 60L
        "hour" -> duration * 60L * 60L
        else -> duration * 60L
    }

    var remainingTime by remember { mutableStateOf(totalTime) }
    var timeProgress by remember { mutableStateOf(0f) }
    var selectedOption by remember { mutableStateOf("") }
    var currentQuestionIndex by remember { mutableStateOf(0) }

    // Function to handle moving to the next question
    fun nextQuestion() {
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex += 1
            selectedOption = ""
            timeProgress = 0f
            remainingTime = totalTime
        }
    }

    LaunchedEffect(isDurationEnabled, currentQuestionIndex) {
        if (isDurationEnabled) {
            timeProgress = 0f
            remainingTime = totalTime
            while (remainingTime > 0) {
                delay(1000L)
                remainingTime -= 1
                timeProgress = (totalTime - remainingTime).toFloat() / totalTime
                if (remainingTime <= 0) {
                    nextQuestion()
                }
            }
        }
    }

    val currentQuestion = questions.getOrNull(currentQuestionIndex)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Quiz", textAlign = TextAlign.Center) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("startQuiz") }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "close")
                    }
                },
                actions = {
                    if (isDurationEnabled) {
                        Text(
                            text = "Timeout: ${remainingTime / 60}:${"%02d".format(remainingTime % 60)}",
                            modifier = Modifier.padding(end = 16.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Loading questions...",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                currentQuestion?.let { question ->
                    LinearProgressIndicator(
                        progress = timeProgress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(7.dp)
                            .padding(horizontal = 20.dp),
                        color = Color.Green
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Question ${currentQuestionIndex + 1}/${questions.size}",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Row {
                            if (!isDurationEnabled || selectedOption.isNotEmpty()) {
                                IconButton(onClick = { if (currentQuestionIndex > 0) currentQuestionIndex -= 1 }) {
                                    Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Previous")
                                }
                            }
                            IconButton(
                                onClick = { if (!isDurationEnabled || selectedOption.isNotEmpty()) nextQuestion() },
                                enabled = !isDurationEnabled || selectedOption.isNotEmpty()
                            ) {
                                Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Next")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        modifier = Modifier
                            .padding(horizontal = 21.dp)
                            .fillMaxWidth(),
                        text = question.text,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Justify
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        question.options.forEach { option ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp)
                                    .clickable { selectedOption = option },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedOption == option) Color.Green else Color.White
                                ),
                                shape = RoundedCornerShape(corner = CornerSize(7.dp)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = option,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Start
                                    )
                                    RadioButton(
                                        selected = selectedOption == option,
                                        onClick = { selectedOption = option }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(57.dp)
                            .padding(horizontal = 20.dp),
                        shape = RoundedCornerShape(45.dp),
                        onClick = { nextQuestion() },
                        enabled = !isDurationEnabled || selectedOption.isNotEmpty()
                    ) {
                        Text(
                            text = "Continue",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
