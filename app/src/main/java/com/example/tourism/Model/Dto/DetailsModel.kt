package com.example.tourism.Model.Dto

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import com.example.tourism.Model.PlaceModel.Location
import com.example.tourism.Model.PlaceModel.OpeningHours
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailsModel(var placepicture:String? = "",
                        var name: String = "",
                        val bitmap: Uri,
                        var businessStatus: String = "",
                        val vicinity: String = "",
                        var rating: Double = 0.0,
                        val placeId: String = "",
                        var location:Location): Parcelable

//var nameplace:String = "",

