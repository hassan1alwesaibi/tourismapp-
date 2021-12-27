package com.example.tourism.Repostries

import android.content.Context
import com.example.tourism.Api.IPlacesApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception


private const val BASE_URL = "https://maps.googleapis.com"

class PlaceRepository(val context: Context) {
    private val retrofitService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val placeApi = retrofitService.create(IPlacesApi::class.java)

    suspend fun getPlaces(lat: Double, lng: Double, radius: Int = 1500) =
        placeApi.getPlaces("$lat,$lng", radius)

    companion object {
        private var instance: PlaceRepository? = null

        fun init(context: Context) {
            if (instance == null)
                instance = PlaceRepository(context)
        }

        fun get(): PlaceRepository {
            return instance ?: throw Exception("ApiServiceRepository must be initialized ")
        }
    }


}
