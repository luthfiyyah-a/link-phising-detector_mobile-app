package com.fp.golink

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityCompat

class Login : AppCompatActivity() {

    lateinit var ivImagePerson: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ivImagePerson = findViewById<ImageView>(R.id.ivImagePerson)

        ivImagePerson.setOnClickListener{

        }
    }

    val READIMAGE:Int = 253
    fun checkPermission() {
        if(Build.VERSION.SDK_INT>=23) {
            if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), READIMAGE)
            }
        }
        loadImage()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when(requestCode) {
            READIMAGE->{
                if(grantResults[0]==PackageManager.)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    fun loadImage() {
        //TODO: load image
    }

    fun buLogin(view:View) {
        //TODO:
    }
}