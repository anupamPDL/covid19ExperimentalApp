package com.anupam.covid19app.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.anupam.covid19app.R
import com.anupam.covid19app.adapter.MyAdapterForCharts
import com.anupam.covid19app.data.entity.ConfirmedLiveCasesByCountry
import com.anupam.covid19app.utils.Utils.isNetworkAvailable
import kotlinx.android.synthetic.main.activity_details.*
import java.util.*
import kotlin.collections.ArrayList

/**
 *  Developed by: Anupam Poudel
    Date: Jan 20, 2021

 */

class DetailsActivity : AppCompatActivity() {

    private var countryName: String? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var myAdapterForCharts: MyAdapterForCharts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        getIntents()
        initializeToolBar()
        initiate()
        initializeSearchViewForProvince()
    }

    private fun getIntents() {
        countryName = intent.extras?.get("data") as String
    }

    private fun initializeToolBar() {
        val title = "Cases in $countryName"
        toolbar.title = title
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initiate() {

        if(isNetworkAvailable(this)) {

            val viewModel: DetailsViewModel by lazy {
                ViewModelProviders.of(this, countryName?.let
                { DetailsViewModelFactory(it) }).get(DetailsViewModel::class.java)
            }
            viewModel.liveCasesByCountry.observe(this, Observer {
                data -> initializeConfirmedCasesGraph(data)
            })
        }else{
            progress_circular_details.visibility = View.GONE
            Toast.makeText(this, "No network available. Please check your connection and try again.", Toast.LENGTH_LONG).show()
        }
    }

    private fun initializeConfirmedCasesGraph(data: ArrayList<ConfirmedLiveCasesByCountry>) {

        if(data[0].province.isNullOrEmpty()) {
            card_.visibility = View.GONE
        }

        linearLayoutManager = LinearLayoutManager(this)
        chart_recycler.layoutManager = linearLayoutManager
        myAdapterForCharts = MyAdapterForCharts(data, this)
        chart_recycler.adapter = myAdapterForCharts
        myAdapterForCharts.notifyDataSetChanged()
        progress_circular_details.visibility = View.GONE
    }

    private fun initializeSearchViewForProvince() {
        search_bar_province.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                myAdapterForCharts.filter.filter(newText)
                return false
            }

        })
    }
}