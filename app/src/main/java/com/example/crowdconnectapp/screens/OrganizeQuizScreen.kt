package com.example.crowdconnectapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crowdconnectapp.components.Picker
import com.example.crowdconnectapp.components.rememberPickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavHostController

@Composable
fun OrganizeQuizScreen(navController: NavHostController) {
    App()
}

@Preview
@Composable
fun App() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var duration by remember { mutableStateOf("") }
        var timeout by remember { mutableStateOf("") }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Create Quiz", style = MaterialTheme.typography.titleLarge
            )
            Button(shape = RoundedCornerShape(5.dp), onClick = { /*TODO*/ }) {
                Text(text = "Host")
            }
        }
        OutlinedTextField(value = title, onValueChange = { text ->
            title = text
        }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth(), maxLines = 1
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = description,
            onValueChange = { text ->
                description = text
            },
            label = { Text("Description") },
            minLines = 4,
            maxLines = 5,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(60.dp)
        ) {
            NumberPicker("Duration",listOf("","sec", "min",""))
            NumberPicker("Timeout",listOf("","sec", "min", "hrs","",))
//            OutlinedTextField(value = duration, onValueChange = { text ->
//                duration = text
//            }, label = { Text("Duration(sec)") },
//                modifier = Modifier.weight(1f)
//            )
//            Spacer(modifier = Modifier.width(16.dp))
//            OutlinedTextField(value = timeout, onValueChange = { text ->
//                timeout = text
//            }, label = { Text("Timeout") }, modifier = Modifier.weight(1f)
//            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text(text = "S")
        }
        Button(
            onClick = {}, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Question")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {}, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Quiz")
        }
    }
}

@Composable
fun NumberPicker(label: String, units: List<String>) {
    var number by remember { mutableIntStateOf(0) }
    Column {
        Text(
            text = "$label: ", style = MaterialTheme.typography.bodyLarge
        )
        Row(modifier = Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { number = if (number == 0) 0 else number - 10 },
                modifier = Modifier.width(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Minus",
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color.LightGray)
                        .size(25.dp),
                    tint = Color.Black
                )
            }
            Text(text = "$number")
            IconButton(onClick = {
                number += 10
            }, modifier = Modifier.width(40.dp)) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Add",
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color.LightGray)
                        .size(25.dp),
                    tint = Color.Black
                )
            }
            Picker(units)

        }
    }
}

@Composable
fun Picker(units: List<String>) {
    val units = remember { units }
    val unitsPickerState = rememberPickerState()
    Picker(
        state = unitsPickerState,
        items = units,
        visibleItemsCount = 3,
        modifier = Modifier,
        textModifier = Modifier.padding(8.dp),
        textStyle = TextStyle(fontSize = 18.sp)
    )
}
