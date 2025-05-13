package com.example.retrofitwrooom.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.retrofitwrooom.model.note.Note

@Database(entities = [Note::class] , version =  1)
 abstract  class noteDatabase :RoomDatabase() {

     abstract  fun getNoteDao():NoteDao

    companion object{
        @Volatile
        private var instance:noteDatabase? =null
        private var LOCK = Any()

        operator  fun  invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }

        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                noteDatabase::class.java,
                "note_db"
            )
                .build()

    }
}