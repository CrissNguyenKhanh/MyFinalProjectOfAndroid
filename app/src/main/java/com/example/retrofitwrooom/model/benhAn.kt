package com.example.retrofitwrooom.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class benhAn(
    val id: Long,
    val hoTenBenhNhan: String,
    val moTaBenh: String?,
    val tenBacSi: String,
    val doctor: doctor,
    val benhNhan: benhNhan,
    val department: Department
): Parcelable
