package com.example.tourism.view.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.tourism.Model.Dto.Users
import com.example.tourism.ViewModel.UsersViewModel
import com.example.tourism.databinding.ActivityRegisterBinding
import com.example.tourism.until.RegisterValidations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var users = Users()
    private  val usersViewModel: UsersViewModel by viewModels()
    private val validator = RegisterValidations()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


// when press it will open login activity
        binding.SingInTextView.setOnClickListener() {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
 //-------------------------------------------------------------------------------------------
   // after fill it will save your information in firebase
        binding.SingUpButton.setOnClickListener() {
            val firstname: String = binding.firstnameEditText.text.toString()
            val lastname: String = binding.lastnameEditText.text.toString()
            val email: String = binding.EmailEditText.text.toString()
            val passwor: String = binding.PasswordEditText.text.toString()
            val conpassword: String = binding.ConfirmPasswordEditText.text.toString()

            if (firstname.isNotBlank() && lastname.isNotBlank() && email.isNotBlank() && passwor.isNotBlank() && conpassword.isNotBlank()) {
                if (passwor == conpassword) {
                    if (validator.emailIsValid(email)) {
                        if (validator.passwordIsValid(passwor)) {
                            FirebaseAuth.getInstance()
                                .createUserWithEmailAndPassword(email, passwor)
                                .addOnCompleteListener() {
                                    if (it.isSuccessful) {
                                        val firebaseUser: FirebaseUser = it.result!!.user!!

                                        users.apply {
                                            FirstName = firstname
                                            LastName = lastname
                                            Email = email
                                            usersViewModel.save(users)
                                        }


                                        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
                                        sharedPreferencesEditor = sharedPreferences.edit()
                                        sharedPreferencesEditor.putBoolean("isUserLogin", true)
                                        sharedPreferencesEditor.putString("UsserID", FirebaseAuth.getInstance().uid.toString())
                                        sharedPreferencesEditor.commit()

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


