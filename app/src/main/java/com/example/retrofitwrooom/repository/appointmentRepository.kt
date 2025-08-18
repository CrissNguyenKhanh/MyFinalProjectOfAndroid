package com.example.retrofitwrooom.repository

import com.example.retrofitwrooom.api.RetrofitIntanceDoctor
import com.example.retrofitwrooom.model.appointment
import com.example.retrofitwrooom.model.request.appointmentDoctorId
import com.example.retrofitwrooom.model.request.appontmentRequest
import com.example.retrofitwrooom.model.request.benhAnRequest
import com.example.retrofitwrooom.model.thongBao
import retrofit2.Response

class appointmentRepository {

    suspend fun addCuocHen(appontmentRequest: appontmentRequest): Response<thongBao> {
        return RetrofitIntanceDoctor.apiCuochen.addApponitment(appontmentRequest)
    }

    suspend fun gettAllListappo(): Response<List<appointment>> {
        return RetrofitIntanceDoctor.apiCuochen.getAllAppointment()
    }

    suspend fun gettAllListappobydoctorId(doctor_id: Long): Response<List<appointment>> {
        val khanh = appointmentDoctorId(doctor_id)
        return RetrofitIntanceDoctor.apiCuochen.getAllAppointmentBydoctorid(khanh)
    }

    suspend fun getCountByDoctorId(doctorId: Long) =
        RetrofitIntanceDoctor.apiCuochen.getAppointmentCountByDoctorId(doctorId)
}