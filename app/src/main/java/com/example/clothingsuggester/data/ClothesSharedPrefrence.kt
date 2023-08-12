package com.example.clothingsuggester.data


import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class ClothesSharedPrefrence(private val context: Context) {

    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getLastChosenClothesIndex(weatherType: DayWeatherType): Int {
        return sharedPreferences.getInt(weatherType.name, -1)
    }

    fun saveLastChosenClothesIndex(weatherType: DayWeatherType, index: Int) {
        sharedPreferences.edit().putInt(weatherType.name, index).apply()
    }
}
