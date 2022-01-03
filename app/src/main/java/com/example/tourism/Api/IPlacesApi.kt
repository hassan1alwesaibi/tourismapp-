package com.example.tourism.Api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.tourism.Model.PlaceModel.Places
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IPlacesApi {
    @GET("/maps/api/place/nearbysearch/json?key=AIzaSyAswnygZzJMdw9uEJ21KM5ZTLiAFj7Fogc")




    suspend fun getPlaces(
        @Query("location")location:String,
        @Query("radius")radius:Int,
       // @Query("types")types:String
    ): Response<Places>
}