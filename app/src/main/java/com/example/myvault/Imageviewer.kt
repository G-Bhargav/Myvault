package com.example.myvault

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.RoundedCorner
import android.view.View
import android.webkit.URLUtil
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Imageviewer : AppCompatActivity() {
    lateinit var imageview: ImageView
    lateinit var button: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imageviewer)
        imageview = findViewById(R.id.imageView)
        val fileurl = intent.getStringExtra("EXTRA_FILEURL")
        Glide.with(applicationContext).load(fileurl).transform(RoundedCorners(70)).placeholder(R.drawable.img_2).into(imageview)
        button = findViewById(R.id.btnImageDownload)
        button.setOnClickListener{
            val filename = URLUtil.guessFileName(fileurl, null, null)
            val request = DownloadManager.Request(Uri.parse(fileurl))
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            request.setTitle("Download")
            request.setDescription("$filename is downloading...")
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filename)
            val manager =applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)
            Toast.makeText(applicationContext,"Downloading...", Toast.LENGTH_SHORT).show()
            button.visibility= View.GONE
        }
    }

}