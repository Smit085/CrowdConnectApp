package com.example.crowdconnectapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.crowdconnectapp.screens.AttendeeDashboard
import com.example.crowdconnectapp.screens.HostDashboard
import com.example.crowdconnectapp.screens.OrganizePollScreen
import com.example.crowdconnectapp.screens.OrganizeQuizScreen
import com.example.crowdconnectapp.screens.WelcomeScreen
import com.example.crowdconnectapp.ui.theme.CrowdConnectAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CrowdConnectAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "welcomeScreen") {
        composable(route = "welcomeScreen") {
            WelcomeScreen(navController)
        }
        composable(route = "hostScreen") {
            HostDashboard(navController)
        }
        composable(route = "attendeeScreen") {
            AttendeeDashboard(navController)
        }
        composable(route = "organizeQuizScreen") {
            OrganizeQuizScreen(navController)
        }
        composable(route = "organizePollScreen") {
            OrganizePollScreen(navController)
        }
    }
}