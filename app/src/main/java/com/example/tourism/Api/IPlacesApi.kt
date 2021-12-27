package com.example.tourism.Api

import com.example.tourism.Model.PlaceModel.Places
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IPlacesApi {
    @GET("/maps/api/place/nearbysearch/json?key=AIzaSyAswnygZzJMdw9uEJ21KM5ZTLiAFj7Fogc")
    suspend fun getPlaces(
        @Query("location")location:String,
        @Query("radius")radius:Int

    ): Response<Places>
}