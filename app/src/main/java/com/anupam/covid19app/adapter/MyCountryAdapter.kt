package com.anupam.covid19app.adapter

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.anupam.covid19app.R
import com.anupam.covid19app.data.entity.Countries
import com.anupam.covid19app.utils.Utils
import com.anupam.covid19app.utils.Utils.formatMyNumber
import kotlinx.android.synthetic.main.custom_countries_list.view.*
import java.util.*
import kotlin.collections.ArrayList

class MyCountryAdapter(private var countries: ArrayList<Countries>, private val context: Context):
    RecyclerView.Adapter<MyCountryAdapter.ItemHolder>(), Filterable {

    var countryFilterList = ArrayList<Countries>()

    init {
        countryFilterList = countries
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_countries_list, parent, false)

        return ItemHolder(itemView)
    }

    override fun getItemCount() = countryFilterList.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {

        val totalDeaths = countryFilterList[position].totalDeaths
        val newDeaths = countryFilterList[position].newDeaths
        val totalConfirmed = countryFilterList[position].totalConfirmed
        val newConfirmed = countryFilterList[position].newConfirmed
        val totalRecovered = countryFilterList[position].totalRecovered
        val newRecovered = countryFilterList[position].newRecovered
        val country = countryFilterList[position].country

        holder.itemView.textView_TotalDeaths_.text = formatMyNumber(totalDeaths)

        val newCases = "+"+formatMyNumber(newConfirmed)
        holder.itemView.textView_NewConfirmed_.text = newCases


        val newDeathString = "+"+formatMyNumber(newDeaths)
        holder.itemView.textView_newDeaths_.text = newDeathString

        val newRecoveredString = "+"+formatMyNumber(newRecovered)
        holder.itemView.textView_newRecovered_.text = newRecoveredString

        holder.itemView.textView_totalCases_custom.text = formatMyNumber(totalConfirmed)
        holder.itemView.textView_totalConfirmed_.text = formatMyNumber(totalConfirmed)
        holder.itemView.textView_totalRecovered_.text = formatMyNumber(totalRecovered)
        holder.itemView.textView_country_name.text = country

        holder.itemView.cardView_card.setOnClickListener {
            Utils.initiateDetailsActivity(context, country)
        }
    }

    class ItemHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        override fun onClick(v: View) {
            Log.d("RecyclerView", "testing!")

        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val charSearch = constraint.toString()

              if(charSearch.isEmpty()) {
                  countryFilterList = countries
                } else {
                    val resultList = ArrayList<Countries>()
                        for (row in countries) {
                            val cName = row.country
                            if (cName.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                                resultList.add(row)
                            }
                        }
                        countryFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = countryFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                countryFilterList = results?.values as ArrayList<Countries>
                notifyDataSetChanged()
            }

        }
    }
}