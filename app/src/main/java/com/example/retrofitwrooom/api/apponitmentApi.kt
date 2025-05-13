package com.example.retrofitwrooom.api

import com.example.retrofitwrooom.model.Notification
import com.example.retrofitwrooom.model.appointment
import com.example.retrofitwrooom.model.request.appointmentDoctorId
import com.example.retrofitwrooom.model.request.appontmentRequest
import com.example.retrofitwrooom.model.request.benhAnRequest
import com.example.retrofitwrooom.model.thongBao
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface apponitmentApi {

    @POST("/khanhdz/appointment/addAppointment")
    suspend fun addApponitment(
        @Body Request: appontmentRequest
    ): Response<thongBao>

    @GET("/khanhdz/appointment/getAllApponitment")
    suspend fun getAllAppointment():Response<List<appointment>>

    @POST("/khanhdz/appointment/gettAppointmentBydoctorId")
    suspend fun getAllAppointmentBydoctorid(
        @Body Request: appointmentDoctorId
    ): Response<List<appointment>>

}