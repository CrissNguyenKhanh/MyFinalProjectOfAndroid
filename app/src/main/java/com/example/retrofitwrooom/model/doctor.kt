package com.example.retrofitwrooom.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.io.Serializable

@Parcelize
data class doctor(
    val id: Long,
    val username: String,
    val password: String,
    val fullName: String?,
    val email: String?,
    val phoneNumber: String?,
    val specialization: String?,
    val role: String?,
    val department: Department?,     // Đã là Parcelable
    val createdAt: String?,
    val updatedAt: String?,
    val image:String
) : Parcelable