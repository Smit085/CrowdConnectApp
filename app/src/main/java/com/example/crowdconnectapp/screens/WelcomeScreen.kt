package com.example.crowdconnectapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.crowdconnectapp.R

@Composable
fun WelcomeScreen(navController: NavHostController) {
    var selectedOption by remember { mutableStateOf<Option?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connect), // Replace with your logo
            contentDescription = "Logo",
            modifier = Modifier
                .size(100.dp)
                .clip(MaterialTheme.shapes.medium)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome to CorwdConnect",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Join the session or host your own.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        OptionButton("Get started as a host") {
            // Handle Host button click
            selectedOption = Option.Host
            navController.navigate("hostScreen")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OptionButton("Continue as a Attendee") {
            // Handle Attendee button click
            selectedOption = Option.Attendee
            navController.navigate("attendeeScreen")
        }
    }
}

@Composable
fun OptionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick, modifier = Modifier.width(320.dp)
    ) {
        Text(
            text = text, color = Color.White, fontSize = 18.sp
        )
    }
}

enum class Option {
    Host, Attendee
}