package com.example.quizzhub

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.quizzhub.adapter.BookmarkAdapter
import com.example.quizzhub.database.BookmarkRepository
import com.example.quizzhub.database.QuizDatabase
import com.example.quizzhub.databinding.ActivityDetailBinding
import com.example.quizzhub.model.BookmarkViewModel
import com.example.quizzhub.model.BookmarkViewModelFactory
import com.example.quizzhub.model.SavedQuestion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var bookmarkViewModel: BookmarkViewModel

    private lateinit var response: String
    private lateinit var question: String
    private lateinit var correctAns: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        val repository = BookmarkRepository(QuizDatabase.getDatabase(this))
        val viewModelFactory = BookmarkViewModelFactory(application , repository)
        bookmarkViewModel = ViewModelProvider(this, viewModelFactory)[BookmarkViewModel::class.java]


        val getData = intent.extras
        val path = getData?.get("path").toString()

        response = getData?.get("response").toString()
        question = getData?.get("question").toString()
        correctAns = getData?.get("correct").toString()
        val quesNo = getData?.get("number").toString()

        binding.tvResponse.text = response.toString()
        binding.tvQuesNo.setText(quesNo.toString())

        binding.backPress.setOnClickListener{
            onBackPressed()
        }

        if (response == "null"){
            onBackPressed()
            Toast.makeText(this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show()
        }

        if (path == "analyse"){
            val yourAns = getData?.get("answer").toString()

            try{
                val response1 = response.replace("**", " ")
                response = response1.replace("```", " ")
            }catch (e: Exception){
                finishAffinity()
                startActivity(Intent(this, AnalyseActivity::class.java))
                finish()
                Toast.makeText(this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show()

            }
            if(yourAns != correctAns){
                binding.tvYourAns.setText(yourAns)
            }
            else{
                binding.tvYourAns.setText(correctAns)
            }
            binding.tvResponse.setText(response)
        }
        else{
            binding.checkbox.isChecked = true
            binding.checkbox.isClickable = false
            binding.tvYourAns.visibility = View.GONE
            binding.headAns.visibility = View.GONE
        }

        binding.tvQues.setText(question)
        binding.tvCorrectAns.setText(correctAns)


        binding.checkbox.setOnCheckedChangeListener{ checkbox , isChecked->
            if(isChecked){
                val addItem = SavedQuestion(id = quesNo.hashCode() , question = question , correctAnswer = correctAns , explain = response )
                bookmarkViewModel.insert(addItem)
            }
        }
    }
}