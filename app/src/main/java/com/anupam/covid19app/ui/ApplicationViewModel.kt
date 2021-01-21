package com.anupam.covid19app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.anupam.covid19app.data.db.CovidCasesDBService
import kotlinx.coroutines.launch

class ApplicationViewModel(application: Application): AndroidViewModel(application) {

    private var _dbService: CovidCasesDBService = CovidCasesDBService(application)

    init {
        fetchCases()
    }

    private fun fetchCases() {
        viewModelScope.launch {
            _dbService.fetchCovidCases()
        }
    }

    internal var dbService: CovidCasesDBService
    get() {return _dbService}
    set(value) {_dbService = value}
}