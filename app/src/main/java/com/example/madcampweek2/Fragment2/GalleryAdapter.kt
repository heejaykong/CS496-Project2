package com.example.madcampweek2.Fragment2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madcampweek2.R

class GalleryAdapter(val context: Context?, private val datasetList : MutableList<ImageData>, private val folderImageNum : MutableList<Int>) :
    RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>(){
    var mPosition = 0
    fun getPosition(): Int{
        return mPosition
    }
    fun setPosition(position:Int){
        mPosition = position
    }
    inner class GalleryViewHolder(v: View) : RecyclerView.ViewHolder(v){
        private val gridImage = v.findViewById<ImageView>(R.id.imageView)
        @RequiresApi(Build.VERSION_CODES.P)
        fun bind(item: ImageData){
            val uri = item.contentUri
            Glide.with(gridImage).load(uri).into(gridImage)
        }
    }
    fun addItem(aNew : ImageData){
        datasetList.add(aNew)
        notifyDataSetChanged()
    }
    fun removeItem(position : Int){
        if (position>0){
            datasetList.removeAt(position)
            notifyDataSetChanged()
        }
    }
    override fun getItemCount() : Int{
        return datasetList.size
    }
    override fun onCreateViewHolder(parent : ViewGroup, viewType: Int): GalleryViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.grid_item,
            parent,
            false
        )
        return GalleryViewHolder(inflatedView)
    }
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val item = datasetList[position]
        if (context != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val intent = Intent(context, FocusActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString("uri", item.contentUri.toString())
                    intent.putExtras(bundle)    // intent 객체에 Bundle을 저장
                    ActivityCompat.startActivityForResult(context as Activity, intent, 1, bundle)
                }
            })
        }
    }
}