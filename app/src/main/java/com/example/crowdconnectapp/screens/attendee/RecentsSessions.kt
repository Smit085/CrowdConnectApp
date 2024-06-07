package com.example.crowdconnectapp.screens.attendee

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.crowdconnectapp.data.fetchAttendedSessions
import com.example.crowdconnectapp.data.fetchSessionData
import com.example.crowdconnectapp.models.AuthViewModel
import com.example.crowdconnectapp.models.Question
import com.example.crowdconnectapp.models.getAvatarResource

data class AtendeeSession(
    val sessionId: String,
    val responses: List<Response>,
    var title: String,
    val date: String,
    val time: String,
    val isEvaluateEnabled: Boolean,
    var score: Int = 0
)

data class Response(
    val questionIndex: Int = -1,
    val selectedAnswerIndex: Int = -1
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentsSessions(attendeeId: String, authViewModel: AuthViewModel, navController: NavHostController) {
    val userAvatar by authViewModel.userAvatar.collectAsState()
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Sessions") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            navigationIcon = {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                        navController.navigate("attendeeScreen")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                Image(
                    painter = painterResource(id = getAvatarResource(userAvatar)),
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .padding(end = 8.dp)
                        .clickable {
                            navController.navigate("updateProfileScreen")
                        }
                )
            }
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
                    val (title, questions,isEvaluateEnabled) = fetchSessionData(session.sessionId)
                    val updatedSession = session.copy(
                        title = title,
                        isEvaluateEnabled = isEvaluateEnabled,
                        score = calculateScore(session.responses, questions)
                    )
                    updatedSessions.add(updatedSession)
                }

                attendedSessionsList = updatedSessions
                isLoading = false
            } catch (e: Exception) {
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
    BackHandler {
        navController.popBackStack()
        navController.navigate("attendeeScreen")
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
                        // Display score or placeholder based on isEvaluateEnabled flag
                        Text(
                            text = if (session.isEvaluateEnabled) "${session.score}/${session.responses.size}" else "-/:",
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

