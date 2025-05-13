package com.example.retrofitwrooom.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class appointment(
    val id:Long,
    val appointment_date:String,
    val reason:String,
    val status:String,
    val doctor: doctor,
    val benhNhan: benhNhan,
): Parcelable

