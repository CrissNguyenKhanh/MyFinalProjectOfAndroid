package com.example.retrofitwrooom.repository

import com.example.retrofitwrooom.api.RetrofitIntanceDoctor
import com.example.retrofitwrooom.model.benhNhan
import com.example.retrofitwrooom.model.doctor
import com.example.retrofitwrooom.model.request.benhnhanFind
import com.example.retrofitwrooom.model.request.benhnhanFinder
import com.example.retrofitwrooom.model.request.benhnhanRequest
import com.example.retrofitwrooom.model.request.doctorFind
import com.example.retrofitwrooom.model.request.findBenhhnhanRequest
import com.example.retrofitwrooom.model.request.loginRequest
import com.example.retrofitwrooom.model.request.loginbenhnhanrequest
import com.example.retrofitwrooom.model.thongBao
import com.example.retrofitwrooom.model.update.BenhNhanUpdate
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class benhNhanRepository() {


    suspend fun getLogin(email:String , full_name:String):Response<thongBao>{
        val request = loginbenhnhanrequest(email,full_name)
        return RetrofitIntanceDoctor.apiBenhNhan.login(request)
    }
    suspend fun getListBenhNhan(): Response<List<benhNhan>>{
        return  RetrofitIntanceDoctor.apiBenhNhan.getListBenhNhan()
    }
    suspend fun  deleteBenhNhan(maBenhNhan:Long) :Response<thongBao>{
        return RetrofitIntanceDoctor.apiBenhNhan.deleteBenhNhan(maBenhNhan)
    }
    suspend fun updateBenhNhan(maBenhNhan: Long , benhNhanUpdate: BenhNhanUpdate):Response<thongBao>{
        return RetrofitIntanceDoctor.apiBenhNhan.updateBenhNhan(maBenhNhan,benhNhanUpdate)
    }
//    suspend fun addBenhNhan(benhnhanRequest: benhnhanRequest):Response<thongBao>{
//        return RetrofitIntanceDoctor.apiBenhNhan.addBenhNhan(benhnhanRequest);
//    }
    suspend fun findBenhNhan(benhnhanFind: benhnhanFind):Response<List<benhNhan>>{
        return RetrofitIntanceDoctor.apiBenhNhan.findBenhNhan(benhnhanFind)
    }

   suspend fun addBenhNhan(benhnhanRequest: RequestBody, imageFile: MultipartBody.Part):Response<thongBao>{
        return RetrofitIntanceDoctor.apiBenhNhan.addBenhNhan(benhnhanRequest,imageFile);
    }

    suspend fun  getAllBenhNhan():Response<Long>{
        return RetrofitIntanceDoctor.apiBenhNhan.getAllBenhNhan();
    }

    suspend fun  getAllBenhNhanDaKham():Response<Long>{
        return RetrofitIntanceDoctor.apiBenhNhan.getAllBenhNhanDaKham();
    }
    suspend fun  getAllBenhNhanChuakham():Response<Long>{
        return RetrofitIntanceDoctor.apiBenhNhan.getAllBenhNhanChuaKham();
    }
    suspend fun  getAllDoctor():Response<List<doctor>>{
        return RetrofitIntanceDoctor.apiDoctor.getAllDoctor();
    }
    suspend fun  benhnhanfindByEmail(userName: String):Response<benhNhan>{
        val findRequest = benhnhanFinder(userName)
        return RetrofitIntanceDoctor.apiBenhNhan.findBenhNhanbyemail(findRequest)
    }

    suspend fun  benhnhanfindbydocotr_id(doctor_id:Long):Response<List<benhNhan>>{
        val  Request = findBenhhnhanRequest(doctor_id)
        return RetrofitIntanceDoctor.apiBenhNhan.getListBenhNhanByDoctorId(Request)
    }


}