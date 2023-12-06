package com.fp.golink

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fp.golink.data.Post
import com.google.firebase.database.FirebaseDatabase

class AddPostActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var etPost : EditText
    private lateinit var btnSubmitPost : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_post)
        Log.d("LUTH", "Masuk ke add post")

        etPost = findViewById(R.id.etPost)
        btnSubmitPost = findViewById(R.id.btnSubmitPost)

        btnSubmitPost.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        saveData()
    }

    private fun saveData() {
        val resultIntent = Intent()

        val tulisan = etPost.text.toString().trim()

        if(tulisan.isEmpty()){
            etPost.error = "Isi tulisan"
            return
        }

        val ref = FirebaseDatabase.getInstance("https://todo-list-tutorial1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("posts")
        val postId = ref.push().key
        val post = Post(postId, tulisan, gambar = null)
        Log.d("LUTH", postId.toString())
        if(postId != null)
        {

            // Simpan data ke Firebase
            ref.child(postId).setValue(post).addOnCompleteListener {
                    // Tampilkan pesan kesalahan
                    Toast.makeText(applicationContext, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(applicationContext, "Data gagal ditambahkan", Toast.LENGTH_SHORT).show()
            }
        }

        resultIntent.putExtra("tulisan", tulisan)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

}