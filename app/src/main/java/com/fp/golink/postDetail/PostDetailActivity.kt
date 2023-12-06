package com.fp.golink.postDetail

import android.media.Image
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.fp.golink.R

class PostDetailActivity : AppCompatActivity() {
    private lateinit var tvPostId : TextView
    private lateinit var tvPostTulisan : TextView
    private lateinit var ivPostGambar : ImageView
    private lateinit var tvUsername : TextView
    private lateinit var ivProfilePictUser : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.post_item)

        initView()
        setValuesToViews()
    }

    private fun initView() {
        tvPostTulisan = findViewById(R.id.tv_post_tulisan)
        ivPostGambar = findViewById(R.id.iv_post_gambar)

        tvUsername = findViewById(R.id.tv_username)
        ivProfilePictUser = findViewById(R.id.iv_profilePictUser)


    }

    private fun setValuesToViews() {
        tvPostTulisan.text = intent.getStringExtra("postTulisan")
//        ivPostGambar.setImageBitmap("postGambar")
    }

}