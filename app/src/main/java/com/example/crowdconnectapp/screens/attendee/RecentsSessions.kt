package com.example.crowdconnectapp.screens.attendee

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.crowdconnectapp.data.fetchAttendedSessions
import com.example.crowdconnectapp.data.fetchSessionData
import com.example.crowdconnectapp.models.Question

data class AtendeeSession(
    val sessionId: String,
    val responses: List<Response>,
    var title: String,
    val date: String,
    val time: String,
    var score: Int = 0
)

data class Response(
    val questionIndex: Int,
    val selectedAnswerIndex: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentsSessions(attendeeId: String) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Sessions") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
        )
    }) {

        var attendedSessionsList by remember { mutableStateOf<List<AtendeeSession>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }
        val context = LocalContext.current

        LaunchedEffect(attendeeId) {
            try {
                val fetchedSessions = fetchAttendedSessions(attendeeId)
                val updatedSessions = mutableListOf<AtendeeSession>()

                fetchedSessions.forEachIndexed { index, session ->
                    val (title, questions) = fetchSessionData(session.sessionId)
                    val updatedSession = session.copy(
                        title = title,
                        score = calculateScore(session.responses, questions)
                    )
                    updatedSessions.add(updatedSession)
                }

                attendedSessionsList = updatedSessions
                isLoading = false
            } catch (e: Exception) {
                Log.e("RecentsSessions", "Error fetching sessions", e)
                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
                isLoading = false
            }
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .wrapContentSize(Alignment.Center)
            )
        } else {
            if (attendedSessionsList.isEmpty()) { // Check if the attended sessions list is empty or null
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No sessions joined Yet!",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "Recent Sessions",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 70.dp)
                        ) {
                            items(attendedSessionsList) { session ->
                                SessionCard(session)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun calculateScore(responses: List<Response>, questions: List<Question>): Int {
    var score = 0
    responses.forEachIndexed { _, response ->
        val question = questions.getOrNull(response.questionIndex)
        if (question != null) {
            if (question.correctAnswerIndex == response.selectedAnswerIndex) {
                score++
            }
        }
    }
    return score
}

@Composable
fun SessionCard(session: AtendeeSession) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .padding(horizontal = 10.dp),
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Quiz,
                    contentDescription = "Session Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = session.title,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Row {
                        Text(
                            text = "Score: ",
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${session.score}/${session.responses.size}",// can you add total no of questions here
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                    Row {
                        Text(
                            text = "Date: ",
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = session.date,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Time: ",
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = session.time,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
