package com.fp.golink.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import com.fp.golink.MainActivity
import com.fp.golink.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button
    lateinit var btnRegister: TextView
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onStart() {
        super.onStart()
//        if(firebaseAuth.currentUser!=null) {
//            startActivity(Intent(this, MainActivity::class.java))
//        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        firebaseAuth = Firebase.auth

        etEmail = findViewById(R.id.login_etEmail)
        etPassword = findViewById(R.id.login_etPassword)
        btnLogin = findViewById(R.id.login_goToRegister)
        btnRegister = findViewById(R.id.login_submit)

        btnRegister.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        btnLogin.setOnClickListener {
            if(etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty()) {
                processLogin()
            }
            else {
                Toast.makeText(this, "Silakan isi semua data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun processLogin() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                    val user = firebaseAuth.currentUser
                    Toast.makeText(this, "Welcome, ${user?.displayName}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                }
            .addOnFailureListener { error ->
                Toast.makeText(this, error.localizedMessage, LENGTH_SHORT).show()
            }
    }

}