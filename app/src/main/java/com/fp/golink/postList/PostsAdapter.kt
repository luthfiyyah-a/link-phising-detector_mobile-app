package com.fp.golink.postList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fp.golink.R
import com.fp.golink.data.Post

class PostsAdapter(private val postList: ArrayList<Post>) :
    RecyclerView.Adapter<PostsAdapter.ViewHolder>(){

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener (clickListener: onItemClickListener) {
        mListener = clickListener
    }

    /* Creates and inflates view and return PostViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item, parent, false)
        return ViewHolder(view, mListener)
    }

    /* Gets current post and uses it to bind view. */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPost = postList[position]
        holder.postTulisan.text =currentPost.tulisan
//        holder.postImage.setImageResource(R.drawable.ic_image)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

        /* ViewHolder for Post, takes in the inflated view and the onClick behavior. */
    class ViewHolder(itemView: View, val clickListener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
            val postTulisan: TextView = itemView.findViewById(R.id.tv_post_tulisan)
            val postImage: ImageView = itemView.findViewById(R.id.iv_post_gambar)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}