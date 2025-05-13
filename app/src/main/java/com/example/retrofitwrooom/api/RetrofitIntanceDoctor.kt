package com.example.retrofitwrooom.api

import com.example.retrofitwrooom.util.Constants.Companion.BASE_URL
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitIntanceDoctor {

    companion object{
        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl("http://192.168.1.8:8081")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create()

        private val retrofit2 by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl("http://192.168.1.8:8081")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
        }
        val apiDoctor by lazy {
            retrofit.create(doctorApi::class.java)
        }
        val apiBenhAn by lazy {
            retrofit.create(benhAnApi::class.java)
        }
        val apiCuochen by lazy {
            retrofit2.create(apponitmentApi::class.java)
        }



        val apiBenhNhan by lazy {
            retrofit2.create(benhNhanApi::class.java)
        }


    }
}
