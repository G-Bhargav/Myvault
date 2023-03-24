package com.example.myvault

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class ImageAdapter(private val ImagesList: ArrayList<Images>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.imageview,parent,false)
        return ImageViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return ImagesList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Glide.with(holder.itemView.context).load(ImagesList[position].fileUrl).placeholder(R.drawable.img_2).into(holder.image)
    }

    inner class ImageViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val image: ImageView= itemView.findViewById(R.id.imageView2)
    }
}