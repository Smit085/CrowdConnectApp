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
    var timeProgress by remember { mutableStateOf(0.1f) }
    var remainingTime by remember { mutableStateOf(10 * 60L) }
    var selectedOption by remember { mutableStateOf("") }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    val questions by quizViewModel.questions.collectAsState()
    val isLoading by quizViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        while (timeProgress < 1f) {
            delay(1500L)
            timeProgress += 0.1f
        }
    }

    LaunchedEffect(Unit) {
        while (remainingTime > 0) {
            delay(1000L)
            remainingTime -= 1
        }
    }

    val minutes = remainingTime / 60
    val seconds = remainingTime % 60

    if (remainingTime <= 0) {
        // Navigate to some result screen or show a dialog
    }

    val currentQuestion = questions.getOrNull(currentQuestionIndex)

    fun nextQuestion() {
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex += 1
            selectedOption = ""
        }
    }

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
//                    Text(
//                        text = "Timeout: $minutes:${"%02d".format(seconds)}",
//                        modifier = Modifier.padding(end = 16.dp),
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 14.sp
//                    )
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
                Text(
                    modifier = Modifier.padding(21.dp),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Justify,
                    text = "Loading questions...",
                    style = MaterialTheme.typography.titleMedium
                )
            } else {
                currentQuestion?.let { question ->
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
                            IconButton(onClick = { if (currentQuestionIndex > 0) currentQuestionIndex -= 1 }) {
                                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Previous")
                            }
                            IconButton(onClick = { nextQuestion() }) {
                                Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Next")
                            }
                        }
                    }
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 24.dp),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        LinearProgressIndicator(
                            progress = timeProgress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(7.dp),
                            color = Color.Green,
                        )
                    }



                    Text(
                        modifier = Modifier.padding(21.dp),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Justify,
                        text = question.text,
                        style = MaterialTheme.typography.titleMedium
                    )

                    question.options.forEach { option ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .padding(horizontal = 20.dp, vertical = 5.dp)
                                .clickable { selectedOption = option },
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedOption == option) Color.Green else Color.White
                            ),
                            shape = RoundedCornerShape(corner = CornerSize(7.dp)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.padding(start = 7.dp),
                                    text = option,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Justify
                                )
                                RadioButton(
                                    selected = selectedOption == option,
                                    onClick = { selectedOption = option }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(70.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(17.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(57.dp),
                            shape = RoundedCornerShape(45.dp),
                            onClick = { nextQuestion() }
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
}
