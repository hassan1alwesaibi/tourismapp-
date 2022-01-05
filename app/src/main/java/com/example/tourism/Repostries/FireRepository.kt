package com.example.tourism.Repostries

import android.content.Context
import android.net.Uri
import com.example.tourism.Model.Dto.CommentsModel
import com.example.tourism.Model.Dto.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

import java.lang.Exception

class FireRepository(val context: Context) {

    private var storageReference = FirebaseStorage.getInstance().getReference()
    private var Reference =  Firebase.firestore.collection("users")
    private var Places = Firebase.firestore.collection("places")
//    private val ref = storageReference.child(FirebaseAuth.getInstance().uid.toString())

//---------------------------------------------------------------------------// for users information
            fun save(users: Users) = Reference.document(FirebaseAuth.getInstance().uid.toString()).set(users)
            fun UploadPhoto(imge: Uri) = storageReference.child(FirebaseAuth.getInstance().uid.toString()).putFile(imge)
            fun delete()  = Reference.document(FirebaseAuth.getInstance().uid.toString()).delete()
            fun getUser()  = Reference.document(FirebaseAuth.getInstance().uid.toString()).get()
            fun getlistUsers() = Reference.get()
            fun getUserDatabyId(userId: String) = Reference.document(userId).get()
 //------------------------------------------------------------------------------------for comments
            fun savecomments(placeId:String, comment:CommentsModel ) = Places.document(placeId).collection("comeents").add(comment)
            fun getcomments(placeId:String) = Places.document(placeId).collection("comeents").get()


    companion object {
        private var instance: FireRepository? = null

        fun init(context: Context) {
            if (instance == null)
                instance = FireRepository(context)
        }

        fun get(): FireRepository {
            return FireRepository.instance ?: throw Exception("ApiServiceRepository must be initialized ")
        }

    }
}