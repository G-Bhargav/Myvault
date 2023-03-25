package com.example.myvault

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myvault.databinding.FragmentImageBinding
import com.example.myvault.databinding.FragmentVideoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class VideoFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var videosList: ArrayList<Videos>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var binding: FragmentVideoBinding
    lateinit var videoAdapter:VideoAdapter
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View? {
        binding=FragmentVideoBinding.inflate(layoutInflater)
        recyclerView= binding.rvVideoName
        recyclerView.layoutManager= LinearLayoutManager(context)
        videosList= arrayListOf()
        firebaseAuth= FirebaseAuth.getInstance()
        val username = firebaseAuth.currentUser?.email!!.trim().substringBefore(".")
        databaseReference= FirebaseDatabase.getInstance().getReference().child(username).child("Videos")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                videosList= arrayListOf()
                if (snapshot.exists()){
                    for (dataSnapShot in snapshot.children){
                        val videoname = dataSnapShot.getKey().toString()
                        val videofileurl = dataSnapShot.child("fileUrl").getValue().toString()
                        videosList.add(Videos(videoname,videofileurl))
                    }
                    videoAdapter=VideoAdapter(videosList)
                    recyclerView.adapter= videoAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,error.toString(), Toast.LENGTH_SHORT).show()
            }

        })
        videoAdapter=VideoAdapter(videosList)
        recyclerView.adapter= videoAdapter
        return binding.root
    }
}