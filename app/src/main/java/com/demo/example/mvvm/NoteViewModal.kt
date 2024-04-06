package com.demo.example.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.demo.example.room.Note
import com.demo.example.room.NoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModal(application: Application):AndroidViewModel(application) {

    var repository:NoteRepository
    val allNotes : LiveData<List<Note>>
    init {
        val dao = NoteDatabase.getDatabase(application).getNotesDao()
        repository = NoteRepository(dao)
        allNotes = repository.allNotes
    }
    fun addNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertNote(note)
        }
    }
    fun updateNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNote(note)
        }
    }
    fun deleteNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(note)
        }
    }



}