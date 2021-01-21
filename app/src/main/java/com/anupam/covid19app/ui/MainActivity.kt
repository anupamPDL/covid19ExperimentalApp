package com.anupam.covid19app.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.anupam.covid19app.R
import com.anupam.covid19app.adapter.MyCountryAdapter
import com.anupam.covid19app.data.entity.Countries
import com.anupam.covid19app.data.entity.CovidCases
import com.anupam.covid19app.utils.Utils.formatMyNumber
import com.anupam.covid19app.utils.Utils.formatToShortDate
import com.anupam.covid19app.utils.Utils.isNetworkAvailable
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

/**

 Developed by: Anupam Poudel
 Date: Jan 20, 2021

 Covid19App displays covid19 cases from all around the world.

 This app uses API provided by
 "CORONAVIRUS COVID19 API". View readme.md for more info.

 Main Page displays global result and a list of countries with covid19 cases.
 User can search country name. Once tapped on the country item, this app takes user to
 another activity where cases are displayed based on province/state.

 If a list of province/state is available, user can search for province/state. In absence of
 province/state, result is displayed based on serial number and search is disabled. Covid19 cases
 are displayed in a pie chart and a bar graph when viewing details.

 The initial data is first loaded into database and displayed in UI.

 Tapping on top 3 items: Recovered, Deaths & Confirmed will sort the list in a descending order
 based on cases.
 */

class MainActivity : AppCompatActivity() {

    private lateinit var applicationViewModel: ApplicationViewModel
    private lateinit var myCountryAdapter: MyCountryAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private  var countries = ArrayList<Countries>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeToolBar()
        initializeSwipeRefresh()
        initializeViewModel()
        initializeSorting()
    }

    private fun initializeSwipeRefresh() {
        swipeRefresh.setOnRefreshListener {
            initializeViewModel()
            swipeRefresh.isRefreshing = false
        }
    }

    private fun initializeViewModel() {

       if(!isNetworkAvailable(this)) {
           progress_circular.visibility = View.GONE
           val message = "You are not connected to internet."
           snackBar(message)
           return
       }else if (isNetworkAvailable(this)){
           applicationViewModel = ViewModelProviders.of(this).get(ApplicationViewModel::class.java)
           applicationViewModel.dbService.getLocalCasesDAO().getAllCases().observe(this, Observer {
               data -> if(data == null) {
               initializeViewModel()
           }else {
               initializeDataToView(data)
           }
           })
       }else {
           val message = "Something's not right. Please refresh to continue."
           snackBar(message)
       }
    }

    private fun initializeToolBar() {
        val title = "Covid 19 - Data"
        toolbar.title = title
        setSupportActionBar(toolbar)
    }

    private fun initializeDataToView(covidCases: CovidCases) {
        countries.clear()

        val totalDeaths = covidCases.global?.totalDeaths
        val totalConfirmed = covidCases.global?.totalConfirmed
        val totalRecovered = covidCases.global?.totalRecovered
        val newDeaths = covidCases.global?.newDeaths
        val newConfirmed = covidCases.global?.newConfirmed
        val newRecovered = covidCases.global?.newRecovered
        val date = covidCases.date

        date_.text = formatToShortDate(date)
        textView_TotalDeaths.text = formatMyNumber(totalDeaths)
        textView_totalConfirmed.text = formatMyNumber(totalConfirmed)
        textView_totalRecovered.text = formatMyNumber(totalRecovered)
        val newCases = "+"+formatMyNumber(newConfirmed)
        textView_new_confirmed.text = newCases

        val newDeathString = "+"+formatMyNumber(newDeaths)
        textView_new_deaths.text = newDeathString

        val newRecoveredString = "+"+formatMyNumber(newRecovered)
        textView_new_recovered.text = newRecoveredString

        textView_totalCases.text = formatMyNumber(totalConfirmed)

        for(eachCountry in covidCases?.countries!!) {
            countries.add(eachCountry)
        }

        initializeRecyclerView()
    }

    private fun initializeRecyclerView() {

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView_country_list.layoutManager = linearLayoutManager
        myCountryAdapter = MyCountryAdapter(countries, this)
        recyclerView_country_list.adapter = myCountryAdapter
        progress_circular.visibility = View.GONE
        myCountryAdapter.notifyDataSetChanged()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val searchViewItem: MenuItem = menu!!.findItem(R.id.app_bar_search)
        val searchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                myCountryAdapter.filter.filter(newText)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun initializeSorting() {

        textView_confirmed.setOnClickListener {
            val compareTotalConfirmed = Countries.CompareTotalConfirmed()
            countries.sortWith(compareTotalConfirmed)
            myCountryAdapter.notifyDataSetChanged()
            val message = "Total Confirmed Cases: Max to Min"
            snackBar(message)
        }

        textView_deaths.setOnClickListener {
            val compareTotalDeaths = Countries.CompareTotalDeaths()
            countries.sortWith(compareTotalDeaths)
            myCountryAdapter.notifyDataSetChanged()
            val message = "Total Death Cases: Max to Min"
            snackBar(message)

        }

        textView_recovered.setOnClickListener {
            val compareTotalRecovered = Countries.CompareTotalRecovered()
            countries.sortWith(compareTotalRecovered)
            myCountryAdapter.notifyDataSetChanged()
            val message = "Total Recovered Cases: Max to Min"
            snackBar(message)
        }

    }

    private fun snackBar(message: String) {
         val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        snackBar.setAction("dismiss") { snackBar.dismiss() }
        snackBar.show()

    }
}