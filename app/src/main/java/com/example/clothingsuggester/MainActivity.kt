package com.example.clothingsuggester

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.clothingsuggester.api.APIManager
import com.example.clothingsuggester.data.DataManager
import com.example.clothingsuggester.data.model.Interval
import com.example.clothingsuggester.data.model.Values
import com.example.clothingsuggester.util.Constants.LOCATION_PERMISSION_CODE
import com.example.clothingsuggester.util.Constants.TAG
import com.example.clothingsuggester.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.Locale

class MainActivity : AppCompatActivity(), APIManager.MinuteDataCallback {
    lateinit var binding: ActivityMainBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val apiManager = APIManager()
    lateinit var city: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCurrentLocation()
    }

    private fun getCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        requestWeatherData()
    }

    private fun requestWeatherData() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_CODE
            )
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    apiManager.makeRequestUsingOkhttp(latitude, longitude, this)
                    val latLng = LatLng(latitude, longitude)
                    city = getCityNameFromLatLng(latLng)
                } else {
                    Log.i(TAG, "Location is null")
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpBinding(todayWeather: Interval) {
        binding.apply {
            textTemperature.text = "${todayWeather.values.temperature.toInt()}°c"
            textHumidityValue.text = "${todayWeather.values.humidity} %"
            textWindValue.text = "${todayWeather.values.windSpeed} km/h"
            textVisibilityValue.text = "${todayWeather.values.visibility.toInt()} km"
            textCityName.text = city
            imageClothes.setImageResource(todayWeather.clothesImageId)
        }
    }

    private fun getCityNameFromLatLng(latLng: LatLng): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        var cityName = ""

        try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses!!.isNotEmpty()) {
                cityName = addresses[0].locality ?: ""
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return cityName
    }

    override fun onMinuteDataReceived(values: Values) {
        val weatherType = DataManager(applicationContext).getWeatherType(values)
        val clothesImageId = DataManager(applicationContext).getClothesImageId(weatherType)

        val interval = Interval(values, weatherType, clothesImageId)

        runOnUiThread {
            setUpBinding(interval)
        }
    }


    override fun onAPIError(errorMessage: String) {
        Log.e(TAG, "API Error: $errorMessage")
        // Handle API errors here, if necessary
    }
}
