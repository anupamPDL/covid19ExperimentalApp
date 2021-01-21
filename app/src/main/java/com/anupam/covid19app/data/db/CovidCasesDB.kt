package com.anupam.covid19app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anupam.covid19app.data.entity.CovidCases

@Database(entities = [CovidCases::class], version = 1)
abstract class CovidCasesDB: RoomDatabase() {
    abstract fun covidCasesDAO(): CovidCasesDAO

}