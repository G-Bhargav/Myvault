package com.example.myvault

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myvault.databinding.FragmentImageBinding
import com.example.myvault.databinding.FragmentPdfBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import android.content.Intent


class PdfFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var pdfsList: ArrayList<Pdf>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var binding: FragmentPdfBinding
    lateinit var pdfAdapter: PdfAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPdfBinding.inflate(layoutInflater)
        recyclerView = binding.rvPdfName
        pdfsList = arrayListOf()
        recyclerView.layoutManager = LinearLayoutManager(context)
        firebaseAuth = FirebaseAuth.getInstance()
        val username = firebaseAuth.currentUser?.email!!.trim().substringBefore(".")
        databaseReference =
            FirebaseDatabase.getInstance().getReference().child(username).child("PDFs")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pdfsList = arrayListOf()
                if (snapshot.exists()) {
                    for (dataSnapShot in snapshot.children) {
                        val pdfname = dataSnapShot.getKey().toString()
                        val fileUrl = dataSnapShot.child("fileUrl").getValue().toString()
                        pdfsList.add(Pdf(pdfname,fileUrl))
                    }
                    pdfAdapter = PdfAdapter(pdfsList)
                    recyclerView.adapter = pdfAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })

        pdfAdapter = PdfAdapter(pdfsList)
        recyclerView.adapter = pdfAdapter

        pdfAdapter.onItemClick = {
            Log.d("itemclick", "clicked")
            val pdfasnme = it.pdfname
            val filesUrl = it.fileURl

        }

        return binding.root
    }

}



