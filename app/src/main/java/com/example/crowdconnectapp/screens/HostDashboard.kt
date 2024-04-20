package com.example.crowdconnectapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

data class Session(val id: String, val name: String)

val sessions = listOf(
    Session("1", "Organize Quiz"),
    Session("2", "Organize Poll"),
    Session("3", "Take Attendance"),
    Session("4", "Organize Voting"),
    Session("5", "Share Materials"),
    Session("6", "Collect Assignments"),
    Session("7", "Take Survey/Feedback"),
    Session("8", "Take Survey/Feedback"),
    Session("9", "Take Survey/Feedback")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostDashboard(navController: NavHostController) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Host Dashboard") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
        )
    }) {
        Column(modifier = Modifier.padding(it)) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Services",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            SessionList(sessions, navController)
        }
    }
}

@Composable
fun SessionList(sessions: List<Session>, navController: NavHostController) {
    LazyColumn {
        items(sessions) { session ->
            SessionCard(session, navController)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionCard(session: Session, navController: NavHostController) {
    Card(
        onClick = {
            when (session.id) {
                "1" -> navController.navigate("organizeQuizScreen")
                "2" -> navController.navigate("organizeQuizScreen")
                "3" -> navController.navigate("organizeVotingScreen")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = session.name, style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "ID: ${session.id}", style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
