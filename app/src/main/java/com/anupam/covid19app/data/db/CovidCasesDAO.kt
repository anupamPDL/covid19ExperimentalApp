package com.anupam.covid19app.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anupam.covid19app.data.entity.CovidCases

@Dao
interface CovidCasesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cases: CovidCases)

    @Query("select * from covid_cases")
    fun getAllCases(): LiveData<CovidCases>

    @Query("delete from covid_cases")
    fun deleteData()

}