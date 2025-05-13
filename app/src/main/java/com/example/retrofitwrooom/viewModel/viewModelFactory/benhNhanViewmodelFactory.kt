package com.example.retrofitwrooom.viewModel.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitwrooom.repository.DoctorRepository
import com.example.retrofitwrooom.repository.benhNhanRepository
import com.example.retrofitwrooom.viewModel.benhNhanViewModel
import com.example.retrofitwrooom.viewModel.doctorViewModel

class benhNhanViewmodelFactory(val benhNhanRepository: benhNhanRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return benhNhanViewModel(benhNhanRepository) as T
    }
}