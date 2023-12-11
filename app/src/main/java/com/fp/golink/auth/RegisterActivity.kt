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
import com.fp.golink.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegisterActivity: AppCompatActivity(){

    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etUsername: EditText
    lateinit var etPassword: EditText
    lateinit var etKonfirmasiPassword: EditText
    lateinit var btnLogin: TextView
    lateinit var btnRegister: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onStart() {
        super.onStart()
//        if(firebaseAuth.currentUser!=null) {
//            startActivity(Intent(this, MainActivity::class.java))
//        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        firebaseAuth = Firebase.auth

        etName = findViewById(R.id.regis_etNama)
        etEmail = findViewById(R.id.regis_etEmail)
        etPassword = findViewById(R.id.regis_etPassword)
        etUsername = findViewById(R.id.regis_etUsername)
        etKonfirmasiPassword = findViewById(R.id.regis_etKonfirmasiPassword)
        btnLogin = findViewById(R.id.regis_tvGoToLogin)
        btnRegister = findViewById(R.id.regis_submit)

        btnLogin.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnRegister.setOnClickListener {
            if(etName.text.isNotEmpty() && etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty()) {
                if(etPassword.text.toString() == etKonfirmasiPassword.text.toString()) {
                    Toast.makeText(this, "Nama: ${etName.text.toString()}", Toast.LENGTH_SHORT).show()
                    processRegister()
                }
                else {
                    Toast.makeText(this, "Konfirmasi kata sandi harus sama", LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(this, "Silakan isi semua data", LENGTH_SHORT).show()
            }
        }
    }

    private fun processRegister() {
        val name = etName.text.toString()
        val username = etUsername.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    Log.w("REGIS", "CreateUserWithEmail:success")

                    Toast.makeText(this, "Nama: ${name}", Toast.LENGTH_SHORT).show()
                    // Set display name after signing in
                    val userUpdateProfile = userProfileChangeRequest {
                        displayName = name
                    }
                    val user = task.result.user
                    user!!.updateProfile(userUpdateProfile)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Welcome, ${user.displayName}", LENGTH_SHORT).show()

                            saveDataUserToDatabase(user, username)

                            startActivity(Intent(this, MainActivity::class.java))
                        }
                        .addOnFailureListener{error2 ->
                            Toast.makeText(this, error2.localizedMessage, LENGTH_SHORT).show()
                        }
                }
                else {
                    // if sign in fails, display a message to the user
                    Log.w("REGIS", "CreateUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Auth failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveDataUserToDatabase(user: FirebaseUser, username: String) {
        if (user != null) {
            val uid = user.uid
            val email = user.email
            val name = user.displayName

            // Simpan informasi tambahan pengguna di Firebase Realtime Database
            val databaseReference = FirebaseDatabase.getInstance("https://todo-list-tutorial1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users")
            val userReference = databaseReference.child(uid)

            val userData = User(uid, name, email, username)

            userReference.setValue(userData).addOnCompleteListener {
                // Tampilkan pesan kesalahan
                Toast.makeText(applicationContext, "Data User berhasil ditambahkan ke database", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(applicationContext, "Data User gagal ditambahkan ke database", Toast.LENGTH_SHORT).show()
            }
        }
    }

}