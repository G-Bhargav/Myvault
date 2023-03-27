package com.example.myvault

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myvault.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


        binding.textView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.SignUpButton.setOnClickListener {
            val email = binding.EmailSignUp.text.toString()
            val pass = binding.PasswordSignUp.text.toString()
            val confirmpass = binding.ConfirmPasswordSignUp.text.toString()

            if(email.isEmpty()){
                binding.EmailSignUp.error=" Username cannot be empty"
            }
            if(pass.isEmpty()){
                binding.PasswordSignUp.error=" password cannot be empty"
            }
            if(confirmpass.isEmpty()){
                binding.ConfirmPasswordSignUp.error=" password cannot be empty"
            }
            fun isValidPassword(pass:String):Boolean{
                if(pass.length<6) return false
                var u = 0
                var l = 0
                var d = 0
                var s = 0
                for (char in pass){
                    if(char.isUpperCase()) u++
                    else if(char.isLowerCase()) l++
                    else if(char.isDigit()) d++
                    else if(char in "@#$%^&+=_.") s++
                }
                if(u==0|| l==0 || s==0 || d==0) return false
                return true
            }
            if (!isValidPassword(pass)){
                binding.PasswordSignUp.error="password length>= 6\n should contain at least one capital letter\nshould contain atleast one small letter\n should contain atleast one number\nshould contain atleast one special character(@#\$%^&+=_.)"

            }
            if (email.isNotEmpty() && pass.isNotEmpty() && confirmpass.isNotEmpty()) {
                if (pass == confirmpass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, MainActivity::class.java)
                            Toast.makeText(this,"Account Successfully created",Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                        } else {

                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    "Confirm password and password aren't matching".also { binding.ConfirmPasswordSignUp.error = it }

                }
            }
        }
    }

    override fun onBackPressed() {
        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMain)
    }
}