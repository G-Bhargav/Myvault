package com.example.myvault

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.VideoView
import android.widget.MediaController

class Videoviewer : AppCompatActivity() {

    lateinit var videoView: VideoView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videoviewer)
        videoView = findViewById(R.id.videoView)
        var url = intent.getStringExtra("EXTRA_FILEURL")
        val uri: Uri = Uri.parse(url)
        videoView.setVideoURI(uri)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        mediaController.setMediaPlayer(videoView)
        videoView.setMediaController(mediaController)
        videoView.start()
    }
}