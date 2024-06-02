package com.example.crowdconnectapp.screens.host.quiz

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.crowdconnectapp.models.Question
import com.example.crowdconnectapp.models.QuizViewModel
import com.example.crowdconnectapp.ui.theme.Blue
import com.example.crowdconnectapp.ui.theme.VividBlue

@Composable
fun ManageQuestions(quizViewModel: QuizViewModel) {
    val questions by quizViewModel.questions.collectAsState()

    if (questions.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Questions not created yet\n     add a question first.",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp).align(Alignment.Center)
            )
        }
    } else {
        ManageQuestionsScreen(questions = questions, onDeleteQuestion = quizViewModel::deleteQuestion)
    }
}

@Composable
fun ManageQuestionsScreen(
    questions: List<Question>,
    onDeleteQuestion: (Question) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Questions",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        if (questions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "No questions available",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(questions) { question ->
                    val questionNumber = questions.indexOf(question) + 1
                    QuestionItem(
                        question = question,
                        questionNumber = questionNumber,
                        onDeleteClick = { onDeleteQuestion(question) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

            }
        }
    }
}

@Composable
fun QuestionItem(
    question: Question,
    questionNumber: Int,
    onDeleteClick: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Blue,
        ),
        border = BorderStroke(1.dp, VividBlue),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "$questionNumber. ${question.question}",
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Options:", style = MaterialTheme.typography.labelMedium
            )
            question.options.forEachIndexed { index, option ->
                Text(
                    text = "${index + 1}. $option", style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = "Correct Answer: ${question.options[question.correctAnswerIndex]}",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete this question?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        onDeleteClick()
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
