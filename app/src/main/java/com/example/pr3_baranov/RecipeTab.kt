package com.example.pr3_baranov

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.gson.Gson

data class Recipe(val title: String, val ingredients: String, val instructions: String)

@Composable
fun RecipeTab() {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    val recipes = remember { mutableStateListOf<Recipe>() }
    val gson = Gson()

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Назва рецепту") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = ingredients, onValueChange = { ingredients = it }, label = { Text("Інгредієнти") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = instructions, onValueChange = { instructions = it }, label = { Text("Інструкція приготування") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (title.isNotBlank()) {
                    recipes.add(Recipe(title, ingredients, instructions))
                    title = ""
                    ingredients = ""
                    instructions = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Зберегти рецепт")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(recipes) { recipe ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = recipe.title, style = MaterialTheme.typography.titleMedium)
                        Text(text = "Інгредієнти: ${recipe.ingredients}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Інструкція: ${recipe.instructions}", style = MaterialTheme.typography.bodySmall)

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(onClick = {
                            val jsonRecipe = gson.toJson(recipe)
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, jsonRecipe)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Поділитися рецептом"))
                        }) {
                            Text("Поділитися як JSON")
                        }
                    }
                }
            }
        }
    }
}