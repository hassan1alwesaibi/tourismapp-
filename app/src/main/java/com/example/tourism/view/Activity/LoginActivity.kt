package com.example.tourism.view.Activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.tourism.R
import com.example.tourism.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

lateinit var sharedPreferences: SharedPreferences
lateinit var sharedPreferencesEditor: SharedPreferences.Editor

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
//---------------------------------------------------------------------call forgetPassword dialog
        binding.ForgetPassword.setOnClickListener() {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Forget Password")
            val view: View = layoutInflater.inflate(R.layout.dialog_forgetpassworf, null)
            val username: EditText = view.findViewById(R.id.username)
            builder.setView(view)
            builder.setPositiveButton("Reset", { _, _ ->
                forgetpassword(username)
            })
            builder.setNegativeButton("Close", { _, _ -> })
            builder.show()

        }
//--------------------------------------------if click in it will open register activity
        binding.SingUpTextView.setOnClickListener() {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
//---------------------------------------------------------------------------
        binding.LogInButton.setOnClickListener() {
            val email: String = binding.EmailloginEditText.text.toString()
            val password: String = binding.PasswordloginEditText.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email, password).addOnCompleteListener() {
                        if (it.isSuccessful) {
                            //sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
                            sharedPreferencesEditor = sharedPreferences.edit()
                            sharedPreferencesEditor.putBoolean("isUserLogin", true)
                            sharedPreferencesEditor.commit()

                            val firebaseUser: FirebaseUser = it.result!!.user!!
                            Toast.makeText(
                                this, "User Logged in  " +
                                        "Sucessfully", Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra(
                                "UserId",
                                FirebaseAuth.getInstance().currentUser!!.uid
                            )
                            intent.putExtra(
                                "Email",
                                FirebaseAuth.getInstance().currentUser!!.uid
                            )
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                it.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }


    }
//-------------------------------- for forgetpassword and will resive email from it
    private fun forgetpassword(username: EditText) {
        if (username.text.toString().isEmpty()) {
            return
        } else if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()) {
            return
        }
        Firebase.auth.sendPasswordResetEmail(username.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Email sent", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }

    }

}
