package com.example.tourism.Model.Dto

import android.os.Parcelable
import com.example.tourism.Model.PlaceModel.Location
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CollectionModel(
                           var placepicture:String = "",
                           var nameplace:String = "",
                           var comment:String="",
                           var location:Location): Parcelable
