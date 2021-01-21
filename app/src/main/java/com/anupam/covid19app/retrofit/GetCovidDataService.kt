package com.anupam.covid19app.retrofit

import com.anupam.covid19app.data.entity.ConfirmedLiveCasesByCountry
import com.anupam.covid19app.data.entity.CovidCases
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GetCovidDataService {

    @GET("summary")
     suspend fun getAllCovidCases() : CovidCases

    @GET("live/country/{country_name}/status/confirmed")
    fun getConfirmedLiveCasesByCountry(
            @Path("country_name")country_name: String): Call<ArrayList<ConfirmedLiveCasesByCountry>>

}