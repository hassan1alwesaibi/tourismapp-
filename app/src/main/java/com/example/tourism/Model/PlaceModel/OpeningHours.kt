package com.example.tourism.Model.PlaceModel


import com.google.gson.annotations.SerializedName

data class OpeningHours(
    @SerializedName("open_now")
    val openNow: Boolean
)