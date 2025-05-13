package com.example.retrofitwrooom.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class benhNhan(
    val id:Long,
    val fullName: String,
    val dob: Date,
    val address: String,
    val phoneNumber: String,
    val email: String,
    val gender:String,
    val doctor: doctor,
    val image:String,
    val description:String,
    val checkKham:Boolean
): Parcelable
