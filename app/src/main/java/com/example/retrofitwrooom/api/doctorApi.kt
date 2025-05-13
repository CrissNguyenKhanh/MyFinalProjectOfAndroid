package com.example.retrofitwrooom.api

import com.example.retrofitwrooom.model.Department
import com.example.retrofitwrooom.model.NewResponse
import com.example.retrofitwrooom.model.doctor
import com.example.retrofitwrooom.model.request.doctorFilter
import com.example.retrofitwrooom.model.request.doctorFind
import com.example.retrofitwrooom.model.request.doctorRequest
import com.example.retrofitwrooom.model.request.loginRequest
import com.example.retrofitwrooom.model.thongBao
import com.example.retrofitwrooom.util.Constants.Companion.API_KEY
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface doctorApi {

    @POST("/khanhdz/doctor/login")
    suspend fun login(@Body loginRequest: loginRequest): Response<thongBao>

    //lay ca nhan doctor ra thoi
    @POST("/khanhdz/doctor/findDoctor")
    suspend fun findUser(@Body doctorFind: doctorFind): Response<doctor>

    @Multipart
    @POST("/khanhdz/doctor/addDoctor")
    suspend fun addDoctor(
        @Part("doctor") doctorRequestJson: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<thongBao>


    @POST("/khanhdz/doctor/getDoctorById")
    suspend fun getDoctorFilter(@Body doctorFilter: doctorFilter):Response<List<doctor>>

    @GET("/khanhdz/doctor/getAllDoctor")
    suspend fun getAllDoctor(): Response<List<doctor>>

    @GET("/khanhdz/doctor/getAllListDepartment")
    suspend fun getAllDepartment(): Response<List<Department>>



}