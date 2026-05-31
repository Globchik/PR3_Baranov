package com.example.pr3_baranov

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.UUID

data class TodoItem(val id: String = UUID.randomUUID().toString(), val text: String, var isDone: Boolean = false)

@Composable
fun TodoTab() {
    var taskText by remember { mutableStateOf("") }
    val tasks = remember { mutableStateListOf<TodoItem>() }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = taskText,
                onValueChange = { taskText = it },
                label = { Text("Нове завдання") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (taskText.isNotBlank()) {
                    tasks.add(TodoItem(text = taskText))
                    taskText = ""
                }
            }) {
                Text("Додати")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(tasks) { task ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Checkbox(
                        checked = task.isDone,
                        onCheckedChange = { checked ->
                            val index = tasks.indexOf(task)
                            if (index != -1) {
                                tasks[index] = task.copy(isDone = checked)
                            }
                        }
                    )
                    Text(text = task.text)
                }
            }
        }
    }
}