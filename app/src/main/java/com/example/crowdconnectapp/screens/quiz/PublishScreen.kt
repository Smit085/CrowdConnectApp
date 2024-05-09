package com.example.crowdconnectapp.screens.quiz

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.crowdconnectapp.components.qrcode.FlipCard
import com.example.crowdconnectapp.data.addToDB
import com.example.crowdconnectapp.data.readDB
import com.example.crowdconnectapp.models.QuizViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.security.SecureRandom
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PublishScreen() {
    val quizViewModel: QuizViewModel = hiltViewModel()

    if (quizViewModel.sessioncode == "") {
        quizViewModel.sessioncode = generateCode("QZ")
    }

    Column(Modifier.fillMaxSize()) {
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
                onClick = { val clipboardManager =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("password", quizViewModel.sessioncode)
                    clipboardManager.setPrimaryClip(clip) },
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
        Button(
            onClick = {
                addToDB(quizViewModel,"Sessions",quizViewModel.sessioncode)
            },
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Publish", style = MaterialTheme.typography.titleLarge)
        }
    }
}

fun generateCode(prefix: String): String {
    val charPool: List<Char> = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    val random = SecureRandom()
    return prefix + (1..8).map { random.nextInt(charPool.size) }.map(charPool::get).joinToString("")
}


