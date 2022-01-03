package com.example.tourism.Model.Dto

import android.os.Parcelable
import com.example.tourism.Model.PlaceModel.OpeningHours
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailsModel(var placepicture:String = "",
                        var name: String = "",
                        var businessStatus: String = "",
                        val vicinity: String = "",
                        var rating: Double = 0.0,
                        val placeId: String = "",): Parcelable