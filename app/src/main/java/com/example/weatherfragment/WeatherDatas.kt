package com.example.weatherfragment

import com.google.gson.annotations.SerializedName

data class WeatherData(
    @SerializedName("sys") val sys: Sys,
    @SerializedName("main") val main: Main,
    @SerializedName("wind") val wind: Wind
)

data class Sys(
    @SerializedName("sunrise") val sunrise: Long,
    @SerializedName("sunset") val sunset: Long
)

data class Main(
    @SerializedName("temp") val temperature: Double
)

data class Wind(
    @SerializedName("speed") val windSpeed: Double
)
