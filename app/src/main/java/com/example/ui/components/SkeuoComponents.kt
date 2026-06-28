package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

@Composable
fun ModernSkeuoBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Zinc900)
    ) {
        content()
    }
}

@Composable
fun ModernSkeuoCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.Black,
                spotColor = Color.Black
            )
            .background(
                color = Color(0xFF1C1C1F),
                shape = RoundedCornerShape(24.dp)
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(Color.White.copy(alpha = 0.05f), Color.Transparent)
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp)
    ) {
        Column {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernSkeuoSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth(),
        color = Color(0xFF121214),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Zinc800.copy(alpha = 0.5f))
    ) {
        // Simulating inset shadow with a darker container and subtle inner border
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    // Top-left dark shadow (inner)
                    drawLine(
                        color = Color.Black.copy(alpha = 0.4f),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 4.dp.toPx()
                    )
                    drawLine(
                        color = Color.Black.copy(alpha = 0.4f),
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = 4.dp.toPx()
                    )
                },
            contentAlignment = Alignment.CenterStart
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(placeholder, style = TextStyle(color = Zinc600)) },
                leadingIcon = leadingIcon,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Zinc100,
                    unfocusedTextColor = Zinc100
                ),
                singleLine = true
            )
        }
    }
}

@Composable
fun ModernSkeuoFAB(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(64.dp)
            .shadow(
                elevation = 12.dp,
                shape = CircleShape,
                ambientColor = Indigo500.copy(alpha = 0.4f),
                spotColor = Indigo500.copy(alpha = 0.4f)
            )
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Indigo400, Indigo600)
                ),
                shape = CircleShape
            )
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // Highlighting the top edge for skeuomorphic depth
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.White.copy(alpha = 0.3f), Color.Transparent)
                    ),
                    shape = CircleShape
                )
        )
        icon()
    }
}

@Composable
fun ModernSkeuoChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    val containerColor = if (selected) Indigo500.copy(alpha = 0.1f) else Color(0xFF1C1C1F).copy(alpha = 0.5f)
    val borderColor = if (selected) Indigo500.copy(alpha = 0.3f) else Zinc700.copy(alpha = 0.3f)
    val textColor = if (selected) Indigo400 else Zinc500

    Surface(
        onClick = onClick,
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = Color.Black,
                spotColor = Color.Black
            ),
        color = containerColor,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelMedium.copy(
                color = textColor,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

// Keeping a simplified button for general use
@Composable
fun SkeuoButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModernSkeuoChip(selected = true, onClick = onClick, label = text, modifier = modifier)
}
