package com.example.retrofitwrooom.database

import androidx.room.TypeConverter
import com.example.retrofitwrooom.model.Source
class Converters {
    // day la class co chuc nang chinh la chuyen cac kieu du lieu phuc tap thanh cac kieu du lieu ma room data hieu duoc

    //chuyen source thanh String
    @TypeConverter
    fun fromSource(source: Source):String{
         return source.name
    }

    //chuyen String thanh source
    @TypeConverter
    fun toSource(name:String):Source{
        return Source(name,name)
    }




}