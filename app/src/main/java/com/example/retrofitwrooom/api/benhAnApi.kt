package com.example.retrofitwrooom.api

import com.example.retrofitwrooom.model.request.benhAnRequest
import com.example.retrofitwrooom.model.thongBao
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface benhAnApi {

    @POST("/khanhdz/benAn/addBenhAn")
    suspend fun addBenhNhan(
        @Body Request: benhAnRequest
    ): Response<thongBao>


}