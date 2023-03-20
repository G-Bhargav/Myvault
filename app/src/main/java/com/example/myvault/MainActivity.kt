package com.example.myvault

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.viewpager2.widget.ViewPager2
import com.example.myvault.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.logging.Logger.global

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: FragmentPageAdapter
    private var storageReference= Firebase.storage.reference
    private  var currentfile: Uri?= null
    private lateinit var database : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth= FirebaseAuth.getInstance()



        binding.btnLogout.setOnClickListener{
            firebaseAuth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        adapter= FragmentPageAdapter(supportFragmentManager,lifecycle)
        tabLayout= binding.tabLayout
        viewPager= binding.viewpager
        tabLayout.addTab(tabLayout.newTab().setText("Images"))
        tabLayout.addTab(tabLayout.newTab().setText("PDF's"))

        binding.viewpager.adapter = adapter

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!= null){
                    viewPager.currentItem= tab.position
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        binding.btnImageAdd.setOnClickListener{
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type="image/*"
                imageLauncher.launch(it)

            }
        }

        binding.btnUpload.setOnClickListener{
            val fil: String = System.currentTimeMillis().toString()
            uploadImageToStorage(fil)
            binding.btnUpload.text= "Uploading.."

        }




    }
    private val imageLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        (if(result.resultCode== RESULT_OK ){
            result?.data?.data?.let {
                currentfile = it
                val imageView: ImageView= findViewById(R.id.ImageView)
                imageView.setImageURI(it)
                binding.btnUpload.visibility= View.VISIBLE
            }
        }else {
            Toast.makeText(this,"cancelled",Toast.LENGTH_SHORT).show()

        })

    }
    private fun uploadImageToStorage(filename: String) {
        try{
            currentfile?.let {

                storageReference.child("Images/${filename}").putFile(it).addOnSuccessListener {
                    binding.btnUpload.visibility= View.GONE
                    Toast.makeText(this,"Successfully Uploaded",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(this,"error on upload", Toast.LENGTH_SHORT).show()
                }


            }
        }
        catch(e: Exception) {
            Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

}




