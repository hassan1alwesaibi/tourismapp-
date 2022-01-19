package com.example.tourism.Repostries

import android.content.Context
import com.example.tourism.Api.IPlacesApi
import com.example.tourism.Model.Dto.DetailsModel

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception


private const val BASE_URL = "https://maps.googleapis.com"
private var Reference =  Firebase.firestore.collection("places")
class PlaceRepository(val context: Context) {
    private val retrofitService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val placeApi = retrofitService.create(IPlacesApi::class.java)

   // get places from places Model
    suspend fun getPlaces(lat: Double,
                          lng: Double,
                          radius: Int,
                          pageToken:String) =
        placeApi.getPlaces("$lat,$lng",radius,pageToken)
   // text search for places
    suspend fun searchPlaces(query: String,
                             radius: Any) =
        placeApi.searchPlaces(query,radius)






    //---------------------------------------------------------------------
    /***
     * this companion object for restricts the instantiation of a class to one "single" instance.
     * This is useful when exactly one object is needed to coordinate actions across the system.
     * */
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
//-----------------------------------------------------------------------

}
