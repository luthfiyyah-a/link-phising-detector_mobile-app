package com.fp.golink.ui.post

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fp.golink.PostWithAuthor
import com.fp.golink.R
import com.fp.golink.data.Post
import com.fp.golink.data.User
import com.fp.golink.postDetail.PostDetailActivity
import com.fp.golink.postList.PostsWithAuthorAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class PostFragment : Fragment() {

    lateinit var ref: DatabaseReference
    lateinit var refUser: DatabaseReference
    lateinit var postWithAuthorList: ArrayList<PostWithAuthor>
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        postWithAuthorList = arrayListOf<PostWithAuthor>()
        getPostsData()
        return view
    }

    private fun getPostsData() {
        val firebaseDatabase = FirebaseDatabase.getInstance("https://todo-list-tutorial1-default-rtdb.asia-southeast1.firebasedatabase.app/")
        ref = firebaseDatabase.getReference("posts")
        refUser = firebaseDatabase.getReference("users")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                // Handling onCancelled
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                postWithAuthorList.clear()

                if (snapshot.exists()) {
                    for (userSnap in snapshot.children) {
                        val userId = userSnap.key
                        for (postSnap in userSnap.children) {
                            val post = postSnap.getValue(Post::class.java)
                            val id = post?.id
                            val judul = post?.judul
                            val tulisan = post?.tulisan

                            if (userId != null) {
                                refUser.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(error: DatabaseError) {
                                        // Handling onCancelled
                                    }

                                    override fun onDataChange(userSnapshot: DataSnapshot) {
                                        val user = userSnapshot.getValue(User::class.java)
                                        val usernameAuthor = user?.username

                                        val postWithAuthor = PostWithAuthor(id, judul, tulisan, usernameAuthor)
                                        postWithAuthorList.add(postWithAuthor!!)
                                    }
                                })
                            }
                        }
                    }

                    val postsWithAuthorAdapter = PostsWithAuthorAdapter(postWithAuthorList)
                    recyclerView.adapter = postsWithAuthorAdapter

                    postsWithAuthorAdapter.setOnItemClickListener(object : PostsWithAuthorAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(requireContext(), PostDetailActivity::class.java)
                            intent.putExtra("authorUsername", postWithAuthorList[position].authorName)
                            intent.putExtra("postId", postWithAuthorList[position].postId)
                            intent.putExtra("postJudul", postWithAuthorList[position].judul)
                            intent.putExtra("postTulisan", postWithAuthorList[position].tulisan)
                            startActivity(intent)
                        }
                    })
                }
            }
        })
    }
}
