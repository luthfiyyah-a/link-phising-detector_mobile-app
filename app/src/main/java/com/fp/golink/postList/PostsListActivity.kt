package com.fp.golink.postList

import android.content.Intent
import android.os.Bundle
import android.renderscript.Sampler.Value
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
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
import com.fp.golink.PostWithAuthor
import com.fp.golink.data.User
import com.fp.golink.postDetail.PostDetailActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.getValue


const val POST_ID = "post id"

class PostsListActivity : AppCompatActivity() {

    private val newPostActivityRequestCode = 1

    lateinit var ref : DatabaseReference
    lateinit var refUser : DatabaseReference
    lateinit var postList : ArrayList<Post>
    lateinit var userList: ArrayList<User>
    lateinit var postWithAuthorList : ArrayList<PostWithAuthor>
    lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        postList = arrayListOf<Post>()
        userList = arrayListOf<User>()
        postWithAuthorList = arrayListOf<PostWithAuthor>()

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
        val firebaseDatabase = FirebaseDatabase.getInstance("https://todo-list-tutorial1-default-rtdb.asia-southeast1.firebasedatabase.app/")

        ref = firebaseDatabase.getReference("posts")
        refUser = firebaseDatabase.getReference("users")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                postWithAuthorList.clear()

                if(snapshot.exists()) {
                    for (userSnap in snapshot.children) {
                        val userId = userSnap.key
                        Log.d("LIST", "titik user: ${userId}")
                        for (postSnap in userSnap.children) {
                            val post = postSnap.getValue(Post::class.java)

                            val id = post?.id
                            val judul = post?.judul
                            val tulisan = post?.tulisan
                            Log.d("LIST", "titik post: ${judul}")

                            // take user information that relate to this post
                            if (userId != null) {
                                refUser.child(userId).addListenerForSingleValueEvent(object  : ValueEventListener {
                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val user = snapshot.getValue(User::class.java)
                                        val usernameAuthor = user?.username
                                        Log.d("LIST", "titik ambil user: ${user}")

                                        val postWithAuthor = PostWithAuthor(id, judul, tulisan, usernameAuthor, userId)
                                        postWithAuthorList.add(postWithAuthor!!)
                                    }
                                })
                            }
                        }
                    }

                    val postsWithAuthorAdapter = PostsWithAuthorAdapter(postWithAuthorList)
                    recyclerView.adapter = postsWithAuthorAdapter

                    postsWithAuthorAdapter.setOnItemClickListener(object : PostsWithAuthorAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@PostsListActivity, PostDetailActivity::class.java)

                            // put extras
                            intent.putExtra("authorUsername", postWithAuthorList[position].authorName)
                            intent.putExtra("postId", postWithAuthorList[position].postId)
                            intent.putExtra("postJudul", postWithAuthorList[position].judul)
                            intent.putExtra("postTulisan", postWithAuthorList[position].tulisan)
//                            intent.putExtra("postGambar", postList[position].gambar)
                            startActivity(intent)
                        }
                    })
                }
            }
        })
    }

    private fun adapterOnClick(post: Post) {
        val intent = Intent(this@PostsListActivity, PostDetailActivity::class.java)
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