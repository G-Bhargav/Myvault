package com.example.myvault

import android.content.Context
import android.content.Intent
import android.content.pm.LauncherActivityInfo
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.viewpager2.widget.ViewPager2
import com.example.myvault.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

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
        database= FirebaseDatabase.getInstance().reference

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
                if (tab != null) {
                    if(tab.position==0){
                        binding.btnImageAdd.visibility= View.VISIBLE
                        binding.btnPdfAdd.visibility= View.GONE
                    }else{
                        binding.btnImageAdd.visibility= View.GONE
                        binding.btnPdfAdd.visibility= View.VISIBLE
                    }
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
                Launcher.launch(it)

            }
        }
        binding.btnPdfAdd.setOnClickListener{
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type="application/*"
                PdfLauncher.launch(it)

            }
        }

        binding.btnUpload.setOnClickListener{
            //      var fil: String = System.currentTimeMillis().toString()


            binding.btnUpload.text= "Uploading.."

        }




    }
    private val Launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        (if(result.resultCode== RESULT_OK ){
            result?.data?.data?.let {
                currentfile = it
                val imageView: ImageView= findViewById(R.id.ImageView)
                imageView.setImageURI(it)
                val fil = getFilenameFromUri(this,it).toString()
                uploadImageToStorage(fil)
                binding.btnUpload.visibility= View.VISIBLE
            }
        }else {
            Toast.makeText(this,"cancelled",Toast.LENGTH_SHORT).show()

        })

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private val PdfLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        (if(result.resultCode== RESULT_OK ){
            result?.data?.data?.let {
                currentfile = it
                var fil = getFilenameFromUri(this,it).toString()
                uploadPdfToStorage(fil)
                binding.btnUpload.visibility= View.VISIBLE
            }
        }else {
            Toast.makeText(this,"cancelled",Toast.LENGTH_SHORT).show()

        })

    }
    private fun uploadImageToStorage(filename: String) {
        try{
            currentfile?.let {

                storageReference.child("Images/${filename}").putFile(it).addOnProgressListener {
                    binding.btnUpload.text= "Uploading(${(it.bytesTransferred/it.totalByteCount)*100})"
                }.addOnSuccessListener {
                    binding.btnUpload.visibility= View.GONE
                    Toast.makeText(this,"Successfully Uploaded in Storage",Toast.LENGTH_SHORT).show()
                    val username = firebaseAuth.currentUser?.email!!.trim().substringBefore(".")
                    val fileLocation =storageReference.child("Images/${filename}")
                    val extension = filename.substringAfter(".")
                    fileLocation.downloadUrl.addOnSuccessListener {uri->
                        var url = uri.toString()
                        val file = File(extension,url)
                        database.child(username).child("Images").child(filename.trim().substringBefore(".")).setValue(file).addOnSuccessListener {
                            Toast.makeText(this,"successfully Uploaded to realtime database", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener{
                            Toast.makeText(this,"error on realtime database", Toast.LENGTH_SHORT).show()
                        }

                    }


                }.addOnFailureListener{
                    Toast.makeText(this,"error on upload", Toast.LENGTH_SHORT).show()
                }



            }
        }
        catch(e: Exception) {
            Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    private fun uploadPdfToStorage(filename: String) {
        try{
            currentfile?.let {

                storageReference.child("PDFs/${filename}").putFile(it).addOnProgressListener {

                    binding.btnUpload.text= "Uploading(${(it.bytesTransferred/it.totalByteCount)*100})"
                }.addOnSuccessListener {
                    binding.btnUpload.visibility= View.GONE
                    Toast.makeText(this,"Successfully Uploaded in Storage",Toast.LENGTH_SHORT).show()
                    val username = firebaseAuth.currentUser?.email!!.trim().substringBefore(".")
                    val fileLocation =storageReference.child("PDFs/${filename}")
                    val extension = filename.substringAfter(".")
                    fileLocation.downloadUrl.addOnSuccessListener {uri->
                        var url = uri.toString()
                        val file = File(extension,url)
                        database.child(username).child("PDFs").child(filename.trim().substringBefore(".")).setValue(file).addOnSuccessListener {
                            Toast.makeText(this,"successfully Uploaded to realtime database", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener{
                            Toast.makeText(this,"error on realtime database", Toast.LENGTH_SHORT).show()
                        }

                    }
                }.addOnFailureListener{
                    Toast.makeText(this,"error on upload", Toast.LENGTH_SHORT).show()
                }



            }
        }
        catch(e: Exception) {
            Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getFilenameFromUri(context: Context, uri : Uri): String? {
        val fileName: String?
        val cursor= context.contentResolver.query(uri, null,null,null)
        cursor?.moveToFirst()
        fileName= cursor?.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
        cursor?.close()
        return fileName
    }

}




