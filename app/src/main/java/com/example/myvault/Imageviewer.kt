package com.example.myvault

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.RoundedCorner
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class Imageviewer : AppCompatActivity() {
    lateinit var imageview: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imageviewer)
        imageview = findViewById(R.id.imageView)
        val fileurl = intent.getStringExtra("EXTRA_FILEURL")
        Glide.with(applicationContext).load(fileurl).transform(RoundedCorners(70)).placeholder(R.drawable.img_2).into(imageview)

    }

}