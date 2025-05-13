package com.example.retrofitwrooom.viewModel.note

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitwrooom.repository.noteRepository

class noteViewmodelProvider(val app:Application , private val noteRepository: noteRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return noteViewModel(app,noteRepository) as T
    }
}