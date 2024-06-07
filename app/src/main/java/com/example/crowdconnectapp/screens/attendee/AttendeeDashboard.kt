package com.example.crowdconnectapp.screens.attendee

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.crowdconnectapp.R
import com.example.crowdconnectapp.models.AuthViewModel
import com.example.crowdconnectapp.models.getAvatarResource

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendeeDashboard(navController: NavHostController, authViewModel: AuthViewModel) {
    val userAvatar by authViewModel.userAvatar.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Attendee Dashboard") },
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
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.qrcode_img),
                contentDescription = "QR Code",
                modifier = Modifier.size(300.dp)
            )

            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            var isSheetOpen by rememberSaveable { mutableStateOf(false) }

            if (isSheetOpen) {
                ModalBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = { isSheetOpen = false },
                    windowInsets = WindowInsets.ime
                ) {
                    BottomSheet(navController)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Steps:", fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                )
                InfoRow(text = "Click on join session.")
                InfoRow(text = "Scan qrcode or enter code manually.")
                InfoRow(text = "Start the session.")
                InfoRow(text = "Submit your response.")
                InfoRow(text = "Review your score.")
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { isSheetOpen = true },
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .width(300.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.PhotoCamera,
                    contentDescription = "PhotoCamera",
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically)
                )
                Spacer(Modifier.width(8.dp)) // Adds some space between the icon and the text
                Text(
                    text = "Join Session",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
    BackHandler {
        navController.popBackStack()
        navController.navigate("welcomeScreen")
    }
}
@Composable
fun InfoRow(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = "• $text",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
