package com.example.tourism.Api



import com.example.tourism.Model.PlaceModel.PlacesModel
import com.example.tourism.Model.PlaceModel.SearchModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
// for nearby place
interface IPlacesApi {
    @GET("/maps/api/place/nearbysearch/json?key=AIzaSyAswnygZzJMdw9uEJ21KM5ZTLiAFj7Fogc")
    suspend fun getPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("pageToken")pageToken:String
    ): Response<PlacesModel>
    @GET("/maps/api/place/textsearch/json?key=AIzaSyAswnygZzJMdw9uEJ21KM5ZTLiAFj7Fogc")

    suspend fun searchPlaces(
        @Query("query") query: String,
        @Query("radius") radius: Any
    ):Response<SearchModel>



}