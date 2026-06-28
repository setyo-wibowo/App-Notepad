package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Category
import com.example.data.Note
import com.example.ui.NoteViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    viewModel: NoteViewModel,
    onNoteClick: (Note) -> Unit,
    onAddClick: () -> Unit
) {
    val notes by viewModel.notes.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCatId by viewModel.selectedCategoryId.collectAsState()

    ModernSkeuoBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.statusBars.asPaddingValues())
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .shadow(4.dp, RoundedCornerShape(12.dp))
                            .background(Zinc800, RoundedCornerShape(12.dp))
                            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = Indigo400, modifier = Modifier.size(20.dp))
                    }
                    Text(
                        text = "SkeuoNote",
                        style = MaterialTheme.typography.titleLarge,
                        color = Zinc100,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Icon(Icons.Default.CloudDone, contentDescription = "Sync", tint = Zinc500, modifier = Modifier.size(20.dp))
            }

            // Search Bar
            ModernSkeuoSearchField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                placeholder = "Search encrypted notes...",
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Zinc600) },
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Categories
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    ModernSkeuoChip(
                        selected = selectedCatId == null,
                        onClick = { viewModel.onCategorySelected(null) },
                        label = "All"
                    )
                }
                items(categories) { category ->
                    ModernSkeuoChip(
                        selected = selectedCatId == category.id,
                        onClick = { viewModel.onCategorySelected(category.id) },
                        label = category.name
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Notes List
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(notes) { note ->
                    NoteItem(note = note, onClick = { onNoteClick(note) })
                }
            }
        }

        // FAB
        ModernSkeuoFAB(
            onClick = onAddClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .navigationBarsPadding(),
            icon = { Icon(Icons.Default.Add, contentDescription = "Add Note", tint = Color.White, modifier = Modifier.size(32.dp)) }
        )
    }
}

@Composable
fun NoteItem(note: Note, onClick: () -> Unit) {
    ModernSkeuoCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = Zinc100
            )
            Icon(Icons.Default.Lock, contentDescription = "Encrypted", tint = Zinc600, modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Modified: ${java.text.SimpleDateFormat("hh:mm a").format(java.util.Date(note.lastModified))}",
                style = MaterialTheme.typography.labelSmall,
                color = Zinc600,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )
            Surface(
                color = Zinc950,
                shape = RoundedCornerShape(4.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Zinc800)
            ) {
                Text(
                    text = "Personal", // Ideally fetch from categories
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = Zinc500
                )
            }
        }
    }
}
