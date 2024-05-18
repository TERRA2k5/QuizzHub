package com.example.quizzhub.logFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.quizzhub.R
import com.example.quizzhub.databinding.FragmentForgetBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class ForgetFragment : Fragment() {

    lateinit var binding: FragmentForgetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_forget, container,false)

        binding.buttonFor.setOnClickListener {
            val email = binding.etEmailFor.text.toString()
            if(email == ""){
                 binding.etEmailFor.error = "Required."
            }
            else{
                 Firebase.auth.sendPasswordResetEmail(email)
                     .addOnCompleteListener { task ->
                         if (task.isSuccessful) {
                             Toast.makeText(context, "Email sent.", Toast.LENGTH_SHORT).show()
                         }
                     }
            }
        }

        return binding.root
    }
}