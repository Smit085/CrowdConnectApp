import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.NavHostController

data class Question(
    val id: Int,
    var text: String,
    val options: MutableList<String> = mutableListOf()
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganizeVotingScreen(navController: NavHostController) {
    var questions by remember {
        mutableStateOf(
            mutableListOf(
                Question(1, ""),
                Question(2, "")
            )
        )
    }

//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(text = "Manage Questions") }
//            )
//        },
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = {
//                    println("FAB clicked") // Add this line to check if the FAB click is detected
//                    questions = questions.toMutableList().apply {
//                        add(Question(questions.size + 1, "New Question"))
//                    }
//
//                },
//                content = { Icon(Icons.Default.Add, contentDescription = "Add Question") }
//            )
//        }
//    ) {
//        LazyColumn(
//            modifier = Modifier.padding(it),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            items(questions) { question ->
//                QuestionItem(question)
//            }
//        }
//    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = "Previous",
                modifier = Modifier.padding(0.dp),
                tint = Color.Blue
            )
            TextButton(
                onClick = { navController.navigate("organizeQuizScreen") },
                contentPadding = PaddingValues(0.dp) // Set contentPadding to remove extra padding
            ) {
                Text(
                    text = "Previous",
                    color = Color.Blue,
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .wrapContentSize(),
            contentPadding = PaddingValues(bottom = 4.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(questions) { question ->
                QuestionItem(question)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    questions = questions.toMutableList().apply {
                        add(Question(questions.size + 1, "New Question"))
                    }
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add",
                tint = Color.Blue
            )
            Text(
                text = "New Question",
                color = Color.Blue,
            )
        }
    }
}


@Composable
fun QuestionItem(question: Question) {
    var editedQuestion by remember { mutableStateOf(question.text) }
    var newOption by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            OutlinedTextField(
                value = editedQuestion,
                onValueChange = { editedQuestion = it },
                label = {
                    Text("Question ${question.id}") // Include question number dynamically
                }
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Options:", style = MaterialTheme.typography.titleMedium)
            question.options.forEachIndexed { index, option ->
                OptionItem(option, onDelete = { question.options.removeAt(index) })
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = newOption,
                    onValueChange = { newOption = it },
                    label = { Text("New Option") }
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (newOption.isNotBlank()) {
                            question.options.add(newOption)
                            newOption = ""
                        }
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Option")
                }
            }
        }
    }
}

@Composable
fun OptionItem(option: String, onDelete: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "- $option")
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Option")
        }
    }
}