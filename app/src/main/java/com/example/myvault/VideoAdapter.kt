package com.example.myvault

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class VideoAdapter(private val VideosList: ArrayList<Videos>) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.videoname,parent,false)
        return VideoViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return VideosList.size
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val currentitem= VideosList[position]
        holder.Videoname.text = currentitem.videoname
        currentitem.videoname?.let { Log.d("12334322", it) }
        currentitem.fileUrl?.let { Log.d("12334322", it) }

        holder.itemView.setOnClickListener(View.OnClickListener {
            val intent= Intent(holder.itemView.context, Videoviewer::class.java).also { c->
                c.putExtra("EXTRA_VIDEONAME", currentitem.videoname)
                c.putExtra("EXTRA_FILEURL", currentitem.fileUrl)
            }
            holder.itemView.context.startActivity(intent)
        })
        holder.button.setOnClickListener{
            val url = currentitem.fileUrl
            val filename = URLUtil.guessFileName(url, null, null)
            val request = DownloadManager.Request(Uri.parse(url))
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            request.setTitle("Download")
            request.setDescription("$filename is downloading...")
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filename)
            val manager = holder.itemView.context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)
            Toast.makeText(holder.itemView.context,"Downloading...", Toast.LENGTH_SHORT).show()
            holder.button.visibility= View.GONE

        }

    }

    inner class VideoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val Videoname= itemView.findViewById<TextView>(R.id.textView4)
        val button = itemView.findViewById<ImageButton>(R.id.btnVideoDownload)
    }
}