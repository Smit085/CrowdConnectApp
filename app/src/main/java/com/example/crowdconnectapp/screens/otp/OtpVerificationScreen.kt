package com.example.crowdconnectapp.screens.otp

import android.nfc.Tag
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider

@Composable
fun OtpVerificationScreen(navController: NavHostController, verificationId: String?) {
    val context = LocalContext.current
    var otpValue by remember { mutableStateOf("") }
    val verificationInProgress by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Mail,
            contentDescription = "Mail Icon",
            tint = Color(0xFFDD761C),
            modifier = Modifier
                .size(100.dp)
        )
        Spacer(modifier = Modifier.height(42.dp))
        Text(
            text = "OTP Verification",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight(800)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Please enter the OTP sent to you", // Corrected text
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            fontSize = 11.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        OtpTextField(
            otpText = otpValue,
            onOtpTextChange = { value, otpInputFilled ->
                otpValue = value
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text(
                text = "Didn't receive the otp?",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                fontSize = 11.sp
            )
                Text(
                    text = " RESEND AGAIN",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp,
                    fontWeight = FontWeight(800),
                    modifier = Modifier.clickable {
                    }
                )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
//                if (!verificationInProgress) {
                    // Call function to verify OTP
                    verifyOtp(navController,verificationId, otpValue)
//                }
            },
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(250.dp)
        ) {
            Text(text = "Verify", style = MaterialTheme.typography.titleMedium)
        }
    }
}

fun verifyOtp(navController: NavHostController, verificationId: String?, otp: String) {
    if (verificationId.isNullOrBlank()) {
        // Handle error: Verification ID is null or blank
        Log.d("Error", "Verification ID is null or blank")
        return
    }

    val credential = PhoneAuthProvider.getCredential(verificationId, otp)
    FirebaseAuth.getInstance().signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("NAV", "isSuccessful")
                navController.navigate("hostScreen")
            } else {
                Log.d("NAV", "UnSuccessful")
            }
        }
}
@Composable
fun OtpTextField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpCount: Int = 6,
    onOtpTextChange: (String, Boolean) -> Unit
) {
    LaunchedEffect(Unit) {
        if (otpText.length > otpCount) {
            throw IllegalArgumentException("Otp text value must not have more than otpCount: $otpCount characters")
        }
    }

    BasicTextField(
        modifier = modifier,
        value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
        onValueChange = {
            if (it.text.length <= otpCount) {
                onOtpTextChange.invoke(it.text, it.text.length == otpCount)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.Center) {
                repeat(otpCount) { index ->
                    CharView(
                        index = index,
                        text = otpText
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    )
}

@Composable
private fun CharView(
    index: Int,
    text: String
) {
    val isFocused = text.length == index
    val char = when {
        index == text.length -> "0"
        index > text.length -> "0"
        else -> text[index].toString()
    }
    Text(
        modifier = Modifier
            .width(40.dp)
            .border(
                1.dp, when {
                    isFocused -> Color(0xFF1679AB)
                    else -> Color.LightGray
                }, RoundedCornerShape(8.dp)
            )
            .padding(2.dp),
        text = char,
        style = MaterialTheme.typography.headlineSmall,
        color = if (isFocused) {
            Color(0xFF1679AB)
        } else {
            Color.LightGray
        },
        textAlign = TextAlign.Center
    )
}