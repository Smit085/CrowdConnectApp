package com.example.crowdconnectapp.screens.otp

import android.telephony.PhoneNumberUtils
import androidx.compose.animation.VectorConverter
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crowdconnectapp.data.addToDB
import com.example.crowdconnectapp.ui.theme.Blue
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
@Preview
fun LoginScreen() {
    var phoneNumber by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Send,
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
            label = "Enter Mobile Number",
            icon = Icons.Filled.Phone
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {},
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally).width(250.dp)
        ) {
            Text(text = "Get OTP", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
private fun OutlinedTextFieldWithIcon(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
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
                    textFieldValue.value = it
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
            keyboardActions = KeyboardActions(onDone = { /* Handle Done action */ }),
            maxLines = 1,
            modifier = Modifier.padding().height(55.dp).width(250.dp),
            leadingIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        Modifier.padding(start = 12.dp, end = 4.dp),
                        tint = Color.Black
                    )
                    Text(
                        text = "+91",
                        fontSize = 18.sp,
                        modifier = Modifier.alignByBaseline().padding(end = 18.dp)
                    )
                }
            }
        )
    }
}