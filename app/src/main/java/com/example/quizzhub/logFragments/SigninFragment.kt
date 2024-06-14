package com.example.quizzhub.logFragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.quizzhub.MainActivity
import com.example.quizzhub.R
import com.example.quizzhub.database.BookmarkRepository
import com.example.quizzhub.database.QuizDatabase
import com.example.quizzhub.databinding.FragmentSigninBinding
import com.example.quizzhub.model.BookmarkViewModel
import com.example.quizzhub.model.BookmarkViewModelFactory
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import java.util.regex.Matcher
import java.util.regex.Pattern

class SigninFragment : Fragment() {

    lateinit var binding: FragmentSigninBinding
    lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN = 9001
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signin, container, false)

        auth = Firebase.auth

        binding.btnNewUser.setOnClickListener {
            findNavController().navigate(R.id.action_signinFragment_to_signupFragment)
        }

        binding.tvForget.setOnClickListener {
            findNavController().navigate(R.id.action_signinFragment_to_forgetFragment)
        }

        binding.btnIn.setOnClickListener {
            val email = binding.etEmailIn.text.toString()
            val password = binding.etPasswordIn.text.toString()

            if(emailValidator(email)){
                if (email == "") {
                    binding.emailbox.error = "Required"
                }
                if (password == "") {
                    binding.passbox.error = "Required"
                }
                if (email != "" && password != "") {
                    signInPassEmail(email , password)
                }
            }
            else {
                binding.emailbox.error = "Enter valid email ID"
            }

        }

        binding.btnGoogle.setOnClickListener {
            signIn()
        }

        return binding.root
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

    private fun signInPassEmail(email: String, password: String) {
        val repository = BookmarkRepository(QuizDatabase.getDatabase(requireContext()))
        val factory = activity?.let { BookmarkViewModelFactory(it.application , repository) }
        val bookmarkViewModel: BookmarkViewModel by viewModels { factory!! }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    bookmarkViewModel.uploadAllToFirestore()
                    Toast.makeText(
                        context,
                        "Logged in as ${user?.displayName.toString()} ",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(context, MainActivity::class.java))
                    activity?.finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }


    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(context, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val repository = BookmarkRepository(QuizDatabase.getDatabase(requireContext()))
        val factory = activity?.let { BookmarkViewModelFactory(it.application , repository) }
        val bookmarkViewModel: BookmarkViewModel by viewModels { factory!! }
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    bookmarkViewModel.uploadAllToFirestore()
                    Toast.makeText(
                        context,
                        "Signed in as ${user?.displayName.toString()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(context, MainActivity::class.java))
                    activity?.finish()
                } else {
                    Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

}