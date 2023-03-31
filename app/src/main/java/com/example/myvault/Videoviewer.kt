package com.example.myvault

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity


class Videoviewer : AppCompatActivity() {

    lateinit var videoView: VideoView
    lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videoviewer)
        videoView = findViewById(R.id.videoView)
        progressBar = findViewById(R.id.videoProgressBar)
        var url = intent.getStringExtra("EXTRA_FILEURL")
        val uri: Uri = Uri.parse(url)
        videoView.setVideoURI(uri)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        mediaController.setMediaPlayer(videoView)
        videoView.setMediaController(mediaController)
        videoView.start()
        videoView.setOnPreparedListener {
            progressBar.visibility= View.GONE
            videoView.start() // start the video
        }
    }
}