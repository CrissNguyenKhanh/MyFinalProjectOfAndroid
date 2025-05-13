package com.example.retrofitwrooom.viewModel.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitwrooom.repository.benhAnRepository
import com.example.retrofitwrooom.viewModel.benhanViewModel

class benhanviewmodelFactory(val benhAnRepository: benhAnRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  benhanViewModel(benhAnRepository) as T
    }
}