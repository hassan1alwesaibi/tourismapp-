package com.example.tourism.ViewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tourism.Repostries.PlaceRepository
import androidx.lifecycle.viewModelScope
import com.example.tourism.Model.PlaceModel.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


class PlaceViewModel:ViewModel() {
    val placesLiveDate = MutableLiveData<List<Result>>()
    val placesErrorLiveData = MutableLiveData<String>()
    private val apiRepo = PlaceRepository.get()

    fun callPlace(lat:Double,lng:Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiRepo.getPlaces(lat, lng)
                if(response.isSuccessful){
                    response.body()?.run {
                        Log.d(TAG,this.toString())
                        placesLiveDate.postValue(results) // ask
                    }
                } else {
                    Log.d(TAG,response.message())

                    // Send Error Response to view
                    placesErrorLiveData.postValue(response.message())
                }
                }catch (e: Exception) {
                Log.d(TAG,e.message.toString())
                placesErrorLiveData.postValue(e.message.toString())
            }
        }
    }
}