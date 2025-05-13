package com.example.retrofitwrooom.viewModel.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitwrooom.repository.appointmentRepository
import com.example.retrofitwrooom.viewModel.appointmentViewmodel

class appointmentViewmodelFacotry(val repository: appointmentRepository) :ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return appointmentViewmodel(repository)  as T

    }
}