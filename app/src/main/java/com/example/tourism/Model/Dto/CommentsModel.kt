package com.example.tourism.Model.Dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommentsModel(var comments:String = "",
                         var UserId:String = ""): Parcelable
