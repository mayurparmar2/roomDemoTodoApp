package com.demo.example.mvvm

import androidx.lifecycle.LiveData
import com.demo.example.room.Note
import com.demo.example.room.NotesDao

class NoteRepository(private val notesDao: NotesDao) {

    val allNotes :LiveData<List<Note>> = notesDao.getAllNotes()

    suspend fun insertNote(note:Note){
        notesDao.insert(note)
    }
    suspend fun updateNote(note:Note){
        notesDao.update(note)
    }

    suspend fun delete(note:Note){
        notesDao.delete(note)
    }

}