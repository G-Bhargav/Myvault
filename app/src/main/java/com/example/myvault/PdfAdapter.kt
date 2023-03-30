package com.example.myvault

import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class PdfAdapter(private val pdfslist : ArrayList<Pdf>): RecyclerView.Adapter<PdfAdapter.PdfViewHolder>(){

    var onItemClick: ((Pdf) -> Unit)?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pdfname,parent,false)
        return PdfViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        val currentitem= pdfslist[position]
        holder.Pdfname.text= currentitem.pdfname

        holder.itemView.setOnClickListener(View.OnClickListener {
            val intent=Intent(holder.itemView.context, Pdfviewer::class.java).also {c->
                c.putExtra("EXTRA_PDFNAME", currentitem.pdfname)
                c.putExtra("EXTRA_FILEURL", currentitem.fileURl)
            }
            holder.itemView.context.startActivity(intent)
        })


        holder.btn.setOnClickListener{
            val url = currentitem.fileURl
            val request = DownloadManager.Request(Uri.parse(url))
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            request.setTitle("Download")
            request.setDescription("${currentitem.pdfname} is downloading...")
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"${currentitem.pdfname}")
            val manager = holder.itemView.context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)
//            Toast
        }
    }

    override fun getItemCount(): Int {
        return pdfslist.size
    }


    inner class PdfViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val Pdfname= itemView.findViewById<TextView>(R.id.textView3)
        val btn = itemView.findViewById<ImageButton>(R.id.imageButton)
    }
}