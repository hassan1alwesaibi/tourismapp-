package com.example.tourism.ViewModel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourism.Model.Dto.CommentsModel
import com.example.tourism.Model.Dto.PlacesModel
import com.example.tourism.Model.Dto.Users
import com.example.tourism.Repostries.UsersRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

private const val TAG = "CommentsViewModel"
class CommentsViewModel: ViewModel() {
    val ErrorLiveData = MutableLiveData<String>()
    val  getUserDatabyIdLiveDate = MutableLiveData<String>()

    val getLiveData = MutableLiveData<MutableList<CommentsModel>>()

    private val apiRepo = UsersRepository.get()
    private var firestore: FirebaseFirestore

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

    }
    fun getlistComment(placeId:String){
        var listcomment = mutableListOf<CommentsModel>()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                apiRepo.getcomments(placeId)
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val comment = document.toObject<CommentsModel>()

                            listcomment.add(comment)

                        }
                         getLiveData.postValue(listcomment)
                        Log.d(TAG, listcomment.toString())

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
    fun getUserDatabyId(userId: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                apiRepo.getUserDatabyId(userId).addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject<Users>(Users::class.java)
                    getUserDatabyIdLiveDate.postValue(userId)
                    Log.d("Firebase", "document saved")
                }.addOnFailureListener {
                    ErrorLiveData .postValue(it.message.toString())
                    Log.d("Firebase", it.message.toString())
                }
            } catch (e: Exception) {
                Log.d(ContentValues.TAG, e.message.toString())
                ErrorLiveData .postValue(e.message.toString())
            }
        }
    }

}