package com.example.crowdconnectapp.screens.host

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentsSessions(navController: NavHostController) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Sessions") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
        )
    }) {
//        Column(modifier = Modifier.padding(it)) {
//            Text(
//                modifier = Modifier.padding(16.dp),
//                text = "Recents Sesssions",
//                style = MaterialTheme.typography.titleMedium,
//                color = MaterialTheme.colorScheme.onSurface
//            )
//        }

        var sessions by remember { mutableStateOf<List<PastSessions>>(emptyList()) }
//
        LaunchedEffect(Unit) {
            fetchSessions { fetchedSessions ->
                sessions = fetchedSessions
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Recents Sesssions",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            LazyColumn {
                items(sessions) { session ->
                    SessionCard(session = session, onDeleteConfirmed = {
                        deleteSession(session.id,
                            onSuccess = {
                                sessions = sessions.filterNot { it.id == session.id }
                            },
                            onError = { e ->
                                //Handle ERROr
                            }
                        )
                    })
                }
            }
        }
    }
}

data class PastSessions(
    val id: String,
    val title: String,
    val duration: Int,
    val durationIn: String,
    val selectedDate: String,
    val selectedTime: String
)

@Composable
fun SessionCard(
    session: PastSessions,
    onDeleteConfirmed: () -> Unit // Add onDeleteConfirmed lambda parameter
) {
    var showDialog by remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = White,
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
                    text = session.title, style = MaterialTheme.typography.headlineMedium
                )
                // Label indicating session is live or ended
                Spacer(modifier = Modifier.weight(1f))
                Card(
                    shape = RoundedCornerShape(2.dp), colors = CardDefaults.cardColors(
                        containerColor = Red,
                    ), elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                ) {
                    Text(
                        text = "• Live", style = MaterialTheme.typography.titleSmall.copy(
                            color = White, fontSize = 10.sp
                        ), modifier = Modifier.padding(4.dp)
                    )
                }
            }
            Row {
                Column {
                    Text(
                        text = "Duration: ${session.duration} ${session.durationIn}",
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    // Date and Time
                    Row {
                        Text(
                            text = "Date: ${session.selectedDate}",
                            style = MaterialTheme.typography.labelMedium,
                                    maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Time: ${session.selectedTime}",
                            style = MaterialTheme.typography.labelMedium,
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
                        modifier = Modifier
                            .size(25.dp)
                    )
                }
            }
        }
    }
    if (showDialog) {
        DeleteConfirmationDialog(
            onDeleteConfirmed = {
                onDeleteConfirmed()
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun DeleteConfirmationDialog(
    onDeleteConfirmed: () -> Unit,
    onDismiss: () -> Unit
) {
    // Implement your dialog UI here, such as AlertDialog or custom dialog
    // Example using AlertDialog
    AlertDialog(
        onDismissRequest = onDismiss,
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
        }
    )
}

private fun deleteSession(sessionId: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
    val firestore = FirebaseFirestore.getInstance()

    try {
        firestore.collection("Sessions").document(sessionId).delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError(e)
            }
    } catch (e: Exception) {
        onError(e)
    }
}


private suspend fun fetchSessions(onSuccess: (List<PastSessions>) -> Unit) {
    val firestore = FirebaseFirestore.getInstance()
    val sessionList = mutableListOf<PastSessions>()
    try {
        val querySnapshot = firestore.collection("Sessions").get().await()
        for (document in querySnapshot.documents) {
            val id = document.id
            val title = document.getString("title") ?: ""
            val duration = document.getLong("duration") ?: 0
            val durationIn = document.getString("durationIn") ?: ""
            val selectedDate = document.getString("selectedDate") ?: ""
            val selectedTime = document.getString("selectedTime") ?: ""
            sessionList.add(
                PastSessions(
                    id,
                    title,
                    duration.toInt(),
                    durationIn,
                    selectedDate,
                    selectedTime,
                )
            )
        }
        onSuccess(sessionList)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}