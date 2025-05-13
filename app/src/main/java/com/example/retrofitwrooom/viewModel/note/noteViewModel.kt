package com.example.retrofitwrooom.viewModel.note

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.example.retrofitwrooom.model.note.Note
import com.example.retrofitwrooom.repository.noteRepository
import kotlinx.coroutines.launch

class noteViewModel(app:Application , private val noteRepository: noteRepository) :AndroidViewModel(app) {

    fun addNote(note :Note) =
        viewModelScope.launch {
            noteRepository.insertNote(note)
        }

    fun DeleteNote(note :Note) =
        viewModelScope.launch {
            noteRepository.DeleteNote(note)
        }
    fun UpdateNote(note :Note) =
        viewModelScope.launch {
            noteRepository.UpdateNote(note)
        }

    fun getAllNotes() = noteRepository.getAllNotes()

    fun searchNote(query: String?) = noteRepository.searchNotes(query)
}