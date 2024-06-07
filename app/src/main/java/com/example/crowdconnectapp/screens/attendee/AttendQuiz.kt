package com.example.crowdconnectapp.screens.attendee

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.crowdconnectapp.data.submitResponses
import com.example.crowdconnectapp.models.QuizViewModel
import com.example.crowdconnectapp.models.UserResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@ExperimentalComposeUiApi
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AttendQuiz(navController: NavController) {
    val quizViewModel: QuizViewModel = hiltViewModel()
    val questions by quizViewModel.questions.collectAsState()
    val isLoading by quizViewModel.isLoading.collectAsState()
    val isShuffleQuestionsEnabled = quizViewModel.isShuffleQuestionsEnabled
    val isShuffleOptionsEnabled = quizViewModel.isShuffleOptionsEnabled
    val duration = quizViewModel.duration
    val durationIn = quizViewModel.durationIn
    val isDurationEnabled = quizViewModel.isDurationEnabled
    val context = LocalContext.current

    Log.i("AttendQuiz",questions.toString())
    val totalTime = when (durationIn) {
        "sec" -> duration * 1L
        "min" -> duration * 60L
        "hrs" -> duration * 60L * 60L
        else -> duration * 60L
    }

    var remainingTime by remember { mutableStateOf(totalTime) }
    var timeProgress by remember { mutableStateOf(0f) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf("") }
    var userResponses by remember { mutableStateOf(mutableListOf<UserResponse>()) }

    // Initialize the shuffled questions and options
    val shuffledQuestions =
        remember(questions, isShuffleQuestionsEnabled, isShuffleOptionsEnabled) {
            if (isShuffleQuestionsEnabled) {
                questions.shuffled().map { question ->
                    question to (if (isShuffleOptionsEnabled) question.options.shuffled() else question.options)
                }
            } else {
                questions.map { question ->
                    question to (if (isShuffleOptionsEnabled) question.options.shuffled() else question.options)
                }
            }
        }

    val currentQuestionPair = shuffledQuestions.getOrNull(currentQuestionIndex)
    val currentQuestion = currentQuestionPair?.first
    val currentOptions = currentQuestionPair?.second ?: emptyList()

    fun saveAnswer() {
        val question = currentQuestion ?: return
        val selectedOptionIndex = currentOptions.indexOf(selectedOption)
        if (selectedOptionIndex != -1) {
            val response = UserResponse(currentQuestionIndex, selectedOptionIndex)
            userResponses.removeAll { it.questionIndex == currentQuestionIndex }
            userResponses.add(response)
        }
    }

    fun loadSelectedOption() {
        val response = userResponses.find { it.questionIndex == currentQuestionIndex }
        selectedOption = response?.let { currentOptions.getOrNull(it.selectedOptionIndex) } ?: ""
    }

    fun nextQuestion() {
        saveAnswer()
        if (currentQuestionIndex < shuffledQuestions.size - 1) {
            currentQuestionIndex += 1
            timeProgress = 0f
            remainingTime = totalTime
            loadSelectedOption()
        }
    }

    fun previousQuestion() {
        saveAnswer()
        if (currentQuestionIndex > 0) {
            currentQuestionIndex -= 1
            timeProgress = 0f
            remainingTime = totalTime
            loadSelectedOption()
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

    LaunchedEffect(currentQuestionIndex) {
        loadSelectedOption()
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
                    IconButton(onClick = {
                        navController.navigate("attendeeScreen") {
                            navController.popBackStack()
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "close")
                    }
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
                            modifier = Modifier.padding(horizontal = 20.dp)
                        ) {
                            LinearProgressIndicator(
                                progress = timeProgress,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(7.dp),
                                color = Color(0xFF019B00)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    } else {
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
                            text = "Question ${currentQuestionIndex + 1}/${shuffledQuestions.size}",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Row {
                            IconButton(
                                onClick = { previousQuestion() },
                                enabled = currentQuestionIndex > 0
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowLeft,
                                    contentDescription = "Previous"
                                )
                            }
                            IconButton(
                                onClick = { nextQuestion() },
                                enabled = currentQuestionIndex < shuffledQuestions.size - 1
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
                        text = question.question,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Justify
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        currentOptions.forEachIndexed { index, option ->
                            val optionLetter = ('A' + index).toString()
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp)
                                    .clickable {
                                        selectedOption = option
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
                                        color = Color.Black,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

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
                                if (currentQuestionIndex == shuffledQuestions.size - 1) {
                                    saveAnswer()
                                    if (userResponses.size == shuffledQuestions.size) {
                                        submitResponses(
                                            responses = userResponses,
                                            sessionId = quizViewModel.sessioncode,
                                            onSuccess = {
                                                Toast.makeText(
                                                    context,
                                                    "Your response has been submitted.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                navController.navigate("attendeeScreen")
                                            },
                                            onFailure = { exception ->
                                                Log.e(
                                                    "SubmitError",
                                                    "Error submitting responses",
                                                    exception
                                                )
                                            }
                                        )
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Please answer all questions before submitting.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    nextQuestion()
                                }
                            },
                            enabled = selectedOption.isNotBlank()
                        ) {
                            Text(
                                text = if (currentQuestionIndex == shuffledQuestions.size - 1) "Submit" else "Continue",
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
            fontWeight = FontWeight.Bold
        )
    }
}
