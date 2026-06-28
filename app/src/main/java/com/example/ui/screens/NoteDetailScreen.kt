package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.NoteViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    viewModel: NoteViewModel,
    noteId: Long?,
    onBack: () -> Unit
) {
    val notes by viewModel.notes.collectAsState()
    val categories by viewModel.categories.collectAsState()
    
    val existingNote = notes.find { it.id == noteId }
    
    var title by remember { mutableStateOf(existingNote?.title ?: "") }
    var content by remember { mutableStateOf(existingNote?.let { viewModel.decryptContent(it) } ?: "") }
    var selectedCatId by remember { mutableStateOf(existingNote?.categoryId) }

    ModernSkeuoBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.statusBars.asPaddingValues())
        ) {
            TopAppBar(
                title = { Text(if (noteId == null) "New Note" else "Edit Note") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.saveNote(title, content, selectedCatId)
                        onBack()
                    }) {
                        Icon(Icons.Default.Save, contentDescription = "Save", tint = Indigo400)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Zinc100,
                    navigationIconContentColor = Zinc100,
                    actionIconContentColor = Zinc100
                )
            )

            ModernSkeuoCard(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .weight(1f)
            ) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Untitled Note", color = Zinc600) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Zinc100,
                        unfocusedTextColor = Zinc100
                    ),
                    textStyle = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Zinc800)

                TextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = { Text("Start typing your encrypted note...", color = Zinc600) },
                    modifier = Modifier.fillMaxSize(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Zinc400,
                        unfocusedTextColor = Zinc400
                    )
                )
            }
            
            // Category Selector
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding()
            ) {
                ModernSkeuoCard(modifier = Modifier.fillMaxWidth()) {
                    Text("Select Category", color = Zinc100, style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(categories) { category ->
                            ModernSkeuoChip(
                                selected = selectedCatId == category.id,
                                onClick = { selectedCatId = category.id },
                                label = category.name
                            )
                        }
                    }
                }
            }
        }
    }
}
