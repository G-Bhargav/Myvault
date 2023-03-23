package com.example.myvault

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PdfAdapter( private val pdfslist : ArrayList<Pdf>): RecyclerView.Adapter<PdfAdapter.PdfViewHolder>(){

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

    }

    override fun getItemCount(): Int {
        return pdfslist.size
    }


    inner class PdfViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val Pdfname= itemView.findViewById<TextView>(R.id.textView3)
    }
}