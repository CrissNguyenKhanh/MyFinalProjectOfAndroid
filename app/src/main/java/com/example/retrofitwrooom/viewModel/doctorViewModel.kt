package com.example.retrofitwrooom.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitwrooom.model.Department

import com.example.retrofitwrooom.model.doctor

import com.example.retrofitwrooom.model.request.doctorFilter
import com.example.retrofitwrooom.model.request.doctorRequest
import com.example.retrofitwrooom.repository.DoctorRepository

import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

class doctorViewModel(val doctorRepository: DoctorRepository) :ViewModel() {
    val responseData:MutableLiveData<Response<List<doctor>>> = MutableLiveData()
    val currentDoctor = MutableLiveData<doctor?>()
    val loginSuccess = MutableLiveData<Boolean>()  // true = success, false = fail
    val allDocor:MutableLiveData<Response<List<doctor>>> = MutableLiveData()
    val allDepartment:MutableLiveData<Response<List<Department>>> = MutableLiveData()
    val addSuccess = MutableLiveData<Boolean>()


    val responseDataKhoatim:MutableLiveData<Response<List<doctor>>> = MutableLiveData()
    val responseDataKhoarang:MutableLiveData<Response<List<doctor>>> = MutableLiveData()
    val responseDataKhoaphoi:MutableLiveData<Response<List<doctor>>> = MutableLiveData()
    val responseDataKhoanao:MutableLiveData<Response<List<doctor>>> = MutableLiveData()


    fun getPostItem(doctorFilter: doctorFilter){
        viewModelScope.launch {
            responseData.value = doctorRepository.getDoctorFilter(doctorFilter)
            Log.d("listBenhNhan" , responseData.value.toString())
        }
    }
    fun getPostItemnao( ){
        viewModelScope.launch {
            val khanh = doctorFilter(1)
            responseDataKhoanao.value = doctorRepository.getDoctorFilter(khanh)
            Log.d("listBenhNhan" , responseData.value.toString())
        }
    }
    fun getPostItemRang( ){
        viewModelScope.launch {
            val khanh = doctorFilter(2)
            responseDataKhoarang.value = doctorRepository.getDoctorFilter(khanh)
            Log.d("listBenhNhan" , responseData.value.toString())
        }
    }

    fun getPostItemtim( ){
        viewModelScope.launch {
            val khanh = doctorFilter(3)
            responseDataKhoatim.value = doctorRepository.getDoctorFilter(khanh)
            Log.d("listBenhNhan" , responseData.value.toString())
        }
    }
    fun getPostItemphoi( ){
        viewModelScope.launch {
            val khanh = doctorFilter(4)
            responseDataKhoaphoi.value = doctorRepository.getDoctorFilter(khanh)
            Log.d("listBenhNhan" , responseData.value.toString())
        }
    }


    fun getPostItemDoctor(){
        viewModelScope.launch {
            allDocor.value = doctorRepository.getAllDoctor()
            Log.d("listBenhNhan" , allDocor.value.toString())
        }
    }

    fun getpostItemDepartment(){
        viewModelScope.launch {
            allDepartment.value = doctorRepository.getAllListDepartMent()
            Log.d("listBenhNhan" , allDepartment.value.toString())
        }
    }
    fun login(userName: String, passWord: String) {
        viewModelScope.launch {
            try {
                val response = doctorRepository.getLogin(userName, passWord)
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse?.status == "ok") {
                        loginSuccess.postValue(true)
                    } else {
                        loginSuccess.postValue(false)
                    }
                } else {
                    loginSuccess.postValue(false)
                }
            } catch (e: Exception) {
                loginSuccess.postValue(false)
            }
        }
    }

    fun loginFind(userName: String) {
        viewModelScope.launch {
            try {
                val response = doctorRepository.docTorFind(userName)
                val doctor = response.body()
                Log.d("doctor", doctor.toString())
                if (response.isSuccessful && doctor != null) {
                    currentDoctor.postValue(doctor) // <- lưu lại doctor ở đây
                } else {
                    currentDoctor.postValue(null)
                }
            } catch (e: Exception) {
                currentDoctor.postValue(null)
            }
        }

    }

//    fun addDocTor( doctorRequest: doctorRequest){
//        viewModelScope.launch {
//            try {
//                val response = doctorRepository.addDoctor( doctorRequest)
//                if (response.isSuccessful) {
//                    Log.d("add", "Success: add thanh cong doctor mot su vi dai ")
////                        getPostItem();
//                } else {
//                    Log.d("add", "Failed with code: ${response.code()}")
//                }
//            } catch (e: Exception) {
//                Log.d("add", "Exception: ${e.message}")
//            }
//        }
//
//    }

    fun addDocTor(doctorRequest: doctorRequest, imageFile: MultipartBody.Part) {
        viewModelScope.launch {
            try {
                // Chuyển doctorRequest thành JSON
                val gson = Gson()
                val json = gson.toJson(doctorRequest)
                val doctorRequestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                // Gọi repository để thêm bác sĩ
                val response = doctorRepository.addDoctor(doctorRequestBody, imageFile)

                // Kiểm tra phản hồi từ server
                if (response.isSuccessful) {
                    Log.d("add", "Success: Thêm bác sĩ thành công")
                    val khanh = doctorFilter(doctorRequest.department_id)
                    getPostItem(khanh)
                    addSuccess.postValue(true)
                } else {
                    Log.d("add", "Failed: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                // Log lỗi nếu có ngoại lệ
                Log.d("add", "Exception: ${e.message}")
            }
        }
    }








}




