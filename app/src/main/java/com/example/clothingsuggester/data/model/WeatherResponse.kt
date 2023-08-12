package com.example.clothingsuggester.data.model


data class WeatherResponse(
    val timelines: Timelines,
    val location: Location
)

data class Timelines(
    val minutely: List<MinutelyData>
)

data class Location(
    val lat: Float,
    val lon: Float,
    val name: String,
    val type: String
)
