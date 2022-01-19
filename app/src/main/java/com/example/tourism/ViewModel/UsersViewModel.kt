package com.example.tourism.ViewModel


import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourism.Model.Dto.Users
import com.example.tourism.Repostries.FireRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


private const val TAG = "UsersViewModel"

class UsersViewModel : ViewModel() {
    val saveusersLiveDate = MutableLiveData<Users>()
    val UploadPhotosersLiveDate = MutableLiveData<Uri>()
    val getUserLiveDate = MutableLiveData<Users>()
    val deleUserLiveDate = MutableLiveData<String>()
    val usersErrorLiveData = MutableLiveData<String>()
    val getlistUserLiveData = MutableLiveData<List<Users>>()

    private val apiRepo = FireRepository.get()
    private var firestore: FirebaseFirestore

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

    }

    //------------------------------------------------save and edit users
    fun save(users: Users) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                apiRepo.save(users).addOnSuccessListener {
                    Log.d("Firebase", "document saved")
                    saveusersLiveDate.postValue(users)
                }.addOnFailureListener {
                    usersErrorLiveData.postValue(it.message.toString())
                    Log.d("Firebase", it.message.toString())
                }

            } catch (e: Exception) {
                Log.d(ContentValues.TAG, e.message.toString())
                usersErrorLiveData.postValue(e.message.toString())
            }
        }
    }

    //---------------------------------------------------------------------------
    fun UploadPhoto(imge: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val responseImage = apiRepo.UploadPhoto(imge)
                responseImage.addOnSuccessListener { taskSnapshot ->
                    Log.d(TAG, taskSnapshot.metadata?.name.toString())
                    UploadPhotosersLiveDate.postValue(imge)

                }.addOnFailureListener {
                    Log.d(TAG, it.message.toString())
                    usersErrorLiveData.postValue(it.message.toString())
                }

            } catch (e: Exception) {
                Log.d(ContentValues.TAG, e.message.toString())
                usersErrorLiveData.postValue(e.message.toString())
            }
        }
    }

    //----------------------------------------------------------------
    fun deleteuser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                apiRepo.delete().addOnSuccessListener {
                    deleUserLiveDate.postValue("")
                    Log.d("Firebase", "document saved")
                }.addOnFailureListener {
                    usersErrorLiveData.postValue(it.message.toString())
                    Log.d("Firebase", it.message.toString())
                }
            } catch (e: Exception) {
                Log.d(ContentValues.TAG, e.message.toString())
                usersErrorLiveData.postValue(e.message.toString())
            }
        }
    }

    //-----------------------------------------------------------------------
    fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                apiRepo.getUser().addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject<Users>(Users::class.java)
                    getUserLiveDate.postValue(user!!)
                    Log.d("Firebase", "document saved")
                }.addOnFailureListener {
                    usersErrorLiveData.postValue(it.message.toString())
                    Log.d("Firebase", it.message.toString())
                }
            } catch (e: Exception) {
                Log.d(ContentValues.TAG, e.message.toString())
                usersErrorLiveData.postValue(e.message.toString())
            }
        }
    }
    //-------------------------------------------------------------------

    fun getlistUsers(){
        var listuser = mutableListOf<Users>()
        viewModelScope.launch(Dispatchers.IO) {
            try {
            apiRepo.getlistUsers()
                .addOnSuccessListener { documents ->
                for (document in documents) {
                    val user = document.toObject<Users>()
                    user.UserId = document.id
                    listuser.add(user)

                }
                    getlistUserLiveData.postValue(listuser)
            }.addOnFailureListener {
                usersErrorLiveData.postValue(it.message.toString())
                Log.d("Firebase", it.message.toString())
              }

            } catch (e: Exception) {
                Log.d(ContentValues.TAG, e.message.toString())
                usersErrorLiveData.postValue(e.message.toString())
            }


        }
        }

    }





