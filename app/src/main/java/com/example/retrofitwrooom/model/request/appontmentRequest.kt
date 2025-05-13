package com.example.retrofitwrooom.model.request

data class appontmentRequest(
    val appointment_date:String,
    val reason:String,
    val  status:String,
    val  doctor_id:Long,
    val  patient_id:Long,
)
