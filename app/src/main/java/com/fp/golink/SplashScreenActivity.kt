package com.fp.golink

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity: AppCompatActivity () {
    private val SPLASH_DISPLAY_LENGHT: Long = 1000 //miliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        Handler().postDelayed({
            val mainIntent = Intent(this, Login::class.java)
            startActivity(mainIntent)
            finish()
        }, SPLASH_DISPLAY_LENGHT)
    }
}