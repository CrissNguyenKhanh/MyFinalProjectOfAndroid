package com.example.retrofitwrooom.repository

import androidx.room.Query
import com.example.retrofitwrooom.database.noteDatabase
import com.example.retrofitwrooom.model.note.Note

class noteRepository(private val db: noteDatabase) {
     suspend fun  insertNote(note : Note) = db.getNoteDao().InsertNote(note)
     suspend fun  DeleteNote(note : Note) = db.getNoteDao().deleteNote(note)
     suspend fun  UpdateNote(note : Note) = db.getNoteDao().updateNote(note)

    fun getAllNotes() = db.getNoteDao().getAllNotes()
    fun searchNotes(query: String?) = db.getNoteDao().searchNote(query)


}