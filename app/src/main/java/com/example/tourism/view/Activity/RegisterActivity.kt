package com.example.tourism.view.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.example.tourism.Model.Dto.Users
import com.example.tourism.R
import com.example.tourism.ViewModel.UsersViewModel
import com.example.tourism.until.RegisterValidations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class RegisterActivity : AppCompatActivity() {
    private var users = Users()
    private  val usersViewModel: UsersViewModel by viewModels()
    private val validator = RegisterValidations()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val firstname: EditText = findViewById(R.id.firstname_EditText)
        val lastname: EditText = findViewById(R.id.lastname_EditText)
        val emailAddress: EditText = findViewById(R.id.Email_EditText)
        val password: EditText = findViewById(R.id.Password_EditText)
        val confirmpassword: EditText = findViewById(R.id.ConfirmPassword_EditText)
        val registerButton: Button = findViewById(R.id.SingUp_button)
        val loginTextView: TextView = findViewById(R.id.SingIn_textView)


        loginTextView.setOnClickListener() {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        registerButton.setOnClickListener() {
            val fname: String = firstname.text.toString()
            val lname: String = lastname.text.toString()
            val email: String = emailAddress.text.toString()
            val passwor: String = password.text.toString()
            val conpassword: String = confirmpassword.text.toString()

            if (fname.isNotBlank() && lname.isNotBlank() && email.isNotBlank() && passwor.isNotBlank() && conpassword.isNotBlank()) {
                if (passwor == conpassword) {
                    if (validator.emailIsValid(email)) {
                        if (validator.passwordIsValid(passwor)) {
                            FirebaseAuth.getInstance()
                                .createUserWithEmailAndPassword(email, passwor)
                                .addOnCompleteListener() {
                                    if (it.isSuccessful) {
                                        val firebaseUser: FirebaseUser = it.result!!.user!!

                                        users.apply {
                                            FirstName = fname
                                            LastName = lname
                                            Email = email
                                            usersViewModel.save(users)
                                        }


                                        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
                                        sharedPreferencesEditor = sharedPreferences.edit()
                                        sharedPreferencesEditor.putBoolean("isUserLogin", true)
                                        sharedPreferencesEditor.putString("UsserID", FirebaseAuth.getInstance().uid.toString())
                                        sharedPreferencesEditor.commit()

                                        // Store the information in the user collection
//                                        val db = Firebase.firestore
//
//                                        val user = hashMapOf(
//                                            "first" to fname,
//                                            "last" to lname,
//                                            "userId" to firebaseUser.uid
//                                        )
//                                        db.collection("users")
//                                            .add(user)
//                                            .addOnSuccessListener { documentReference ->
//                                                Log.d(
//                                                    TAG,
//                                                    "DocumentSnapshot added with ID: ${documentReference.id}"
//                                                )
//                                            }
//                                            .addOnFailureListener { e ->
//                                                Log.w(TAG, "Error adding document", e)
//                                            }




                                        Toast.makeText(
                                            this,
                                            "User Registered Sucessfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val intent = Intent(this, MainActivity::class.java)
                                        intent.putExtra("UserId", FirebaseAuth.getInstance().uid.toString())
                                        intent.putExtra("Email", firebaseUser.email)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                        } else
                            Toast.makeText(
                                this,
                                "Make sure your password is strong.",
                                Toast.LENGTH_SHORT
                            ).show()
                    } else
                        Toast.makeText(
                            this,
                            "Make sure you typed your email address correctly.",
                            Toast.LENGTH_SHORT
                        ).show()
                } else
                    Toast.makeText(
                        this,
                        "Password and confirm password don't match",
                        Toast.LENGTH_SHORT
                    ).show()
            } else
                Toast.makeText(this, "Registration fields must not be empty", Toast.LENGTH_SHORT)
                    .show()
        }

    }

}


