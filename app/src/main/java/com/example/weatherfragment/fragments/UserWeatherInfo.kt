package com.example.weatherfragment.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weatherfragment.R
import com.example.weatherfragment.WeatherData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL
import java.util.Date

class UserWeatherInfo : Fragment() {

    private lateinit var cityEditText1: EditText
    private lateinit var cityEditText2: EditText

    private lateinit var firstCityNameTextView: TextView
    private lateinit var secondCityNameTextView: TextView

    private lateinit var firstCityWeatherInfo: TextView
    private lateinit var secondCityWeatherInfo: TextView

    private lateinit var button: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_weather_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        cityEditText1 = view.findViewById(R.id.editTextCity1)
        cityEditText2 = view.findViewById(R.id.editTextCity2)

        firstCityNameTextView = view.findViewById(R.id.firstCityName)
        secondCityNameTextView = view.findViewById(R.id.secondCityName)

        firstCityWeatherInfo = view.findViewById(R.id.firstCityWeatherInfo)
        secondCityWeatherInfo = view.findViewById(R.id.secondCityWeatherInfo)

        button = view.findViewById(R.id.refreshButton)
        button.setOnClickListener {
            onRefreshButtonClick(it)
        }
    }

    fun onRefreshButtonClick(view: View) {
        val city1Name = cityEditText1.text.toString()
        val city2Name = cityEditText2.text.toString()

        if (city1Name.isEmpty() && city2Name.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter city name!", Toast.LENGTH_SHORT).show()
            return
        }

        if (city1Name.isNotEmpty()){
            fetchWeatherData(city1Name) { weatherData ->
                updateWeatherInfo(firstCityNameTextView, city1Name, firstCityWeatherInfo, weatherData)
            }
        } else{
            clearCityData(firstCityNameTextView, firstCityWeatherInfo)
        }

        if (city2Name.isNotEmpty()){
            fetchWeatherData(city2Name) { weatherData ->
                updateWeatherInfo(secondCityNameTextView, city2Name, secondCityWeatherInfo, weatherData)
            }
        } else{
            clearCityData(secondCityNameTextView, secondCityWeatherInfo)
        }
    }

    private fun clearCityData(cityNameTextView: TextView, weatherInfoTextView: TextView) {
        cityNameTextView.text = ""
        weatherInfoTextView.text = ""
    }

    private fun fetchWeatherData(city: String, callback: (WeatherData) -> Unit) {
        val api = "71708aba4504a562f55ad203b3d109cf"
        val weatherURL = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$api&units=metric"

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val stream = URL(weatherURL).openStream()
                val data = stream.bufferedReader().use { it.readText() }
                val weatherData = Gson().fromJson(data, WeatherData::class.java)

                launch(Dispatchers.Main) {
                    callback(weatherData)
                }
            } catch (e: Exception) {
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "City $city not found!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateWeatherInfo(cityNameTextView: TextView, cityName: String, weatherTextView: TextView, weatherData: WeatherData) {
        cityNameTextView.text = cityName

        val temperature = weatherData.main.temperature
        val windSpeed = weatherData.wind.windSpeed
        val sunriseTime = weatherData.sys.sunrise * 1000
        val sunsetTime = weatherData.sys.sunset * 1000

        val sunrise = Date(sunriseTime)
        val sunset = Date(sunsetTime)

        val temperatureText = "Temperature: $temperature °C"
        val windSpeedText = "Wind speed: $windSpeed m/s"
        val sunriseText = "Sunrise: $sunrise"
        val sunsetText = "Sunset: $sunset"

        val fullWeatherInfoText = "${temperatureText}\n${windSpeedText}\n${sunriseText}\n${sunsetText}"

        weatherTextView.text = fullWeatherInfoText
    }
}