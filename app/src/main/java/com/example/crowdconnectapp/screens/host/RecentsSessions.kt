package com.example.crowdconnectapp.screens.host

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.crowdconnectapp.models.AuthViewModel
import com.example.crowdconnectapp.models.getAvatarResource
import com.example.crowdconnectapp.ui.theme.Blue
import com.example.crowdconnectapp.ui.theme.Grey
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentsSessions(authViewModel: AuthViewModel, navController: NavHostController) {
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
                        navController.navigate("hostScreen")
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
        var sessions by remember { mutableStateOf<List<PastSessions>?>(null) } // Nullable sessions
        var isLoading by remember { mutableStateOf(true) } // Loading state
        val currentUser = FirebaseAuth.getInstance().currentUser
        val hostId = currentUser?.phoneNumber ?: ""

        LaunchedEffect(Unit) {
            fetchSessions(hostId) { fetchedSessions ->
                sessions = fetchedSessions
                isLoading = false
            }
        }

        if (isLoading) { // Show loading indicator if data is still loading
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .wrapContentSize(Alignment.Center)
            )
        } else {
            if (sessions.isNullOrEmpty()) { // Check if the session list is empty or null
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No sessions created Yet!",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                // Display sessions
                sessions?.let { sessionList ->
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

                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 70.dp)
                        ) {
                            items(sessionList) { session ->
                                SessionCard(session = session, onDeleteConfirmed = {
                                    deleteSession(session.id, onSuccess = {
                                        sessions = sessions?.filterNot { it.id == session.id }
                                    }, onError = {
                                        // Handle error
                                    })
                                }, onItemClick = {
                                    // Navigate to a new screen showing details of responded attendees
                                    navController.navigate("sessionResponses/${session.id}")
                                })
                            }
                        }
                    }
                }
            }
        }

    }
    BackHandler {
        navController.popBackStack()
        navController.navigate("hostScreen")
    }
}


data class PastSessions(
    val id: String,
    val title: String,
    val duration: Int,
    val durationIn: String,
    val selectedDate: String,
    val selectedTime: String,
    val timeout: Int,
    val timeoutIn: String,
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SessionCard(
    session: PastSessions, onDeleteConfirmed: () -> Unit, onItemClick: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .padding(horizontal = 10.dp)
            .clickable { onItemClick() },
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
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.weight(1f))
                val sessionStatusText = if (isSessionLive(session)) {
                    "• Live"
                } else {
                    "• Completed"
                }
                Card(
                    shape = RoundedCornerShape(2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (sessionStatusText == "• Live") Red else Grey,
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                ) {
                    Text(
                        text = sessionStatusText, style = MaterialTheme.typography.titleSmall.copy(
                            color = White, fontSize = 10.sp
                        ), modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Row {
                        Text(
                            text = "Duration/Question: ",
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = if (session.duration != 0) "${session.duration} ${session.durationIn}" else "No limit",
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
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
                            text = session.selectedDate,
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
                            text = session.selectedTime,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { showDialog = true },
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Red,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        }
    }
    if (showDialog) {
        DeleteConfirmationDialog(onDeleteConfirmed = {
            onDeleteConfirmed()
            showDialog = false
        }, onDismiss = { showDialog = false })
    }
}

fun isSessionLive(session: PastSessions): Boolean {

    val selectedDate = session.selectedDate
    val selectedTime = session.selectedTime
    val timeoutIn = session.timeoutIn
    val timeout = if (timeoutIn == "min") session.timeout else session.timeout * 60
    if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val currentDate = Date()
        try {
            val sessionStartDate = dateFormat.parse("$selectedDate $selectedTime")

            val calendar = Calendar.getInstance().apply {
                time = sessionStartDate!!
                add(Calendar.MINUTE, timeout)
            }
            val sessionEndDate = calendar.time

            return currentDate.after(sessionStartDate) && currentDate.before(sessionEndDate)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    } else {
    }
    return false
}


@Composable
fun DeleteConfirmationDialog(
    onDeleteConfirmed: () -> Unit, onDismiss: () -> Unit
) {
    AlertDialog(onDismissRequest = onDismiss,
        title = { Text("Confirm Deletion") },
        text = { Text("Are you sure you want to delete this session?") },
        confirmButton = {
            Button(
                onClick = onDeleteConfirmed
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        })
}

private fun deleteSession(sessionId: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
    val firestore = FirebaseFirestore.getInstance()

    try {
        firestore.collection("Sessions").document(sessionId).delete().addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener { e ->
            onError(e)
        }
    } catch (e: Exception) {
        onError(e)
    }
}


private suspend fun fetchSessions(hostId: String, onSuccess: (List<PastSessions>) -> Unit) {
    val firestore = FirebaseFirestore.getInstance()
    val sessionList = mutableListOf<PastSessions>()
    try {
        val querySnapshot = firestore.collection("Sessions")
            .whereEqualTo("hostId", hostId)
            .get()
            .await()

        for (document in querySnapshot.documents) {
            val id = document.id
            val title = document.getString("title") ?: ""
            val duration = document.getLong("duration") ?: 0
            val durationIn = document.getString("durationIn") ?: ""
            val selectedDate = document.getString("selectedDate") ?: ""
            val selectedTime = document.getString("selectedTime") ?: ""
            val timeout = document.getLong("timeout") ?: 10
            val timeoutIn = document.getString("timeoutIn") ?: "min"
            sessionList.add(
                PastSessions(
                    id,
                    title,
                    duration.toInt(),
                    durationIn,
                    selectedDate,
                    selectedTime,
                    timeout.toInt(),
                    timeoutIn
                )
            )
        }
        onSuccess(sessionList)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}