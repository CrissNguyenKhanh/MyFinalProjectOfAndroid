package com.example.retrofitwrooom.viewModel.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitwrooom.repository.DoctorRepository
import com.example.retrofitwrooom.viewModel.doctorViewModel

class doctorViewmodelFactory(val doctorRepository: DoctorRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return doctorViewModel(doctorRepository) as T
    }
}