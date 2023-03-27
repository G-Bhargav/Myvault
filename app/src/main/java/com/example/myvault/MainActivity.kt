package com.example.myvault

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.Manifest
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.myvault.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.DecimalFormat

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: FragmentPageAdapter
    private var storageReference= Firebase.storage.reference
    private  var currentfile: Uri?= null
    private lateinit var fileType: String
    private lateinit var database : DatabaseReference
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var isWritePermission = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){permissions->
            isWritePermission= permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: isWritePermission
        }
        requestPermission()

        firebaseAuth= FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance().reference

        binding.btnLogout.setOnClickListener{
            firebaseAuth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        adapter= FragmentPageAdapter(supportFragmentManager,lifecycle)
        tabLayout= binding.tabLayout
        viewPager= binding.viewpager
        tabLayout.addTab(tabLayout.newTab().setText("Images"))
        tabLayout.addTab(tabLayout.newTab().setText("Videos"))
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
                        binding.btnVideoAdd.visibility= View.GONE
                    }else if(tab.position==1){
                        binding.btnImageAdd.visibility= View.GONE
                        binding.btnPdfAdd.visibility= View.GONE
                        binding.btnVideoAdd.visibility= View.VISIBLE
                    }else{
                        binding.btnImageAdd.visibility= View.GONE
                        binding.btnPdfAdd.visibility= View.VISIBLE
                        binding.btnVideoAdd.visibility= View.GONE
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
                fileType="Images"
                Launcher.launch(it)

            }
        }
        binding.btnPdfAdd.setOnClickListener{
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type="application/*"
                fileType = "PDFs"
                Launcher.launch(it)
            }
        }
        binding.btnVideoAdd.setOnClickListener{
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type= "video/*"
                fileType = "Videos"
                Launcher.launch(it)
            }
        }

    }
    private val Launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        (if(result.resultCode== RESULT_OK ){
            result?.data?.data?.let {
                currentfile = it
                val fil = getFilenameFromUri(this,it).toString()
                uploadToStorage(fil)
                binding.btnUpload.visibility= View.VISIBLE
            }
        }else {
            Toast.makeText(this,"cancelled",Toast.LENGTH_SHORT).show()

        })

    }
     private fun uploadToStorage(filename: String) {
        try{
            currentfile?.let {
                storageReference.child("$fileType/${filename}").putFile(it).addOnProgressListener {
                    val prog : DecimalFormat = DecimalFormat("##")
                    var progr = prog.format(it.bytesTransferred.toFloat()/it.totalByteCount.toFloat()).toFloat()*100
                    binding.btnUpload.text= ("Uploading($progr)%").toString()
                }.addOnSuccessListener {
                    binding.btnUpload.visibility= View.GONE
                    Toast.makeText(this,"Successfully Uploaded in Storage",Toast.LENGTH_SHORT).show()
                    val username = firebaseAuth.currentUser?.email!!.trim().substringBefore(".")
                    val fileLocation =storageReference.child("$fileType/${filename}")
                    val extension = filename.substringAfter(".")
                    fileLocation.downloadUrl.addOnSuccessListener {uri->
                        var url = uri.toString()
                        val file = File(extension,url)
                        database.child(username).child(fileType).child(filename.trim().substringBefore(".")).setValue(file).addOnSuccessListener {
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
    override fun onBackPressed() {
        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMain)
    }

    private fun requestPermission(){
        isWritePermission = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
        val permissionRequest : MutableList<String> = ArrayList()
        if(!isWritePermission){
            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        }
        if(permissionRequest.isNotEmpty()){
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }
    }

}