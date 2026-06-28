package com.example.data

import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao, private val categoryDao: CategoryDao) {
    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()
    val allCategories: Flow<List<Category>> = categoryDao.getAllCategories()

    suspend fun insertNote(note: Note) = noteDao.insertNote(note)
    suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)
    
    fun searchNotes(query: String) = noteDao.searchNotes(query)
    fun getNotesByCategory(catId: Long) = noteDao.getNotesByCategory(catId)

    suspend fun insertCategory(category: Category) = categoryDao.insertCategory(category)
    suspend fun deleteCategory(category: Category) = categoryDao.deleteCategory(category)
}
