package com.example.myvault

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myvault.databinding.ActivitySignInBinding
import com.example.myvault.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth= FirebaseAuth.getInstance()

        binding.textView.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.SignInButton.setOnClickListener{
            val email = binding.EmailSignIn.text.toString()
            val pass = binding.PasswordSignIn.text.toString()


            if (email.isNotEmpty() && pass.isNotEmpty()) {
                    firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {

                            Toast.makeText(this, "credentials aren't matching", Toast.LENGTH_SHORT).show()
                        }
                    }


            }

            if(email.isEmpty()){
                binding.EmailSignIn.error=" Username cannot be empty"
            }
            if(pass.isEmpty()){
                binding.PasswordSignIn.error=" password cannot be empty"
            }
            fun isValidPassword(pass:String):Boolean{
                if(pass.length<8) return false
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
                binding.PasswordSignIn.error="password length>= 6\n should contain at least one capital letter\nshould contain atleast one small letter\n should contain atleast one number\nshould contain atleast one special character(@#\$%^&+=_.)"

            }
        }
    }
    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser!=null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
    }
}