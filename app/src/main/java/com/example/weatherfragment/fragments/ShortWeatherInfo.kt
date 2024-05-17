package com.example.weatherfragment.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.weatherfragment.R
import com.example.weatherfragment.WeatherData
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL

class ShortWeatherInfo: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_short_weather_info, container, false)
    }

    private lateinit var temperatureTextView: TextView

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val api = "71708aba4504a562f55ad203b3d109cf"
                val city = "Irkutsk"
                val weatherURL = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$api&units=metric"
                val stream = URL(weatherURL).openStream()
                val data = stream.bufferedReader().use { it.readText() }

                val gson = Gson()
                val weatherData = gson.fromJson(data, WeatherData::class.java)

                val temperature = weatherData.main.temperature

                temperatureTextView = view.findViewById(R.id.temperatureTextView)

                val temperatureText = "Temperature: $temperature °C"

                requireActivity().runOnUiThread {
                    temperatureTextView.text = temperatureText
                    temperatureTextView.textSize = 20f
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}