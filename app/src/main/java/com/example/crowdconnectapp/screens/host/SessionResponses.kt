package com.example.crowdconnectapp.screens.host

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crowdconnectapp.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.FormatListNumbered
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.crowdconnectapp.models.Question
import com.example.crowdconnectapp.models.getAvatarResource
import com.example.crowdconnectapp.screens.attendee.Response
import com.example.crowdconnectapp.screens.attendee.calculateScore
import com.google.firebase.firestore.PropertyName
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionResponses(
    navController: NavController,
    sessionId: String,
    sessionViewModel: SessionViewModel = viewModel()
) {
    LaunchedEffect(sessionId) {
        sessionViewModel.getSession(sessionId)
    }

    val session by sessionViewModel.session.collectAsState()
    val attendees by sessionViewModel.attendees.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sessions Analytics") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
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
                        painter = painterResource(id = R.drawable.avatar1),
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
        }
    ) {
        session?.let { session ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = session.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.W400,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = session.description,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Justify
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Card(
                            modifier = Modifier.size(40.dp), shape = RoundedCornerShape(6.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.CalendarToday,
                                    contentDescription = "Start Date",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Start Date",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = session.selectedDate,
                                fontSize = 12.sp,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.W400,
                                color = Color.Gray
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Card(
                            modifier = Modifier.size(40.dp), shape = RoundedCornerShape(6.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.WatchLater,
                                    contentDescription = "Start Time",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Start Time",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = session.selectedTime,
                                fontSize = 12.sp,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.W400,
                                color = Color.Gray
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Card(
                            modifier = Modifier.size(40.dp), shape = RoundedCornerShape(6.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.FormatListNumbered,
                                    contentDescription = "No of Questions",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Total Questions",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = session.questions.size.toString(),
                                fontSize = 12.sp,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.W400,
                                color = Color.Gray
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Card(
                            modifier = Modifier.size(40.dp), shape = RoundedCornerShape(6.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.WatchLater,
                                    contentDescription = "Time Left",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Time Left",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = sessionViewModel.calculateTimeLeft(session),
                                fontSize = 12.sp,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.W400,
                                color = Color.Gray
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = "Attendees",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.W400
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (attendees.isEmpty()) {
                        Row(horizontalArrangement = Arrangement.Center) {
                            Text(text = "No response yet!")
                        }
                    } else {
                        AttendeesTable(attendees)
                    }
                }
            }
        } ?: run {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}


@Composable
fun AttendeesTable(attendees: List<Attendee>) {
    LazyColumn {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Sr No.",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Name",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(2f),
                )
                Text(
                    text = "Score",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Divider(
                color = Color.Gray, modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
            )
        }
        itemsIndexed(attendees) { index, attendee ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = (index + 1).toString() + ".",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Row(
                    modifier = Modifier.weight(4f), verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = getAvatarResource(attendee.avatar)),
                        contentDescription = "Attendee Avatar",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = attendee.name,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }
                Text(
                    text = attendee.score.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

data class SessionDetails(
    val title: String = "",
    val description: String = "",
    val selectedDate: String = "",
    val selectedTime: String = "",
    val timeout: Long = 0L,
    val questions: List<Question> = emptyList()
)

data class Attendee(
    val name: String = "",
    val avatar: Int = -1,
    val score: Int = -1,
    val attendedSessionlist: List<AttendedSession> = emptyList()
)

data class AttendedSession(
    val sessionId: String = "",
    val date: String = "",
    val time: String = "",
    val responses: List<Response> = emptyList()
)

class SessionRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getSession(sessionId: String): SessionDetails? {
        return try {
            val document = db.collection("Sessions").document(sessionId).get().await()
            if (document.exists()) {
                val title = document.getString("title") ?: "Untitled"
                val description = document.getString("description") ?: ""
                val selectedDate = document.getString("selectedDate") ?: ""
                val selectedTime = document.getString("selectedTime") ?: ""
                var timeout = document.getLong("timeout") ?: 0L
                val timeoutIn = document.getString("timeoutIn") ?: ""
                timeout = if (timeoutIn == "min") timeout else timeout * 60
                val questions = (document.get("questions") as? List<Map<String, Any>>
                    ?: emptyList()).mapNotNull { questionData ->
                    val question = questionData["question"] as? String ?: return@mapNotNull null
                    val options = questionData["options"] as? List<String> ?: return@mapNotNull null
                    val correctAnswerIndex = (questionData["correctAnswerIndex"] as? Long)?.toInt()
                        ?: return@mapNotNull null
                    Question(question, options, correctAnswerIndex)
                }
                SessionDetails(title, description, selectedDate, selectedTime, timeout, questions)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("SessionRepository", "Error getting session", e)
            null
        }
    }

    suspend fun getAttendees(sessionId: String): List<Attendee> {
        return try {
            val querySnapshot = db.collection("Attendee").get().await()
            val allAttendees = querySnapshot.toObjects(Attendee::class.java)

            // Filter attendees based on the sessionId in attendedSessionList
            val filteredAttendees = allAttendees.filter { attendee ->
                attendee.attendedSessionlist.any { session ->
                    session.sessionId == sessionId
                }
            }
            filteredAttendees
        } catch (e: Exception) {
            Log.e("SessionRepository", "Error getting attendees", e)
            emptyList()
        }
    }

    private fun calculateScore(responses: List<Response>, questions: List<Question>): Int {
        Log.i("responses", responses.toString() + questions.toString())
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

    suspend fun getAttendeesWithScores(sessionId: String): List<Attendee> {
        val attendees = getAttendees(sessionId)
        val session = getSession(sessionId)
        Log.i("Session", session.toString())
        if (session != null) {
            return attendees.map { attendee ->
                val attendedSession =
                    attendee.attendedSessionlist.find { it.sessionId == sessionId }
                val score =
                    calculateScore(attendedSession?.responses ?: emptyList(), session.questions)
                attendee.copy(score = score)
            }
        }
        return attendees
    }
}

class SessionViewModel : ViewModel() {

    private val repository = SessionRepository()

    private val _session = MutableStateFlow<SessionDetails?>(null)
    val session: StateFlow<SessionDetails?> get() = _session

    private val _attendees = MutableStateFlow<List<Attendee>>(emptyList())
    val attendees: StateFlow<List<Attendee>> get() = _attendees

    fun getSession(sessionId: String) {
        viewModelScope.launch {
            _session.value = repository.getSession(sessionId)
            getAttendeesWithScores(sessionId)
        }
    }

    private fun getAttendeesWithScores(sessionId: String) {
        viewModelScope.launch {
            _attendees.value = repository.getAttendeesWithScores(sessionId)
        }
    }

    fun calculateTimeLeft(session: SessionDetails): String {
        return try {
            val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val quizStartDateTime =
                dateTimeFormat.parse("${session.selectedDate} ${session.selectedTime}")

            val endTime = Calendar.getInstance().apply {
                time = quizStartDateTime!!
                add(Calendar.MINUTE, session.timeout.toInt())
            }.timeInMillis

            val currentTime = System.currentTimeMillis()
            val timeLeft = endTime - currentTime

            if (timeLeft > 0) {
                val hoursLeft = TimeUnit.MILLISECONDS.toHours(timeLeft)
                val minutesLeft = TimeUnit.MILLISECONDS.toMinutes(timeLeft) % 60
                "$hoursLeft hours and $minutesLeft minutes left"
            } else {
                "Time's up"
            }
        } catch (e: Exception) {
            Log.e("calculateTimeLeft", "Error parsing date/time", e)
            "N/A"
        }
    }
}
