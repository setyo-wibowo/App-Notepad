package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.NoteViewModel
import com.example.ui.screens.NoteDetailScreen
import com.example.ui.screens.NoteListScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SkeuoNoteApp()
            }
        }
    }
}

@Composable
fun SkeuoNoteApp() {
    val navController = rememberNavController()
    val viewModel: NoteViewModel = viewModel()

    NavHost(navController = navController, startDestination = "note_list") {
        composable("note_list") {
            NoteListScreen(
                viewModel = viewModel,
                onNoteClick = { note ->
                    navController.navigate("note_detail/${note.id}")
                },
                onAddClick = {
                    navController.navigate("note_detail/-1")
                }
            )
        }
        composable(
            route = "note_detail/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.LongType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId")
            NoteDetailScreen(
                viewModel = viewModel,
                noteId = if (noteId == -1L) null else noteId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
