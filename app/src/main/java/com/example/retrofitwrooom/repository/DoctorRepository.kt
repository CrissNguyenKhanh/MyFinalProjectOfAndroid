package com.example.retrofitwrooom.repository


import com.example.retrofitwrooom.api.RetrofitIntanceDoctor
import com.example.retrofitwrooom.model.Department
import com.example.retrofitwrooom.model.doctor
import com.example.retrofitwrooom.model.request.doctorFilter
import com.example.retrofitwrooom.model.request.doctorFind
import com.example.retrofitwrooom.model.request.loginRequest
import com.example.retrofitwrooom.model.thongBao
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

class DoctorRepository() {

  suspend fun getLogin(userName:String , passWord:String):Response<thongBao>{
      val request = loginRequest(userName,passWord)
      return RetrofitIntanceDoctor.apiDoctor.login(request)
  }
   suspend fun  docTorFind(userName: String):Response<doctor>{
       val findRequest = doctorFind(userName)
       return RetrofitIntanceDoctor.apiDoctor.findUser(findRequest)
   }
//
//    suspend fun  addDoctor(doctorRequest: doctorRequest):Response<thongBao>{
//        return RetrofitIntanceDoctor.apiDoctor.addDocTor(doctorRequest)
//    }
    suspend fun addDoctor(doctorRequest: RequestBody, imageFile: MultipartBody.Part): Response<thongBao> {
        return RetrofitIntanceDoctor.apiDoctor.addDoctor(doctorRequest, imageFile)
    }
    suspend fun getDoctorFilter(doctorFilter: doctorFilter):Response<List<doctor>>{
        return  RetrofitIntanceDoctor.apiDoctor.getDoctorFilter(doctorFilter)
    }
    suspend fun  getAllDoctor():Response<List<doctor>>{
        return RetrofitIntanceDoctor.apiDoctor.getAllDoctor();
    }
    suspend fun  getAllListDepartMent():Response<List<Department>>{
        return RetrofitIntanceDoctor.apiDoctor.getAllDepartment();
    }

}