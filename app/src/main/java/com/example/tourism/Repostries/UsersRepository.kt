package com.example.tourism.Repostries

import android.content.Context
import android.net.Uri
import com.example.tourism.Model.Dto.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

import java.lang.Exception

class UsersRepository(val context: Context) {

    private var storageReference = FirebaseStorage.getInstance().getReference()
    private var Reference =  Firebase.firestore.collection("users")
//    private val ref = storageReference.child(FirebaseAuth.getInstance().uid.toString())


            fun save(users: Users) = Reference.document(FirebaseAuth.getInstance().uid.toString()).set(users)
            fun UploadPhoto(imge: Uri) = storageReference.child(FirebaseAuth.getInstance().uid.toString()).putFile(imge)
            fun delete()  = Reference.document(FirebaseAuth.getInstance().uid.toString()).delete()
            fun getUser()  = Reference.document(FirebaseAuth.getInstance().uid.toString()).get()
            fun getlistUsers() = Reference.get()
        companion object {
        private var instance: UsersRepository? = null

        fun init(context: Context) {
            if (instance == null)
                instance = UsersRepository(context)
        }

        fun get(): UsersRepository {
            return UsersRepository.instance ?: throw Exception("ApiServiceRepository must be initialized ")
        }

    }
}