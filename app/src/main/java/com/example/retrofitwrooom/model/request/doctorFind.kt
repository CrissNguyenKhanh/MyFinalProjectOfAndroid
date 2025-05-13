package com.example.retrofitwrooom.model.request

import com.google.gson.annotations.SerializedName

data class doctorFind(
    @SerializedName("username")
    val userName:String
)
