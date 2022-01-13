
package com.example.tourism.view.Activity


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.tourism.Repostries.PlaceRepository
import com.example.tourism.R

import com.example.tourism.Repostries.FireRepository
import com.example.tourism.databinding.ActivitySplashBinding


class splashActivity : AppCompatActivity() {

    private var mDelayHandler: Handler? = null
    private val splashLong: Long = 2000 //2seconds


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)





        PlaceRepository.init(this)
        FireRepository.init(this)
//        RoomServiceRepository.init(this)
        setContentView(R.layout.activity_splash)
        mDelayHandler = Handler()

        mDelayHandler!!.postDelayed({
            sharedPreferences = getSharedPreferences("Setting", Context.MODE_PRIVATE)
            if (sharedPreferences.getBoolean("isUserLogin", false)) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, splashLong)

    }

    public override fun onDestroy() {

        super.onDestroy()
    }
}