package com.anupam.covid19app.data.entity

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Countries(

        @SerializedName("Country")
        @ColumnInfo(name = "country") val country : String,

        @SerializedName("CountryCode")
        @ColumnInfo(name = "countryCode") val countryCode : String,

        @SerializedName("Slug")
        @ColumnInfo(name = "slug") val slug : String,

        @SerializedName("NewConfirmed")
        @ColumnInfo(name = "new_confirmed") val newConfirmed : Int,

        @SerializedName("TotalConfirmed")
        @ColumnInfo(name = "totalConfirmed") val totalConfirmed : Int,

        @SerializedName("NewDeaths")
        @ColumnInfo(name = "newDeaths") val newDeaths : Int,

        @SerializedName("TotalDeaths")
        @ColumnInfo(name = "totalDeaths") val totalDeaths : Int,

        @SerializedName("NewRecovered")
        @ColumnInfo(name = "newRecovered") val newRecovered : Int,

        @SerializedName("TotalRecovered")
        @ColumnInfo(name = "totalRecovered") val totalRecovered : Int,

        @SerializedName("Date")
        @ColumnInfo(name = "date") val date : String
) {

        class CompareTotalConfirmed: Comparator<Countries>{
                override fun compare(o1: Countries?, o2: Countries?): Int {
                        if(o1 == null || o2 == null){
                                return 0
                        }
                        return o2.totalConfirmed.compareTo(o1.totalConfirmed)
                }
        }

        class CompareTotalRecovered: Comparator<Countries>{
                override fun compare(o1: Countries?, o2: Countries?): Int {
                        if(o1 == null || o2 == null){
                                return 0
                        }
                        return o2.totalRecovered.compareTo(o1.totalRecovered)
                }
        }

        class CompareTotalDeaths: Comparator<Countries>{
                override fun compare(o1: Countries?, o2: Countries?): Int {
                        if(o1 == null || o2 == null){
                                return 0
                        }
                        return o2.totalDeaths.compareTo(o1.totalDeaths)
                }
        }
}