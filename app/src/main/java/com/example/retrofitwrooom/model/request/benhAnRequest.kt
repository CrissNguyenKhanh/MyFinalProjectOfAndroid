package com.example.retrofitwrooom.model.request

data class benhAnRequest(
    val hoTenBenhNhan: String,
    val moTaBenh: String,
    val doctor_id: Long,
    val tenBacSi: String ,
    val benhnhan_id: Long,
    val khoa_id:Long
)