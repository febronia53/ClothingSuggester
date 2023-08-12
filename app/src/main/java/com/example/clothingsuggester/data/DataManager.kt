package com.example.clothingsuggester.data

import com.example.clothingsuggester.R
import com.example.clothingsuggester.data.model.Values


class DataManager {


    fun getWeatherType(values: Values): DayWeatherType {
        return when {
            values.temperature < 15.0 -> {
                DayWeatherType.WINTER
            }

            values.temperature in 15.0..25.0 -> {
                DayWeatherType.SPRING
            }

            values.temperature in 25.0..32.0 -> {
                DayWeatherType.SUMMER
            }

            else -> {
                DayWeatherType.FALL
            }
        }
    }


    private fun getClothesList(dayWeatherType: DayWeatherType): List<Int> {
        return when (dayWeatherType) {
            DayWeatherType.WINTER -> {
                listOf(
                    R.drawable.winter1,
                    R.drawable.winter2,
                    R.drawable.winter3,
                    R.drawable.winter4,
                    R.drawable.winter5,
                    R.drawable.winter6,
                    R.drawable.winter7,
                    R.drawable.winter8
                )
            }

            DayWeatherType.SUMMER -> {
                listOf(
                    R.drawable.summer1,
                    R.drawable.summer2,
                    R.drawable.summer3,
                    R.drawable.summer4,
                    R.drawable.summer5,
                    R.drawable.summer7,
                    R.drawable.summer8
                )
            }

            DayWeatherType.SPRING -> {
                listOf(
                    R.drawable.spring1,
                    R.drawable.spring2,
                    R.drawable.spring3,
                    R.drawable.spring4,
                    R.drawable.spring5,
                    R.drawable.spring6,
                    R.drawable.spring7,
                    R.drawable.spring8
                )
            }

            else -> {
                listOf(
                    R.drawable.autumn1,
                    R.drawable.autumn2,
                    R.drawable.autumn3,
                    R.drawable.autumn4,
                    R.drawable.autumn5,
                    R.drawable.autumn6,
                    R.drawable.autumn7,
                    R.drawable.autumn8
                )
            }
        }
    }

    fun getClothesImageId(dayWeatherType: DayWeatherType): Int {
        return when (dayWeatherType) {
            DayWeatherType.WINTER -> {
                getClothesList(dayWeatherType).random()
            }

            DayWeatherType.SUMMER -> {
                getClothesList(dayWeatherType).random()
            }

            DayWeatherType.SPRING -> {
                getClothesList(dayWeatherType).random()
            }

            else -> {
                getClothesList(dayWeatherType).random()
            }
        }
    }


}
