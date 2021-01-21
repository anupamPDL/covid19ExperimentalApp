package com.anupam.covid19app.adapter

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.anupam.covid19app.R
import com.anupam.covid19app.data.entity.ConfirmedLiveCasesByCountry
import com.anupam.covid19app.data.entity.Countries
import com.anupam.covid19app.utils.Utils
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.custom_pie_chart_layout.view.*
import org.eazegraph.lib.models.PieModel
import java.util.*
import kotlin.collections.ArrayList

class MyAdapterForCharts(private val casesByCountry: ArrayList<ConfirmedLiveCasesByCountry>, private val context: Context):
RecyclerView.Adapter<MyAdapterForCharts.ItemHolder>(), Filterable {

    var filteredCasesByCountry = ArrayList<ConfirmedLiveCasesByCountry>()

    init {
        filteredCasesByCountry = casesByCountry
    }

    class ItemHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        override fun onClick(v: View) {
            //todo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_pie_chart_layout, parent, false)

        return ItemHolder(itemView)
    }

    override fun getItemCount() = filteredCasesByCountry.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {

        val countryDetails = filteredCasesByCountry[position].province
        val confirmedCases = filteredCasesByCountry[position].confirmed
        val deaths = filteredCasesByCountry[position].deaths
        val recovered = filteredCasesByCountry[position].recovered
        val activeCases = filteredCasesByCountry[position].active
        val date = filteredCasesByCountry[position].date

        holder.itemView.textView_province_name.text = countryDetails
        if(countryDetails.isNullOrEmpty()) {
            val serialNumber: Int = position + 1
            val stringNumber = "S.N. $serialNumber"
            holder.itemView.textView_province_name.text = stringNumber
        }

        val stringDeaths = Utils.formatMyNumber(deaths)
        val deathTotal = "Deaths: $stringDeaths"
        holder.itemView.deaths.text = deathTotal

        val stringConfirmedCases = Utils.formatMyNumber(confirmedCases)
        val confirmedCasesString = "Confirmed: $stringConfirmedCases"
        holder.itemView.confirmed_cases.text = confirmedCasesString

        val stringRecoveredCases = Utils.formatMyNumber(recovered)
        val recoveredCasesString = "Recovered: $stringRecoveredCases"
        holder.itemView.recovered_cases.text = recoveredCasesString

        val stringActiveCases = Utils.formatMyNumber(activeCases)
        val activeCasesString = "Active: $stringActiveCases"
        holder.itemView.active_cases.text = activeCasesString

        displayInPieChart(holder, date,  deaths, recovered, activeCases, confirmedCases)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayInPieChart(holder: ItemHolder, date: String?, deaths: Int, recovered: Int, activeCases: Int, confirmedCase: Int) {

        val totalCases = deaths + recovered + activeCases + confirmedCase
        val floatedTotalCases = totalCases.toFloat()
        val deathPercentage = deaths * 100 / floatedTotalCases
        val recoveredPercentage = recovered * 100 / floatedTotalCases
        val activePercentage = activeCases * 100 / floatedTotalCases
        val confirmedPercentage = confirmedCase * 100 / floatedTotalCases

        val formattedDate = Utils.formattedDate(date!!)
        val stringTotalCases = Utils.formatMyNumber(totalCases)
        val combinedDateAndTotalVal = "Total cases: $stringTotalCases as of $formattedDate"

                holder.itemView.date.text = combinedDateAndTotalVal

        holder.itemView.piechart.addPieSlice(
            PieModel(
                "Deaths", deathPercentage,
                Color.parseColor("#D00606")
            )
        )
        holder.itemView.piechart.addPieSlice(
            PieModel(
                "Recovered", recoveredPercentage,
                Color.parseColor("#388E3C")
            )
        )
        holder.itemView.piechart.addPieSlice(
            PieModel(
                "Confirmed", confirmedPercentage,
                Color.parseColor("#878585")
            )
        )
        holder.itemView.piechart.addPieSlice(
            PieModel(
                "Active", activePercentage,
                Color.parseColor("#FBC02D")
            )
        )
        holder.itemView.piechart.startAnimation();

        displayBarGraph(holder, deaths, recovered, activeCases, confirmedCase)
    }

    private fun displayBarGraph(holder: ItemHolder, death: Int, recovered: Int, active: Int, confirmed: Int) {

        val deathsFloated = death.toFloat()
        val recoveredFloated = recovered.toFloat()
        val activeFloated = active.toFloat()
        val confirmedFloated = confirmed.toFloat()

        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(deathsFloated, 0))
        entries.add(BarEntry(recoveredFloated, 1))
        entries.add(BarEntry(activeFloated, 2))
        entries.add(BarEntry(confirmedFloated, 3))

        val barDataSet = BarDataSet(entries, "Covid19 cases: deaths, recovered, active, confirmed")

        val labels = ArrayList<String>()
        labels.add("Deaths")
        labels.add("Recovered")
        labels.add("Active")
        labels.add("Confirmed")
        val data = BarData(labels, barDataSet)
        holder.itemView.barChart.data = data

        holder.itemView.barChart.setDescription("Cases represented in numbers")

        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        holder.itemView.barChart.animateY(2000)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val charSearch = constraint.toString()

                if(charSearch.isEmpty()) {
                    filteredCasesByCountry = casesByCountry
                } else {
                    val resultList = ArrayList<ConfirmedLiveCasesByCountry>()
                    for (row in casesByCountry) {
                        val pName = row.province
                        if (pName?.toLowerCase(Locale.ROOT)?.contains(charSearch.toLowerCase(Locale.ROOT)) == true) {
                            resultList.add(row)
                        }
                    }
                    filteredCasesByCountry = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredCasesByCountry
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredCasesByCountry = results?.values as ArrayList<ConfirmedLiveCasesByCountry>
                notifyDataSetChanged()
            }

        }
    }
}