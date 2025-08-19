package com.example.retrofitwrooom.api

import com.example.retrofitwrooom.model.Notification
import com.example.retrofitwrooom.model.appointment
import com.example.retrofitwrooom.model.request.appointmentDoctorId
import com.example.retrofitwrooom.model.request.appontmentRequest
import com.example.retrofitwrooom.model.request.benhAnRequest
import com.example.retrofitwrooom.model.thongBao
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface apponitmentApi {

    @POST("/khanhdz/appointment/addAppointment")
    suspend fun addApponitment(
        @Body Request: appontmentRequest
    ): Response<thongBao>


    @POST("/khanhdz/appointment/addAppointmentAccept")
    suspend fun addApponitmentAccept(
        @Body Request: appontmentRequest
    ): Response<thongBao>


    @DELETE("/khanhdz/appointment/deleteAppointMent/{appointmentId}")
    suspend fun deleteCuocHen(
        @Path("appointmentId") appointmentId: Long
    ): Response<thongBao>

    @DELETE("/khanhdz/appointment/deleteAppointMentAccept/{appointmentId}")
    suspend fun deleteCuocHen_Accept(
        @Path("appointmentId") appointmentId: Long
    ): Response<thongBao>


    @GET("/khanhdz/appointment/getAllApponitment")
    suspend fun getAllAppointment(): Response<List<appointment>>

    @POST("/khanhdz/appointment/gettAppointmentBydoctorId")
    suspend fun getAllAppointmentBydoctorid(
        @Body Request: appointmentDoctorId
    ): Response<List<appointment>>

    @POST("/khanhdz/appointment/gettAppointmentAcceptBydoctorId")
    suspend fun getAllAppointmentAcceptBydoctorid(
        @Body Request: appointmentDoctorId
    ): Response<List<appointment>>

    @GET("/khanhdz/appointment/count")
    suspend fun getAppointmentCountByDoctorId(
        @Query("doctorId") doctorId: Long
    ): Response<Int>

    @GET("/khanhdz/appointment/countAccept")
    suspend fun getAppointmentAcceptCountByDoctorId(
        @Query("doctorId") doctorId: Long
    ): Response<Int>
}