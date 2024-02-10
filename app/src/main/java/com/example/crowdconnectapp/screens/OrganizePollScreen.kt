package com.example.crowdconnectapp.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganizePollScreen(navController: NavHostController) {
    var quizTitle by remember { mutableStateOf(TextFieldValue()) }
    var description by remember { mutableStateOf(TextFieldValue()) }
    var duration by remember { mutableStateOf(0f) }
    var numberOfQuestions by remember { mutableStateOf(0f) }

    val buttonAlpha by animateFloatAsState(
        targetValue = if (quizTitle.text.isNotEmpty() && description.text.isNotEmpty()) 1f else 0.5f,
        animationSpec = spring(), label = ""
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Organize Quiz") }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = quizTitle,
                onValueChange = { quizTitle = it },
                label = { Text("Quiz Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            BasicTextField(
                value = description.text,
                onValueChange = { description = TextFieldValue(it) },
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true,
                maxLines = 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clip(shape = MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )
            Slider(
                value = duration,
                onValueChange = { duration = it },
                valueRange = 0f..100f,
                steps = 10,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            Slider(
                value = numberOfQuestions,
                onValueChange = { numberOfQuestions = it },
                valueRange = 0f..100f,
                steps = 10,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            Button(
                onClick = { /* TODO: Add Quiz */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(buttonAlpha)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Text("Start Quiz", style = MaterialTheme.typography.bodyMedium, color = Color.White)
            }
        }
    }
}
