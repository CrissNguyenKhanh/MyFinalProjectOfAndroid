package com.example.retrofitwrooom.repository

import com.example.retrofitwrooom.api.RetrofitIntanceDoctor
import com.example.retrofitwrooom.model.benhNhan
import com.example.retrofitwrooom.model.request.benhAnRequest
import com.example.retrofitwrooom.model.thongBao
import retrofit2.Response

class benhAnRepository {

    suspend fun addBenhAn(benhAnRequest: benhAnRequest): Response<thongBao> {
        return  RetrofitIntanceDoctor.apiBenhAn.addBenhNhan(benhAnRequest)
    }
}