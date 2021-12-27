package com.example.tourism.Model.PlaceModel


import com.google.gson.annotations.SerializedName

data class Northeast(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
)