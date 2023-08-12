package com.example.clothingsuggester.data.model

import androidx.annotation.DrawableRes
import com.example.clothingsuggester.data.DayWeatherType

data class Interval(
    val values: Values,
    val weatherType: DayWeatherType,
    @DrawableRes var clothesImageId: Int
)
