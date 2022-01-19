package com.example.tourism.ViewModel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourism.Model.Dto.CommentsModel
import com.example.tourism.Repostries.FireRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


class BottonSheetViewModel: ViewModel() {
    val saveLiveDate = MutableLiveData<String>()
    val ErrorLiveData = MutableLiveData<String>()

    private val apiRepo = FireRepository.get()
    private var firestore: FirebaseFirestore

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

    }

    fun save(placeId:String, comment: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                apiRepo.savecomments(placeId,
                    CommentsModel(comment, FirebaseAuth.getInstance().uid.toString())
                ).addOnSuccessListener {
                    Log.d("Firebase", "document saved")
                    saveLiveDate.postValue("")
                }.addOnFailureListener {
                    ErrorLiveData.postValue(it.message.toString())
                    Log.d("Firebase", it.message.toString())
                }

            } catch (e: Exception) {
                Log.d(ContentValues.TAG, e.message.toString())
                ErrorLiveData.postValue(e.message.toString())
            }
        }
    }

}