package com.example.madcampweek2.Fragment1

import android.content.Context
import android.graphics.Color
import android.system.Os.bind
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madcampweek2.Fragment3.PostData
import com.example.madcampweek2.R
import kotlinx.android.synthetic.main.post_item.view.*

class PostViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    var view : View = v
    fun bind(item: PostData) {

        // profile image binding
        val profile = view.findViewById<ImageView>(R.id.profile_image)
        val profileUrl = item.profileUrl
        if (profileUrl != null) {
            Glide.with(profile).load(profileUrl).circleCrop().into(profile)
        } else {
            Glide.with(profile).load(R.drawable.user).circleCrop().into(profile)
        }
        // profile image binding
        val image = view.findViewById<ImageView>(R.id.main_image)
        val imageUrl = item.photoUrl
        if (imageUrl != null) {
            Glide.with(view).load(imageUrl).into(image)
        } else {
            Glide.with(view).load(R.drawable.user).into(image)
        }
        //Fill color on Like symbol
        if (item.isMe) {
            view.like_symbol.setColorFilter(Color.parseColor("#E3242B"))
        }

        // text binding
        view.name.text = item.name
        view.main_text.text = item.text
        view.like_number.text = item.number.toString()
    }
}

class PostAdapter(private val mContext: Context, private val itemList: List<PostData>) : RecyclerView.Adapter<PostViewHolder>() {
    override fun getItemCount() = itemList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.post_item,
            parent,
            false
        )
        return PostViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
//        val ITEM_CODE = 1
        val item = itemList[position]

        // call button click event
        holder.view.like_symbol.setOnClickListener{
            if (!item.isMe) {
                item.isMe = true
                item.number++
                holder.view.like_symbol.setColorFilter(Color.parseColor("#E3242B"))
                holder.bind(item)
            } else {
                item.isMe = false
                item.number--
                holder.view.like_symbol.setColorFilter(Color.parseColor("#000000"))
                holder.bind(item)
            }
        }
        holder.apply {
            bind(item)
        }
        // item click event
        holder.view.etc_button.setOnClickListener{

        }
    }
}


