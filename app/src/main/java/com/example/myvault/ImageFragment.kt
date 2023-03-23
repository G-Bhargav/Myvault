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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ImageFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var imagesList: ArrayList<Images>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var binding: FragmentImageBinding
    lateinit var imageAdapter:ImageAdapter
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentImageBinding.inflate(layoutInflater)
        recyclerView= binding.rvImageView
        recyclerView.layoutManager= LinearLayoutManager(context)
        imagesList= arrayListOf()
        firebaseAuth= FirebaseAuth.getInstance()
        val username = firebaseAuth.currentUser?.email!!.trim().substringBefore(".")
        databaseReference= FirebaseDatabase.getInstance().getReference().child(username).child("Images")
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (dataSnapShot in snapshot.children){
                        val image = dataSnapShot.child("fileUrl").getValue().toString()
                        imagesList.add(Images(image))
                    }
                    imageAdapter=ImageAdapter(imagesList)
                    recyclerView.adapter= imageAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show()
            }

        })
        return binding.root
    }
}