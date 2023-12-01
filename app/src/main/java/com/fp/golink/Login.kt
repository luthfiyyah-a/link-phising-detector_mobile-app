package com.fp.golink

import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat

class Login : AppCompatActivity() {

    private lateinit var ivImagePerson: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ivImagePerson = findViewById (R.id.ivImagePerson)

        ivImagePerson.setOnClickListener{
            checkPermission()
        }
    }

    val READIMAGE:Int = 253
    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("LUTH", "Permission not granted yet")
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), READIMAGE)
                return
            } else {
                Log.d("LUTH", "Permission already granted")
            }
        }
        loadImage()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            READIMAGE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadImage()
                } else {
                    Log.d("LUTH", "Permission not granted")
                    Log.d("LUTH", "grantResults: ${grantResults.joinToString()}")
                    Toast.makeText(
                        applicationContext,
                        "Cannot access your images",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    val PICK_IMAGE_CODE = 123
    fun loadImage() {

        var intent = Intent(Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_CODE && resultCode == RESULT_OK) {
            data?.data?.let { selectedImage ->
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor: Cursor? = contentResolver.query(selectedImage, filePathColumn, null, null, null)
                cursor?.use {
                    Log.d("LUTH", "Masuk ke sini")
                    if (it.moveToFirst()) {
                        Log.d("LUTH", "Masuk ke sini 1")
                        val columnIndex = it.getColumnIndex(filePathColumn[0])
                        val picturePath = it.getString(columnIndex)
                        cursor.close()
//                        ivImagePerson?.setImageBitmap(BitmapFactory.decodeFile(picturePath))
                        Log.d("LUTH", "ingfo foto")
                        val bitmap = BitmapFactory.decodeFile(picturePath)
                        if (bitmap != null) {
                            ivImagePerson?.setImageBitmap(bitmap)
                        } else {
                            Log.e("LUTH", "Gagal decode gambar dari path: $picturePath")
                        }


                    }
                }
            }
        }
    }


    fun buLogin(view:View) {
        //TODO:
    }
}