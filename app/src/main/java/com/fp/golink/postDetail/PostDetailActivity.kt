package com.fp.golink.postDetail

import android.content.DialogInterface
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.fp.golink.R
import com.fp.golink.postList.PostsListActivity
import com.fp.golink.ui.post.PostFragment
import com.google.firebase.database.FirebaseDatabase

class PostDetailActivity : AppCompatActivity() {
    private lateinit var tvPostId : TextView
    private lateinit var tvPostJudul : TextView
    private lateinit var tvPostTulisan : TextView
    private lateinit var ivPostGambar : ImageView
    private lateinit var tvUsername : TextView
    private lateinit var ivProfilePictUser : ImageView
    private lateinit var btnDelete : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.post_item)

        initView()
        setValuesToViews()

        btnDelete.setOnClickListener {
            btnDeleteOnClick()
        }
    }

    private fun btnDeleteOnClick() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Hapus Post?")
        builder.setMessage("Apakah kamu ingin menghapus post ini?")
        builder.setPositiveButton("Iya"){ dialogInterface, which ->
            deleteRow(intent.getStringExtra("authorId").toString(), intent.getStringExtra("postId").toString())
        }
        builder.setNegativeButton("Batal"){dialogInterface, which ->

        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun deleteRow(authorId: String, postId: String) {
        val ref = FirebaseDatabase.getInstance("https://todo-list-tutorial1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("posts").child(authorId).child(postId)
        val mTask = ref.removeValue()

        mTask.addOnCompleteListener {
            Toast.makeText(this, "Post berhasil dihapus", Toast.LENGTH_LONG).show()
            navigateToPostFragment()
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Gagal menghapus: ${error.message}",  Toast.LENGTH_LONG).show()
        }
    }

    private fun initView() {
        tvPostJudul = findViewById(R.id.tv_post_judul)
        tvPostTulisan = findViewById(R.id.tv_post_tulisan)
        ivPostGambar = findViewById(R.id.iv_post_gambar)

        tvUsername = findViewById(R.id.tv_username)
        ivProfilePictUser = findViewById(R.id.iv_profilePictUser)

        btnDelete = findViewById(R.id.btn_deleteItem)
    }

    private fun setValuesToViews() {
        tvPostJudul.text = intent.getStringExtra("postJudul")
        tvPostTulisan.text = intent.getStringExtra("postTulisan")
//        ivPostGambar.setImageBitmap("postGambar")
    }

    private fun navigateToPostFragment() {
        // Buat instance dari FragmentManager
        val fragmentManager = supportFragmentManager

        // Mulai transaksi fragment
        val transaction = fragmentManager.beginTransaction()

        // Ganti fragment saat ini dengan fragment Post
        val postFragment = PostFragment()
        transaction.replace(R.id.fl_fragment, postFragment)

        // Tambahkan transaksi ke back stack agar dapat kembali menggunakan tombol back
        transaction.addToBackStack(null)

        // Lakukan transaksi
        transaction.commit()
    }

}