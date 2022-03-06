package com.ejamdar.instagram

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PostAdapter (val context : Context, val posts: List<PostClass>) : RecyclerView.Adapter<PostAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {
        //specify the layout file
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {
        val post = posts.get(position)
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvUserName: TextView
        val ivImage : ImageView
        val tvDesc : TextView

        init{
            tvUserName = itemView.findViewById(R.id.userNamePost)
            ivImage = itemView.findViewById(R.id.photoPostView)
            tvDesc = itemView.findViewById(R.id.descTextViewPost)
        }

        //bind method
        fun bind(post: PostClass){
            tvUserName.text = post.getUser()?.username
            tvDesc.text = post.getDesc()
            Glide.with(itemView.context).load(post.getImage()?.url).into(ivImage)
        }


    }

}