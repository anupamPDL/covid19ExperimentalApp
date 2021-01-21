package com.anupam.covid19app.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anupam.covid19app.data.entity.ConfirmedLiveCasesByCountry
import com.anupam.covid19app.retrofit.CovidService

class DetailsViewModel(private val data: String): ViewModel(){

    var liveCasesByCountry : MutableLiveData<ArrayList<ConfirmedLiveCasesByCountry>> = MutableLiveData()
    var service : CovidService = CovidService()

    init {
        getConfirmedCasesByCountryLiveData()
    }

    private fun getConfirmedCasesByCountryLiveData() {
        liveCasesByCountry = service.fetchConfirmedLiveCasesByCountry(data)
    }
}