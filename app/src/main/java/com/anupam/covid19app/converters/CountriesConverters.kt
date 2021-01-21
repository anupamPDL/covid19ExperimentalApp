package com.anupam.covid19app.converters

import androidx.room.TypeConverter
import com.anupam.covid19app.data.entity.Countries
import com.google.gson.Gson

class CountriesConverters {

    @TypeConverter
    fun listToJson(value: List<Countries>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<Countries>? {
        val objects = Gson().fromJson(value, Array<Countries>::class.java) as Array<Countries>
        val list = objects.toList()
        return list
    }

}