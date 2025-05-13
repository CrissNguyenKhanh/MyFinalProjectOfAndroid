package com.example.retrofitwrooom.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitwrooom.repository.NewsRepository

class NewsViewmodelProvider(val app :Application , val newsRepository: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  NewsViewModel(app , newsRepository) as T
    }

}