package com.example.myvault

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners


class ImageAdapter(private var ImagesList: ArrayList<Images>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.imageview,parent,false)
        return ImageViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return ImagesList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.itemView.setOnClickListener(View.OnClickListener {
            val intent = Intent(holder.itemView.context,Imageviewer::class.java).also {
                it.putExtra("EXTRA_FILEURL",ImagesList[position].fileUrl)
            }
            holder.itemView.context.startActivity(intent)
        })

        Glide.with(holder.itemView.context).load(ImagesList[position].fileUrl).transform(RoundedCorners(30)).placeholder(R.drawable.img_2).into(holder.image)


    }

    fun setdata(List: ArrayList<Images>)
    {
        ImagesList=List
        notifyDataSetChanged()
    }

    inner class ImageViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val image:ImageView = itemView.findViewById(R.id.imageView2)
    }
}