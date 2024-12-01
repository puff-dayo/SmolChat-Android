/*
 * Copyright (C) 2024 Shubham Panchal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shubham0204.smollmandroid.ui.screens.manage_tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.shubham0204.smollmandroid.data.LLMModel
import io.shubham0204.smollmandroid.ui.components.DialogTitleText
import io.shubham0204.smollmandroid.ui.screens.chat.SelectModelsList
import io.shubham0204.smollmandroid.ui.theme.AppFontFamily

@Composable
fun EditTaskDialog(viewModel: TasksViewModel) {
    val selectedTask by remember { viewModel.selectedTaskState }
    selectedTask?.let {task ->
        var taskName by remember { mutableStateOf(task.name) }
        var systemPrompt by remember { mutableStateOf(task.systemPrompt) }
        var selectedModel by remember { mutableStateOf(viewModel.modelsRepository.getModelFromId(task.modelId)) }
        var isModelListDialogVisible by remember { mutableStateOf(false) }
        val modelsList = viewModel.modelsRepository.getAvailableModelsList()
        LaunchedEffect(selectedTask) {
            taskName = task.name
            systemPrompt = task.systemPrompt
        }
        var showEditTaskDialog by remember { viewModel.showEditTaskDialogState }
        if (showEditTaskDialog) {
            Dialog(onDismissRequest = { showEditTaskDialog = false }) {
                Column(
                    modifier =
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    DialogTitleText(text = "Edit Task")
                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        colors =
                        TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        value = taskName,
                        onValueChange = { taskName = it },
                        label = { Text("Task Name", fontFamily = AppFontFamily) },
                        textStyle = TextStyle(fontFamily = AppFontFamily),
                        keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Words)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        colors =
                        TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        value = systemPrompt,
                        onValueChange = { systemPrompt = it },
                        label = { Text("System Prompt", fontFamily = AppFontFamily) },
                        textStyle = TextStyle(fontFamily = AppFontFamily),
                        keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .border(width = 1.dp, Color.DarkGray)
                            .clickable { isModelListDialogVisible = true }
                            .padding(8.dp),
                        text = if (selectedModel == null) "Select Model" else selectedModel!!.name,
                        fontFamily = AppFontFamily,
                    )

                    if (isModelListDialogVisible) {
                        SelectModelsList(
                            onDismissRequest = { isModelListDialogVisible = false },
                            modelsList = modelsList,
                            onModelListItemClick = { model ->
                                isModelListDialogVisible = false
                                selectedModel = model
                            },
                            onModelDeleteClick = { /* Not applicable, as showModelDeleteIcon is set to false */ },
                            showModelDeleteIcon = false,
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        enabled = taskName.isNotBlank() && systemPrompt.isNotBlank(),
                        onClick = {
                            viewModel.updateTask(
                                task.copy(
                                    name = taskName,
                                    systemPrompt = systemPrompt,
                                ),
                            )
                            showEditTaskDialog = false
                        },
                    ) {
                        Icon(Icons.Default.Done, contentDescription = "Update")
                        Text("Update", fontFamily = AppFontFamily)
                    }
                }
            }
        }
    }
}
