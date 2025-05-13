package com.example.retrofitwrooom.model.update

import java.util.Date

data class BenhNhanUpdate(
    val fullName: String,
    val dob: Date,
    val address: String,
    val phoneNumber: String,
    val email: String,
    val doctorId: Long,
    val gender: String,
    val checkKham:Boolean
)
