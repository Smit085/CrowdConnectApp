package com.example.crowdconnectapp.screens.quiz

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
fun ManageQuestions() {

    val quizViewModel: QuizViewModel = hiltViewModel()
    val questions by quizViewModel.questions.collectAsState()

    val onEditQuestion: (Question) -> Unit = {}
    val onDeleteQuestion: (Question) -> Unit = {}

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
        ManageQuestionsScreen(questions = questions, onEditQuestion = onEditQuestion, onDeleteQuestion = onDeleteQuestion)
    }
}

@Composable
fun ManageQuestionsScreen(
    questions: List<Question>,
    onEditQuestion: (Question) -> Unit,
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
                    QuestionItem(
                        question = question,
                        onEditClick = { onEditQuestion(question) },
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
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEditClick() },
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
                    text = question.text,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Row(
                    modifier = Modifier.weight(0.3f)
                ) {
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color.Blue
                        )
                    }
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
}
