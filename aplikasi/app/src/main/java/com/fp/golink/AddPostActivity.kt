package com.fp.golink

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fp.golink.auth.RegisterActivity
import com.fp.golink.data.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class AddPostActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var etPost : EditText
    private lateinit var etPostJudul : EditText
    private lateinit var btnSubmitPost : Button
    private lateinit var btnBack : ImageView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onStart() {
        super.onStart()

        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()

        // Hapus AuthStateListener saat activity dihentikan
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_post)
        Log.d("LUTH", "Masuk ke add post")

        etPost = findViewById(R.id.etPost)
        etPostJudul = findViewById(R.id.etPostJudul)
        btnSubmitPost = findViewById(R.id.btnSubmitPost)

        btnBack = findViewById(R.id.iv_back)

        firebaseAuth = Firebase.auth

        btnSubmitPost.setOnClickListener(this)
        btnBack.setOnClickListener{
            onBackPressed()
        }

        // Inisialisasi authStateListener
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser

            if (user != null && user.email != null) {
                // The user is signed in, continue with the activity
                Log.d("ADD", "User is signed in: ${user.email}")
            } else {
                // No user is signed in, redirect to RegisterActivity
                Log.d("ADD", "No user signed in, redirecting to RegisterActivity")
                startActivity(Intent(this@AddPostActivity, RegisterActivity::class.java))
                finish()
            }
        }
    }

    override fun onClick(v: View?) {
        saveData()
    }

    private fun saveData() {
        val resultIntent = Intent()

        val user = firebaseAuth.currentUser
        val usernameAuthor = user!!.displayName
        val tulisan = etPost.text.toString().trim()
        val judul = etPostJudul.text.toString().trim()

        if(tulisan.isEmpty()){
            etPost.error = "Isi tulisan"
            return
        }
        if(tulisan.isEmpty()){
            etPost.error = "Isi Judul"
            return
        }

        val ref = FirebaseDatabase.getInstance("https://todo-list-tutorial1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("posts")
        val postId = ref.push().key
        val post = Post(postId, judul, tulisan, gambar = null)
        Log.d("LUTH", postId.toString())
        if(postId != null)
        {
            // Simpan data ke Firebase
            ref.child(user!!.uid).child(postId).setValue(post).addOnCompleteListener {
                    // Tampilkan pesan kesalahan
                    Toast.makeText(applicationContext, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(applicationContext, "Data gagal ditambahkan", Toast.LENGTH_SHORT).show()
            }
        }

        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

}