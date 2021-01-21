package com.anupam.covid19app.data.entity

import androidx.room.*
import com.anupam.covid19app.converters.CountriesConverters
import com.google.gson.annotations.SerializedName

@Entity(tableName = "covid_cases", indices = [Index(value = ["global_newConfirmed"], unique = true)])
@TypeConverters(CountriesConverters::class)
data class CovidCases(
        @PrimaryKey(autoGenerate = true)
        var uuid: Int = 0,

        @SerializedName("Message")
        @ColumnInfo(name = "message") val message : String,

        @Embedded(prefix = "global_")
        @SerializedName("Global") val global : Global?,

        @SerializedName("Countries") val countries : List<Countries>?,

        @SerializedName("Date")
        @ColumnInfo(name = "date") val date : String
) {
}
