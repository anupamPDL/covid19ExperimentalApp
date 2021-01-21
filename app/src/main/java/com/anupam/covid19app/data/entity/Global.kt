package com.anupam.covid19app.data.entity

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Global(

        @SerializedName("NewConfirmed")
        @ColumnInfo(name = "newConfirmed") val newConfirmed : Int,

        @SerializedName("TotalConfirmed")
        @ColumnInfo(name = "totalConfirmed") val totalConfirmed : Int,

        @SerializedName("NewDeaths")
        @ColumnInfo(name = "newDeaths") val newDeaths : Int,

        @SerializedName("TotalDeaths")
        @ColumnInfo(name = "totalDeaths") val totalDeaths : Int,

        @SerializedName("NewRecovered")
        @ColumnInfo(name = "newRecovered") val newRecovered : Int,

        @SerializedName("TotalRecovered")
        @ColumnInfo(name = "totalRecovered") val totalRecovered : Int
) {
}