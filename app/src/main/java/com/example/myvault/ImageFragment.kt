package com.example.myvault

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

import com.example.myvault.databinding.FragmentImageBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class ImageFragment : Fragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        view?.findViewById<Button>(R.id.btnImageAdd)?.setOnClickListener{
//            Log.d("ImageButton","clicked")
//



        }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_image, container, false)
    }






}