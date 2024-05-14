package com.example.crowdconnectapp.screens.otp

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import java.util.concurrent.TimeUnit

@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    var phoneNumber by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()
    auth.setLanguageCode("en")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Send,
            contentDescription = "Phone Icon",
            tint = Color(0xFFDD761C),
            modifier = Modifier
                .size(100.dp)
                .rotate(-45F)
        )
        Spacer(modifier = Modifier.height(42.dp))
        Text(
            text = "OTP Verification",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight(800)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "We will send you a one-time OTP on this given mobile number.",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            fontSize = 11.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextFieldWithIcon(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            icon = Icons.Filled.Phone
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            enabled = phoneNumber.length == 10 && !isLoading,
            onClick = {
                isLoading = true
                hideKeyboard(context)
                // Call the composable function from here
                sendOtp(context, phoneNumber, auth) { verificationId ->
                    isLoading = false
                    navController.navigate("otpVerificationScreen/$verificationId")
                }
            },
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(250.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(25.dp)
                )
            } else {
                Text(text = "Get OTP", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

fun hideKeyboard(context: Context) {
    val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow((context as Activity).currentFocus?.windowToken, 0)
}

fun sendOtp(context: Context, phoneNumber: String, auth: FirebaseAuth, onOtpSent: (String) -> Unit) {

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification.
            // 2 - Auto-retrieval.
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the phone number format is not valid.
            Log.e(TAG, "onVerificationFailed: ${e.message}", e)
            // Show error toast or handle failure
            Toast.makeText(context, "Failed to send OTP. Please try again.", Toast.LENGTH_SHORT).show()
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            // The SMS verification code has been sent to the provided phone number.
            onOtpSent(verificationId)
        }
    }
    val options = PhoneAuthOptions.newBuilder(auth)
        .setPhoneNumber("+91$phoneNumber") // Phone number to verify
        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
        .setActivity(context as Activity) // Activity (for callback binding)
        .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
        .build()

    PhoneAuthProvider.verifyPhoneNumber(options)
}


@Composable
private fun OutlinedTextFieldWithIcon(
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector
) {
    val maxLength = 10
    val textFieldValue = remember { mutableStateOf(value) }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = textFieldValue.value,
            onValueChange = {
                if (it.length <= maxLength) {
                    textFieldValue.value = it // Corrected line: Update the state directly
                    onValueChange(it)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(0xFF1679AB),
                focusedBorderColor = Color(0xFF1679AB)
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            textStyle = TextStyle(fontSize = 18.sp),
            keyboardActions = KeyboardActions(onDone = { /* Handle Done action */ }),
            maxLines = 1,
            modifier = Modifier
                .padding()
                .height(55.dp)
                .width(250.dp),
            leadingIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        Modifier.padding(start = 12.dp, end = 6.dp),
                        tint = Color.Black
                    )
                    Divider(
                        color = Color.Gray,
                        modifier = Modifier
                            .height(30.dp)
                            .width(1.dp)
                    )
                    Text(
                        text = "+91",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 6.dp, end = 4.dp)
                    )
                }
            }
        )
    }
}
