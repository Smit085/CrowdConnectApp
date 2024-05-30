package com.example.crowdconnectapp.screens.attendee

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
    val duration = quizViewModel.duration
    val durationIn = quizViewModel.durationIn
    val isDurationEnabled = quizViewModel.isDurationEnabled

    val totalTime = when (durationIn) {
        "sec" -> duration * 1L
        "min" -> duration * 60L
        "hrs" -> duration * 60L * 60L
        else -> duration * 60L
    }

    var remainingTime by remember { mutableStateOf(totalTime) }
    var timeProgress by remember { mutableStateOf(0f) }
    var selectedOption by remember { mutableStateOf("") }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var answeredQuestions by remember { mutableStateOf(setOf<Int>()) }

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
    val allQuestionsAnswered = answeredQuestions.size == questions.size

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
                    // If needed, add additional actions here
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
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
                    if (isDurationEnabled) {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                        ) {
                            LinearProgressIndicator(
                                progress = timeProgress,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(7.dp),
                                color = Color(0xFF019B00),
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    else{
                        Spacer(modifier = Modifier.height(8.dp))
                    }
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
                            IconButton(
                                onClick = { if (currentQuestionIndex > 0) currentQuestionIndex -= 1 },
                                enabled = !isDurationEnabled || (isDurationEnabled && selectedOption.isNotEmpty() && !answeredQuestions.contains(currentQuestionIndex))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowLeft,
                                    contentDescription = "Previous"
                                )
                            }
                            IconButton(
                                onClick = { if (!isDurationEnabled || selectedOption.isNotEmpty()) nextQuestion() },
                                enabled = !isDurationEnabled || selectedOption.isNotEmpty()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = "Next"
                                )
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
                        question.options.forEachIndexed { index, option ->
                            val optionLetter = ('A' + index).toString()
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp)
                                    .clickable {
                                        if (!answeredQuestions.contains(currentQuestionIndex)) {
                                            selectedOption = option
                                            answeredQuestions = answeredQuestions + currentQuestionIndex
                                        }
                                    }
                                    .border(
                                        width = 1.dp,
                                        color = if (selectedOption == option) Color.Green else MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(4.dp)
                                    ),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Transparent
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OptionIndicator(
                                        indicator = optionLetter,
                                        isSelected = selectedOption == option
                                    )
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Text(
                                        text = option,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,
                                        textAlign = TextAlign.Justify,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            shape = RoundedCornerShape(5.dp),
                            modifier = Modifier.width(350.dp),
                            onClick = {
                                if (currentQuestionIndex == questions.size - 1) {
                                    // Submit action here
                                } else {
                                    nextQuestion()
                                }
                            },
                            enabled = !isDurationEnabled || selectedOption.isNotEmpty()
                        ) {
                            Text(
                                text = if (currentQuestionIndex == questions.size - 1) "Submit" else "Continue",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OptionIndicator(indicator: String, isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .background(
                color = if (isSelected) Color(0xFF019B00) else MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = indicator,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}
