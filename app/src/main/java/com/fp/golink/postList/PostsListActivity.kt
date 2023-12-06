package com.fp.golink.postList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fp.golink.AddPostActivity
import com.fp.golink.R
import com.fp.golink.data.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.fp.golink.postDetail.PostDetailActivity
import com.google.firebase.database.ktx.getValue


const val POST_ID = "post id"

class PostsListActivity : AppCompatActivity() {

    private val newPostActivityRequestCode = 1

    lateinit var ref : DatabaseReference
    lateinit var postList : ArrayList<Post>
    lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)


        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        postList = arrayListOf<Post>()

        getPostsData()

        val options = FirebaseRecyclerOptions.Builder<Post>()
            .setQuery(ref, Post::class.java)
            .build()



        val writePost: View = findViewById(R.id.btn_AddPost)
        writePost.setOnClickListener {
            writePostOnClick()
        }
    }

    private fun getPostsData() {
        ref = FirebaseDatabase.getInstance("https://todo-list-tutorial1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("posts")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                postList.clear()
                if(snapshot.exists()) {
                    for (postSnap in snapshot.children) {
                        val post = postSnap.getValue(Post::class.java)
                        postList.add(post!!)
                    }

                    val postsAdapter = PostsAdapter(postList)
                    recyclerView.adapter = postsAdapter

                    postsAdapter.setOnItemClickListener(object : PostsAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@PostsListActivity, PostDetailActivity::class.java)

                            // put extras
                            intent.putExtra("postId", postList[position].id)
                            intent.putExtra("postTulisan", postList[position].tulisan)
//                            intent.putExtra("postGambar", postList[position].gambar)
                            startActivity(intent)
                        }
                    })
                }
            }
        })
    }

    private fun adapterOnClick(post: Post) {
        val intent = Intent(this, PostDetailActivity()::class.java)
        intent.putExtra(POST_ID, post.id)
        startActivity(intent)
    }

    private fun writePostOnClick() {
        val intent = Intent(this, AddPostActivity::class.java)
//        startActivityForResult(intent, newPostActivityRequestCode)
        startActivity(intent)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
//        super.onActivityResult(requestCode, resultCode, intentData)
//
//        /* inserts post into viewModel */
//        if (requestCode == newPostActivityRequestCode && resultCode == Activity.RESULT_OK) {
//            intentData?.let { data ->
//                val postTulisan = data.getStringExtra(POST_TULISAN)
//
//                PostsListViewModel.insertPost(postTulisan)
//            }
//        }
//    }


}