package com.example.crowdconnectapp.screens.host

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.Poll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.crowdconnectapp.models.AuthViewModel
import com.example.crowdconnectapp.models.getAvatarResource
import com.example.crowdconnectapp.screens.UpdateProfileScreen

data class Session(val id: String, val name: String, val description: String, val routes: String ,val icon: ImageVector)

val sessions = listOf(
    Session("1", "Organize Quiz", "Organize a quiz event", "organizeQuizScreen" , Icons.AutoMirrored.Filled.EventNote),
    Session("2", "Organize Poll", "Create and manage polls", "OrganizeVotingScreen" , Icons.Default.Poll),
    Session("3", "Take Attendance", "Take attendance of participants", "OrganizeVotingScreen" , Icons.Default.Group),
    Session("4", "Organize Voting", "Organize voting sessions", "OrganizeVotingScreen" , Icons.Default.HowToVote),
    Session("5", "Share Materials", "Share study materials and resources", "OrganizeVotingScreen" , Icons.Default.AttachFile),
    Session("6", "Collect Assignments", "Collect assignments from participants", "OrganizeVotingScreen" , Icons.Default.Folder),
    Session("7", "Take Survey/Feedback", "Gather feedback and conduct surveys", "OrganizeVotingScreen" , Icons.Default.Feedback)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostDashboard(authViewModel: AuthViewModel,navController: NavHostController) {
    val userAvatar by authViewModel.userAvatar.collectAsState()
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Host Dashboard") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
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
    BackHandler {
        navController.popBackStack()
        navController.navigate("welcomeScreen")
    }
}

@Composable
fun SessionList(sessions: List<Session>, navController: NavHostController) {
    LazyColumn(modifier = Modifier.padding(bottom = 70.dp)) {
        items(sessions) { session ->
            SessionCard(session, navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionCard(session: Session, navController: NavHostController) {
    Card(
        onClick = {
            navController.navigate(session.routes)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .padding(horizontal = 10.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(
                imageVector = session.icon,
                contentDescription = session.name,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = session.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = session.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}
