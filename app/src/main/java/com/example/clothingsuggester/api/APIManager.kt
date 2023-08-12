package com.example.clothingsuggester.api

import com.example.clothingsuggester.data.model.Values
import com.example.clothingsuggester.data.model.WeatherResponse
import com.example.clothingsuggester.util.Constants.APIKEY
import com.example.clothingsuggester.util.Constants.OPENAPIKEY
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class APIManager {
    val client = OkHttpClient()

    interface MinuteDataCallback {
        fun onMinuteDataReceived(values: Values)
        fun onAPIError(errorMessage: String)
    }

    fun makeRequestUsingOkhttp(
        latitude: Double,
        longitude: Double,
        callback: MinuteDataCallback
    ) {
        val url = HttpUrl.Builder().scheme("https").host("api.tomorrow.io")
            .addPathSegments("/v4/weather/forecast")
            .addQueryParameter("location", "$latitude,$longitude")
            .addQueryParameter(APIKEY, OPENAPIKEY)
            .build()

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onAPIError("Request failed: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBodyString = response.body?.string()

                if (responseBodyString != null) {
                    try {
                        val result =
                            Gson().fromJson(responseBodyString, WeatherResponse::class.java)
                        val minutelyDataList = result.timelines.minutely

                        val lastestMinuteData = minutelyDataList.first()
                        callback.onMinuteDataReceived(lastestMinuteData.values)

                    } catch (e: JsonSyntaxException) {
                        callback.onAPIError("JSON parsing error: ${e.message}")
                    }
                } else {
                    callback.onAPIError("Response body is null")
                }
            }
        })
    }
}
