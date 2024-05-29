package com.example.crowdconnectapp.screens.attendee

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.crowdconnectapp.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendeeDashboard(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Attendee Dashboard") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
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
                InfoRow(text = "Submit your response.")
                InfoRow(text = "Review your score.")
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { isSheetOpen = true  },
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
}

@Composable
fun BottomSheetContent(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Enter Session Code", style = MaterialTheme.typography.bodyMedium)
        var code by remember { mutableStateOf("") }
        OutlinedTextField(
            value = code,
            onValueChange = { code = it },
            label = { Text("Session Code") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                if (code.startsWith("QZ", true)) {
                    navController.navigate("startQuiz")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Submit")
        }
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
