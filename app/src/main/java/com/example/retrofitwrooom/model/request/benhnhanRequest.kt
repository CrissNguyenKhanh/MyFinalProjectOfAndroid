package com.example.retrofitwrooom.model.request

import java.util.Date

data class benhnhanRequest(
    val fullName: String,
    val dob: String,
    val address: String,
    val phoneNumber: String,
    val email: String,
    val doctorId: Long,
    val gender: String,
    val image:String,
    val description:String,
    val checkKham:Boolean
)