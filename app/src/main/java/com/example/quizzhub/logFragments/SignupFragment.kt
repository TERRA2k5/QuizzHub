package com.example.quizzhub.logFragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.quizzhub.MainActivity
import com.example.quizzhub.R
import com.example.quizzhub.database.BookmarkRepository
import com.example.quizzhub.database.QuizDatabase
import com.example.quizzhub.databinding.FragmentSignupBinding
import com.example.quizzhub.model.BookmarkViewModel
import com.example.quizzhub.model.BookmarkViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import java.util.regex.Matcher
import java.util.regex.Pattern


class SignupFragment : Fragment() {

    lateinit var binding: FragmentSignupBinding
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false)
        binding.progress.visibility = View.GONE
        binding.buttonUp.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val conPassword = binding.etConPassword.text.toString()

            if (emailValidator(email)) {
                if (password == conPassword) {
                    if(name == ""){
                        binding.namebox.error = "Required"
                    }
                    if (password == "") {
                        binding.passbox.error = "Required"
                    }
                    if (email == "") {
                        binding.emailbox.error = "Required"
                    }
                    if (email != "" && password != "") {
                        if (password.length >= 8) {
                            newUser(email, password , name)
                        } else {
                            binding.passbox.error = "Minimum 8 character"
                        }
                    }
                } else {
                    binding.passbox2.error = "Does not match."
                }
            }
            else{
                binding.emailbox.error = "Enter valid email ID"
            }
        }


        return binding.root
    }


    private fun newUser(email: String, password: String , name: String) {

        binding.progress.visibility = View.VISIBLE
        binding.buttonUp.isEnabled = false
        val repository = BookmarkRepository(QuizDatabase.getDatabase(requireContext()))
        val factory = activity?.let { BookmarkViewModelFactory(it.application , repository) }
        val bookmarkViewModel: BookmarkViewModel by viewModels { factory!! }
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    bookmarkViewModel.uploadAllToFirestore()
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    user?.updateProfile(profileUpdates)?.addOnCompleteListener {
                        if(it.isSuccessful){
                            activity?.finishAffinity()
                            startActivity(Intent(context, MainActivity::class.java))
                            activity?.finish()
                            binding.progress.visibility = View.GONE
                            binding.buttonUp.isEnabled = true
                            Toast.makeText(context, "Signed in as ${auth.currentUser?.displayName}", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            binding.buttonUp.isEnabled = true
                            Toast.makeText(context, "Authentication Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }


    private fun emailValidator(email: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN: String =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }

}