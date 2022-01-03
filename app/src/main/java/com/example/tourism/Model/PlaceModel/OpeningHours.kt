package com.example.tourism.Model.PlaceModel


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OpeningHours(
    @SerializedName("open_now")
    val openNow: Boolean?
): Parcelable