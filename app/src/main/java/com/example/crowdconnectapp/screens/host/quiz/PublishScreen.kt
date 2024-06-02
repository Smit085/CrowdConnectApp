package com.example.crowdconnectapp.screens.host.quiz

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.annotation.RequiresApi
import androidx.navigation.NavHostController
import com.example.crowdconnectapp.components.qrcode.FlipCard
import com.example.crowdconnectapp.data.addSession
import com.example.crowdconnectapp.data.addToDB
import com.example.crowdconnectapp.models.QuizViewModel
import com.google.firebase.auth.FirebaseAuth
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import java.security.SecureRandom

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PublishScreen(navController: NavHostController, quizViewModel: QuizViewModel) {
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser
    val hostId = currentUser?.phoneNumber ?: ""
    var isLoading by remember { mutableStateOf(false) } // Loading state

    if (quizViewModel.sessioncode == "") {
        quizViewModel.sessioncode = generateCode("QZ")
    }

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        FlipCard()
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 10.dp)
        ) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            val context = LocalContext.current
            IconButton(
                onClick = { context.startActivity(shareIntent) }
            ) {
                Icon(
                    Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(30.dp)
                )
            }
            IconButton(
                onClick = {
                    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("password", quizViewModel.sessioncode)
                    clipboardManager.setPrimaryClip(clip)
                }
            ) {
                Icon(
                    Icons.Default.ContentCopy,
                    contentDescription = "Copy",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(30.dp)
                )
            }
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(35.dp))
        } else {
            Button(
                onClick = {
                    isLoading = true
                    addSession(
                        hostId = hostId,
                        name = "Smit",
                        mobno = hostId,
                        sessionId = quizViewModel.sessioncode,
                        onSuccess = {
                            addToDB(quizViewModel, "Sessions", quizViewModel.sessioncode)
                            Toast.makeText(context, "Your Session has been Created.", Toast.LENGTH_SHORT).show()
                            isLoading = false
                            navController.navigate("hostScreen")
                        },
                        onFailure = { exception ->
                            Log.e("SessionCreationError", "Error Creating Session", exception)
                            Toast.makeText(context, "Failed to create session.", Toast.LENGTH_SHORT).show()
                            isLoading = false
                        }
                    )
                },
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Publish", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

fun generateCode(prefix: String): String {
    val charPool: List<Char> = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    val random = SecureRandom()
    return prefix + (1..8).map { random.nextInt(charPool.size) }.map(charPool::get).joinToString("")
}
