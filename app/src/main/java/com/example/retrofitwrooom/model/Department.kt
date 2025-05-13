package com.example.retrofitwrooom.model

import android.os.Parcelable
import kotlinx.parcelize.*


@Parcelize
data class Department(
    val id: Long,
    val name: String,
    val description: String?,
    val createdAt: String?,
    val updatedAt: String?
) : Parcelable