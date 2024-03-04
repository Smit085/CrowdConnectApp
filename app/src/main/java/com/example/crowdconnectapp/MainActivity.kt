package com.example.crowdconnectapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.crowdconnectapp.navigation.NavHostController
import com.example.crowdconnectapp.ui.theme.CrowdConnectAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (Intent.ACTION_VIEW == intent.action) {
//            val sessionId = intent.data?.lastPathSegment
//            if (!sessionId.isNullOrBlank()) {
//                // Navigate to session screen using sessionId
//            }
//        }
        setContent {
            CrowdConnectAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHostController()
                }
            }
        }
    }
}
