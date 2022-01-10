package com.example.tourism.ViewModel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tourism.Repostries.PlaceRepository
import androidx.lifecycle.viewModelScope
import com.example.tourism.Model.Dto.DetailsModel
import com.example.tourism.Model.Dto.Users
import com.example.tourism.Model.PlaceModel.Result

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


class PlaceViewModel:ViewModel() {

    private  val TAG = "PlaceViewModel"
    val placesLiveDate = MutableLiveData<List<Result>>()
     val savedetalsLiveDate = MutableLiveData<DetailsModel>()
    val placesErrorLiveData = MutableLiveData<String>()

    // Getting  from Api  Repository with companion object function
    private val apiRepo = PlaceRepository.get()
    // Getting from Room Service Repository with companion object function
   // private val databaseRepo = RoomServiceRepository.get()
    var pageToken:String = ""
    fun callPlace(lat:Double,lng:Double,radius:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiRepo.getPlaces(lat,lng,radius,pageToken)
                if(response.isSuccessful){
                    response.body()?.run {
                        Log.d(TAG,this.toString())
                        Log.d(TAG, "---------------------")
                        Log.d(TAG,results.toString())
                        pageToken = this.nextPageToken
                        // Send Response to view
                        placesLiveDate.postValue(results)
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
    //----------------------------------------------------------------
    fun searchPlace(query: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiRepo.searchPlaces(query!!)
                if(response.isSuccessful){
                    response.body()?.run {
                        // Send Response to view
                        placesLiveDate.postValue(results)
                    }
                }else {
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