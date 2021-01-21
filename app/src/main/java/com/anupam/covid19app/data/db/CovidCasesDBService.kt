package com.anupam.covid19app.data.db

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.anupam.covid19app.data.entity.CovidCases
import com.anupam.covid19app.retrofit.GetCovidDataService
import com.anupam.covid19app.retrofit.RetrofitClientInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.lang.Exception

class CovidCasesDBService(application: Application) {

    private val application = application

    internal suspend fun fetchCovidCases() {

        withContext(Dispatchers.IO) {
            val service = RetrofitClientInstance.retrofitInstance?.create(GetCovidDataService::class.java)
            val cases = async { service?.getAllCovidCases() }

            updateLocalDB(cases.await())
        }
    }

     suspend fun updateLocalDB(covidCases: CovidCases?) {
        try {
            val localDB = getLocalCasesDAO()
            localDB.insertAll(covidCases!!)
        }catch (e: Exception) {
            Log.d("Error: ", "db: ${e.printStackTrace()}")
        }
    }

    internal fun getLocalCasesDAO(): CovidCasesDAO {
        val db = Room.databaseBuilder(application, CovidCasesDB::class.java, "covidCases.db")
                .fallbackToDestructiveMigration()
                .build()
        return db.covidCasesDAO()

    }
}