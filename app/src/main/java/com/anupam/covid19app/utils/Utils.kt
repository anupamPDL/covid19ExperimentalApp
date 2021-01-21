package com.anupam.covid19app.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.anupam.covid19app.ui.DetailsActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Utils {

    fun initiateDetailsActivity(context: Context, data: String) {
        val intent = Intent(context, DetailsActivity::class.java)
        intent.putExtra("data", data)
        context.startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formattedDate(date: String): String {
        val parsedDate = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
        return parsedDate.format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatToShortDate(date: String): String {
        val parsedDate = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
        return parsedDate.format(DateTimeFormatter.ofPattern("MMMM dd"))
    }

     fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    fun formatMyNumber(number: Int?): String {
        return String.format("%,d", number)
    }
}