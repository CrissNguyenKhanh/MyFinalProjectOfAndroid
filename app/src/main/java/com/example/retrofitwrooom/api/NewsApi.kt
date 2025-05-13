package com.example.retrofitwrooom.api

import com.example.retrofitwrooom.model.NewResponse
import com.example.retrofitwrooom.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
 //thuc hien query co ban
//    @GET("v2/top-headlines")
//    suspend fun getHeadLines(
//        @Query("country")
//        countryCode: String ="us",
//        @Query("page")
//        pageNumber:Int = 1,
//        @Query("apikey")
//        apikey:String = API_KEY
//
//    ):Response<NewResponse>
//
//    @GET("v2/everything")
//    suspend fun getHeadLines(
//        @Query("q")
//        searchQuery :String ,
//        @Query("page")
//        pageNumber: Int = 1,
//        @Query("apikey")
//        apikey:String = API_KEY
//
//    ):Response<NewResponse>


    @GET("v2/everything")
    suspend fun getHeadLines(
        @Query("q")
        searchQuery :String ,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apikey")
        apikey:String = API_KEY

    ):Response<NewResponse>

    @GET("v2/everything")
    suspend fun SerachForNews(
        @Query("q")
        searchQuery :String ,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apikey")
        apikey:String = API_KEY
    ):Response<NewResponse>


}