package com.novlesh.exp63

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val countryList: MutableList<CountryInfo> = mutableListOf()
    private lateinit var adapter: CountryAdapter
    private val independenceDates = listOf(
        "June 29, 1977", "July 1, 1867", "January 1, 1801", "January 1, 1901", "July 14, 1789",
        "October 3, 1990", "May 3, 1947", "September 7, 1822", "August 15, 1947", "October 1, 1949"

    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView: ListView = findViewById(R.id.country_list)
        adapter = CountryAdapter(this, countryList, independenceDates)
        listView.adapter = adapter

        fetchCountryData()
    }


    private fun fetchCountryData() {
        GlobalScope.launch(Dispatchers.IO) {
            val url = URL("https://restcountries.com/v3.1/all")
            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection

            try {
                val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val response = StringBuilder()

                var line = bufferedReader.readLine()
                while (line != null) {
                    response.append(line)
                    line = bufferedReader.readLine()
                }

                withContext(Dispatchers.Main) {
                    parseAndDisplayData(response.toString())
                }
            } finally {
                urlConnection.disconnect()
            }
        }
    }

    private fun parseAndDisplayData(response: String) {
        val jsonArray = JSONArray(response)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)

            // Get the English translations for country names and official names
            val countryName = jsonObject.getJSONObject("name").optString("common", "").trim()
            val officialName = jsonObject.getJSONObject("name").optString("official", "").trim()
            val flagUrl = jsonObject.getJSONObject("flags").getString("png").trim()

            countryList.add(CountryInfo(countryName, flagUrl))
        }

        adapter.notifyDataSetChanged()
    }
}
