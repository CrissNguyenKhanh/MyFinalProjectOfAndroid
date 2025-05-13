package com.example.retrofitwrooom.api

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
import retrofit2.http.Body
import retrofit2.http.DELETE

import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface benhNhanApi {

    @GET("/khanhdz/benhNhan/listBenhNhan")
    suspend fun getListBenhNhan(): Response<List<benhNhan>>

    @DELETE("/khanhdz/benhNhan/deleteBenhNhan/{maBenhNhan}")
    suspend fun  deleteBenhNhan(
        @Path("maBenhNhan") maBenhNhan: Long
    ) : Response<thongBao>
    @PUT("/khanhdz/benhNhan/editBenhNhan/{maBenhnhan}")
    suspend fun updateBenhNhan(
        @Path("maBenhnhan") maBenhnhan: Long,     // dùng @Path thay vì @Field cho ID trên URL
        @Body benhNhanUpdate: BenhNhanUpdate  // tên lớp gửi trong form
    ): Response<thongBao>
//    @POST("/khanhdz/benhNhan/addBenhNhan")
//    suspend fun addBenhNhan(
//        @Body benhnhanhbenhnhanRequest: benhnhanRequest
//    ):Response<thongBao>
    @POST("/khanhdz/benhNhan/findBenhNhan")
    suspend fun findBenhNhan(
        @Body benhnhanFind:benhnhanFind
    ):Response<List<benhNhan>>


    @Multipart
    @POST("/khanhdz/benhNhan/addBenhNhan")
    suspend fun addBenhNhan(
        @Part("patient") benhnhanRequestJson: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<thongBao>

    @GET("/khanhdz/benhNhan/countAll")
    suspend fun getAllBenhNhan(): Response<Long>


    @GET("/khanhdz/benhNhan/countDaKham")
    suspend fun getAllBenhNhanDaKham(): Response<Long>


    @GET("/khanhdz/benhNhan/countChuaKham")
    suspend fun getAllBenhNhanChuaKham(): Response<Long>

    @POST("/khanhdz/benhNhan/login")
    suspend fun login(@Body loginRequest: loginbenhnhanrequest): Response<thongBao>

    @POST("/khanhdz/benhNhan/findbenhNhanbyemail")
    suspend fun findBenhNhanbyemail(@Body benhnhanFinder: benhnhanFinder): Response<benhNhan>

    @POST("/khanhdz/benhNhan/findbenhnhanbydoctorid")
    suspend fun getListBenhNhanByDoctorId(
        @Body findBenhhnhanRequest: findBenhhnhanRequest
    ): Response<List<benhNhan>>

}