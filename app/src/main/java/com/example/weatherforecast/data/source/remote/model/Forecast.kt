package com.example.weatherforecast.data.source.remote.model

data class Forecast(
    val clouds: Int? = null,
    val deg: Int? = null,
    val dt: Long? = null,
    val feels_like: FeelsLike? = null,
    val humidity: Int? = null,
    val pop: Float? = null,
    val pressure: Int? = null,
    val rain: Float? = null,
    val speed: Float? = null,
    val sunrise: Int? = null,
    val sunset: Int? = null,
    val temp: Temp? = null,
    val weather: List<Weather>? = null
)