package com.example.tourism.Model


import com.google.gson.annotations.SerializedName

data class Places(
    @SerializedName("html_attributions")
    val htmlAttributions: List<Any>,
    @SerializedName("next_page_token")
    val nextPageToken: String,
    @SerializedName("results")
    val results: List<Result>,
    @SerializedName("status")
    val status: String
)