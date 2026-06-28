package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.util.EncryptionManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow<Long?>(null)
    val selectedCategoryId = _selectedCategoryId.asStateFlow()

    val categories: StateFlow<List<Category>>
    val notes: StateFlow<List<Note>>

    init {
        val db = AppDatabase.getDatabase(application)
        repository = NoteRepository(db.noteDao(), db.categoryDao())
        
        categories = repository.allCategories.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
        )
        
        notes = combine(repository.allNotes, _searchQuery, _selectedCategoryId) { notes, query, catId ->
            notes.filter { note ->
                val matchesQuery = query.isEmpty() || note.title.contains(query, ignoreCase = true)
                val matchesCat = catId == null || note.categoryId == catId
                matchesQuery && matchesCat
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        // Initial categories
        viewModelScope.launch {
            if (categories.value.isEmpty()) {
                repository.insertCategory(Category(name = "Personal", color = 0xFF4CAF50.toInt()))
                repository.insertCategory(Category(name = "Work", color = 0xFF2196F3.toInt()))
                repository.insertCategory(Category(name = "Ideas", color = 0xFFFFC107.toInt()))
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelected(catId: Long?) {
        _selectedCategoryId.value = catId
    }

    fun saveNote(title: String, content: String, categoryId: Long? = null) {
        viewModelScope.launch {
            val encrypted = EncryptionManager.encrypt(content)
            val note = Note(
                title = title,
                encryptedContent = encrypted.ciphertext,
                iv = encrypted.iv,
                categoryId = categoryId
            )
            repository.insertNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }
    
    fun decryptContent(note: Note): String {
        return EncryptionManager.decrypt(note.encryptedContent, note.iv)
    }
}
