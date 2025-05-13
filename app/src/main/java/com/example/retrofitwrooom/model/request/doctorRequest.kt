package com.example.retrofitwrooom.model.request

import java.util.Date

data class doctorRequest(

    val username: String,
    val password: String,
    val fullName: String,
    val phoneNumber: String,
    val specialization:String,
    val email: String,
    val role: String,
    val image: String,
    val department_id:Int,
){

}
