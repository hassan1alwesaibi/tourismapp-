package com.example.tourism.view.Activity

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.tourism.Model.Dto.Users
import com.example.tourism.R
import com.example.tourism.ViewModel.UsersViewModel
import com.example.tourism.databinding.ActivityRegisterBinding
import com.example.tourism.until.RegisterValidations
import com.example.tourism.view.profileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class RegisterActivity : AppCompatActivity() {
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var nbuilder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"
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

                                        notification()
//                                        sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE)
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
    // for notification
    fun notification(){
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)

            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            val intent:Intent = Intent(this, MainActivity::class.java)
            intent.putExtra("Notification", true)

            val pendingIntent = PendingIntent.getActivity(this, 444, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            nbuilder = Notification.Builder(this, channelId)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Update profile")
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.icon))
        } else {

            nbuilder = Notification.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.icon))
        }
        notificationManager.notify(1234, nbuilder.build())

    }

}


