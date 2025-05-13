package com.example.retrofitwrooom.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitwrooom.model.benhNhan
import com.example.retrofitwrooom.model.doctor
import com.example.retrofitwrooom.model.request.benhnhanFind
import com.example.retrofitwrooom.model.request.benhnhanRequest
import com.example.retrofitwrooom.model.update.BenhNhanUpdate
import com.example.retrofitwrooom.repository.benhNhanRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

class benhNhanViewModel(val benhNhanRepository: benhNhanRepository) :ViewModel() {
     val responseData: MutableLiveData<Response<List<benhNhan>>> = MutableLiveData()
     val responseDatadoctorid: MutableLiveData<Response<List<benhNhan>>> = MutableLiveData()
     val responseDataFind: MutableLiveData<Response<List<benhNhan>>> = MutableLiveData()
     val allDocor:MutableLiveData<Response<List<doctor>>> = MutableLiveData()
     val loginSuccess = MutableLiveData<Boolean>()  // true = success, false = fail
     private val _benhNhanCount = MutableLiveData<Long>()
     val currentDoctor = MutableLiveData<benhNhan?>()
     val benhNhanCount: LiveData<Long> get() = _benhNhanCount

     private val _benhNhanCountDaKham = MutableLiveData<Long>()
     val benhNhanCountDaKham: LiveData<Long> get() = _benhNhanCountDaKham

     private val _benhNhanCountChuaKham = MutableLiveData<Long>()
     val benhNhanCountChuaKham: LiveData<Long> get() = _benhNhanCountChuaKham
     fun getPostItem() {
          viewModelScope.launch {
               responseData.value = benhNhanRepository.getListBenhNhan()
               Log.d("listBenhNhan", responseData.value.toString())
          }
     }

     fun getPostItembydoctorid(doctorid:Long) {
          viewModelScope.launch {
               responseDatadoctorid.value = benhNhanRepository.benhnhanfindbydocotr_id(doctorid)
               Log.d("listBenhNhan", responseDatadoctorid.value.toString())
          }
     }

     fun getPostItemDoctor(){
          viewModelScope.launch {
               allDocor.value = benhNhanRepository.getAllDoctor()
               Log.d("listBenhNhan" , allDocor.value.toString())
          }
     }

     fun getPostItemFind(benhnhanFind: benhnhanFind) {
          viewModelScope.launch {
               responseDataFind.value = benhNhanRepository.findBenhNhan(benhnhanFind)
               Log.d("listBenhNhan", responseDataFind.value.toString())
          }
     }


     fun deleteBenhNhan(maBenhNhan: Long) {
          viewModelScope.launch {
               try {
                    val response = benhNhanRepository.deleteBenhNhan(maBenhNhan)

                    if (response.isSuccessful) {
                         Log.d("deleteStatus", "Success: ")
                         getPostItem();
                         getBenhNhanCount()
                         getPostItembydoctorid(1)
                         getBenhNhanCountDaKham()
                         getBenhNhanCountChuaKham()
                    } else {
                         Log.d("deleteStatus", "Failed with code: ${response.code()}")
                    }
               } catch (e: Exception) {
                    Log.d("deleteStatus", "Exception: ${e.message}")
               }
          }
     }

     fun updateBenhNhan(maBenhNhan: Long, benhNhanUpdate: BenhNhanUpdate) {
          viewModelScope.launch {
               try {
                    val response = benhNhanRepository.updateBenhNhan(maBenhNhan, benhNhanUpdate)
                    if (response.isSuccessful) {
                         Log.d("update", "Success: ")
                         getPostItem();
                         getBenhNhanCountDaKham()
                         getPostItembydoctorid(1)
                         getBenhNhanCountChuaKham()
                    } else {
                         Log.d("update", "Failed with code: ${response.code()}")
                    }
               } catch (e: Exception) {
                    Log.d("update", "Exception: ${e.message}")
               }
          }

     }

     //     fun addBenhNhan( benhnhanRequest: benhnhanRequest){
//          viewModelScope.launch {
//               try {
//                    val response = benhNhanRepository.addBenhNhan( benhnhanRequest)
//                    if (response.isSuccessful) {
//                         Log.d("add", "Success: ")
//                         getPostItem();
//                    } else {
//                         Log.d("add", "Failed with code: ${response.code()}")
//                    }
//               } catch (e: Exception) {
//                    Log.d("add", "Exception: ${e.message}")
//               }
//          }
//
//     }
     fun findBenhNhan(benhnhanFind: benhnhanFind) {
          viewModelScope.launch {
               try {
                    val response = benhNhanRepository.findBenhNhan(benhnhanFind)
                    responseDataFind.value = response

                    if (response.isSuccessful) {
                         Log.d("find", "Success")
                    } else {
                         Log.d("find", "Failed with code: ${response.code()}")
                    }
               } catch (e: Exception) {
                    Log.d("find", "Exception: ${e.message}")
               }
          }
     }

     private val _addSuccess = MutableLiveData<Boolean>()
     val addSuccess: LiveData<Boolean> get() = _addSuccess

     fun addBenhNhan(benhnhan_Request: benhnhanRequest, imageFile: MultipartBody.Part) {
          viewModelScope.launch {
               try {
                    val gson = Gson()
                    val json = gson.toJson(benhnhan_Request)
                    val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                    val response = benhNhanRepository.addBenhNhan(requestBody, imageFile)

                    if (response.isSuccessful) {
                         _addSuccess.postValue(true)
                         getBenhNhanCount()
                         getBenhNhanCountDaKham()
                         getBenhNhanCountChuaKham()

                    } else {
                         Log.d("add", "Failed: ${response.code()} - ${response.message()}")
                         _addSuccess.postValue(false)
                    }
               } catch (e: Exception) {
                    Log.d("add", "Exception: ${e.message}")
                    _addSuccess.postValue(false)
               }
          }


     }

     fun getBenhNhanCount() {
          viewModelScope.launch {
               try {
                    val response = benhNhanRepository.getAllBenhNhan()
                    if (response.isSuccessful) {
                         response.body()?.let {
                              _benhNhanCount.value = it
                              Log.d("benhNhanCount", "Total: $it")
                         }
                    } else {
                         Log.d("benhNhanCount", "Failed: ${response.code()}")
                    }
               } catch (e: Exception) {
                    Log.d("benhNhanCount", "Exception: ${e.message}")
               }
          }
     }

     fun getBenhNhanCountDaKham() {
          viewModelScope.launch {
               try {
                    val response = benhNhanRepository.getAllBenhNhanDaKham()
                    if (response.isSuccessful) {
                         response.body()?.let {
                              _benhNhanCountDaKham.value = it
                              Log.d("benhNhanCount", "Total: $it")
                         }
                    } else {
                         Log.d("benhNhanCount", "Failed: ${response.code()}")
                    }
               } catch (e: Exception) {
                    Log.d("benhNhanCount", "Exception: ${e.message}")
               }
          }
     }

     fun getBenhNhanCountChuaKham() {
          viewModelScope.launch {
               try {
                    val response = benhNhanRepository.getAllBenhNhanChuakham()
                    if (response.isSuccessful) {
                         response.body()?.let {
                              _benhNhanCountChuaKham.value = it
                              Log.d("benhNhanCount", "Total: $it")
                         }
                    } else {
                         Log.d("benhNhanCount", "Failed: ${response.code()}")
                    }
               } catch (e: Exception) {
                    Log.d("benhNhanCount", "Exception: ${e.message}")
               }
          }
     }

     fun login(email: String, full_name: String) {
          viewModelScope.launch {
               try {
                    val response = benhNhanRepository.getLogin(email, full_name)
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
                    val response = benhNhanRepository.benhnhanfindByEmail(userName)
                    val benhnhan = response.body()
                    Log.d("benhnhan", benhnhan.toString())
                    if (response.isSuccessful && benhnhan != null) {
                         currentDoctor.postValue(benhnhan) // <- lưu lại doctor ở đây
                    } else {
                         currentDoctor.postValue(null)
                    }
               } catch (e: Exception) {
                    currentDoctor.postValue(null)
               }
          }

     }
}
