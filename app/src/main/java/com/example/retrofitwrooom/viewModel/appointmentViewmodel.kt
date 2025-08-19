package com.example.retrofitwrooom.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitwrooom.model.appointment
import com.example.retrofitwrooom.model.benhNhan
import com.example.retrofitwrooom.model.request.appontmentRequest
import com.example.retrofitwrooom.model.request.benhAnRequest
import com.example.retrofitwrooom.repository.appointmentRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import kotlin.system.exitProcess

class appointmentViewmodel(val appointment_Repository: appointmentRepository) : ViewModel() {
    val responseData: MutableLiveData<Response<List<appointment>>> = MutableLiveData()
    val responseDataDoctorid: MutableLiveData<Response<List<appointment>>> = MutableLiveData()

    val responseData_Accept: MutableLiveData<Response<List<appointment>>> = MutableLiveData()
    val responseDataDoctorid_Accept: MutableLiveData<Response<List<appointment>>> =
        MutableLiveData()

    val responseDataCountDoctorByid: MutableLiveData<Response<Int>> = MutableLiveData()
    val responseDataCountDoctorByid_Accept: MutableLiveData<Response<Int>> = MutableLiveData()


    fun getPostItem() {
        viewModelScope.launch {
            responseData.value = appointment_Repository.gettAllListappo()
            Log.d("listBenhNhan", responseData.value.toString())
        }
    }

    fun getPostItemBydoctorId(doctor_id: Long) {
        viewModelScope.launch {
            responseDataDoctorid.value = appointment_Repository.gettAllListappobydoctorId(doctor_id)
            Log.d("listBenhNhandoctorid", responseDataDoctorid.value.toString())
        }
    }

    fun getPostItemBydoctorId_Accept(doctor_id: Long) {
        viewModelScope.launch {
            responseDataDoctorid_Accept.value =
                appointment_Repository.gettAllListappoAcceptbydoctorId(doctor_id)
            Log.d("listBenhNhandoctorid", responseDataDoctorid_Accept.value.toString())
        }
    }

    fun addCuochen(request: appontmentRequest) {
        viewModelScope.launch {
            try {
                val khanhadd = appointment_Repository.addCuocHen(request)
                if (khanhadd.isSuccessful) {
                    Log.d("cuochen", "Addd cuochen thanhcong")
                    getPostItem()
                    getPostItemBydoctorId(1)
                } else {

                    Log.d("cuochen", "Addd benh an that bai")

                }

            } catch (e: Exception) {
                Log.d("cuochen", "looi he thong ${e.message}")

            }
        }
    }

    fun addCuochenDaAccept(request: appontmentRequest) {
        viewModelScope.launch {
            try {
                val khanhadd = appointment_Repository.addCuocHenAccept(request)
                if (khanhadd.isSuccessful) {
                    Log.d("cuochen", "Addd cuochen thanhcong")
                    getPostItem()
                    getPostItemBydoctorId(request.doctor_id)
                    getPostItemBydoctorId_Accept(request.doctor_id)

                } else {

                    Log.d("cuochen", "Addd benh an that bai")

                }

            } catch (e: Exception) {
                Log.d("cuochen", "looi he thong ${e.message}")

            }
        }
    }

    fun deleteAppointment(appointmentId: Long, doctor_id: Long) {
        viewModelScope.launch {
            try {
                val response = appointment_Repository.deleteCuocHen(appointmentId)

                if (response.isSuccessful) {
                    Log.d("deleteStatus", "Success: ")
                    getPostItem();
                    getPostItemBydoctorId(doctor_id)
                    getPostItemBydoctorId_Accept(doctor_id)

                } else {
                    Log.d("deleteStatus", "Failed with code: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.d("deleteStatus", "Exception: ${e.message}")
            }
        }
    }

    fun deleteAppointment_Accept(appointmentId: Long, doctor_id: Long) {
        viewModelScope.launch {
            try {
                val response = appointment_Repository.deleteCuocHen_Accept(appointmentId)

                if (response.isSuccessful) {
                    Log.d("deleteStatus", "Success: ")
                    getPostItem();
                    getPostItemBydoctorId(doctor_id)
                    getPostItemBydoctorId_Accept(doctor_id)

                } else {
                    Log.d("deleteStatus", "Failed with code: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.d("deleteStatus", "Exception: ${e.message}")
            }
        }
    }


    fun getCountDoctorById(doctor_id: Long) {
        viewModelScope.launch {
            try {
                val countResponse = appointment_Repository.getCountByDoctorId(doctor_id)
                if (countResponse.isSuccessful) {
                    responseDataCountDoctorByid.value = countResponse
                    Log.d("cuochenCount", "So appointment = ${countResponse.body()}")
                } else {
                    Log.d("cuochenCount", "Lay Count That Bai")
                }
            } catch (e: Exception) {
                Log.d("cuochenCount", "loi he thong ${e.message}")
            }
        }
    }

    fun getCountDoctorById_Accept(doctor_id: Long) {
        viewModelScope.launch {
            try {
                val countResponse = appointment_Repository.getCountAcceptByDoctorId(doctor_id)
                if (countResponse.isSuccessful) {
                    responseDataCountDoctorByid_Accept.value = countResponse
                    Log.d("cuochenCount", "So appointment = ${countResponse.body()}")
                } else {
                    Log.d("cuochenCount", "Lay Count That Bai")
                }
            } catch (e: Exception) {
                Log.d("cuochenCount", "loi he thong ${e.message}")
            }
        }
    }
}

