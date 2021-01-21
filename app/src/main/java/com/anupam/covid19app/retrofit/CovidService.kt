package com.anupam.covid19app.retrofit

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.anupam.covid19app.data.db.CovidCasesDBService
import com.anupam.covid19app.data.entity.ConfirmedLiveCasesByCountry
import com.anupam.covid19app.data.entity.CovidCases
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CovidService {

    //gets all confirmed cases by country name
    fun fetchConfirmedLiveCasesByCountry(countryName: String): MutableLiveData<ArrayList<ConfirmedLiveCasesByCountry>> {
        val result = MutableLiveData<ArrayList<ConfirmedLiveCasesByCountry>>()

        val service = RetrofitClientInstance.retrofitInstance?.create(GetCovidDataService::class.java)
        val call = service?.getConfirmedLiveCasesByCountry(countryName)

        call?.enqueue(object : Callback<ArrayList<ConfirmedLiveCasesByCountry>> {
            override fun onFailure(
                call: Call<ArrayList<ConfirmedLiveCasesByCountry>>,
                t: Throwable
            ) {
                Log.d("Error: LiveData notes: ", t.message.toString())

            }

            override fun onResponse(
                call: Call<ArrayList<ConfirmedLiveCasesByCountry>>,
                response: Response<ArrayList<ConfirmedLiveCasesByCountry>>
            ) {
                result.value = response.body()
            }
        })
        return result
    }
}