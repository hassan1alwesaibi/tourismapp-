package com.example.tourism.Model.PlaceModel


import com.google.gson.annotations.SerializedName

data class Southwest(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
)