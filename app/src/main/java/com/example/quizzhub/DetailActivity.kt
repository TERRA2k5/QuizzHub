package com.example.quizzhub

import android.os.Bundle
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
        enableEdgeToEdge()

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

        if (path == "analyse"){
            val yourAns = getData?.get("answer").toString()

            val response1 =response.replace("**", " ")
            val response2 =response1.replace("```", " ")
            if(yourAns != "1"){
                binding.tvYourAns.setText(yourAns)
            }
            else{
                binding.tvYourAns.setText(correctAns)
            }
            binding.tvResponse.setText(response2)

            response = response2

        }
        else{
            binding.checkbox.isChecked = true
            binding.checkbox.isClickable = false
        }

        binding.tvQues.setText(question)
        binding.tvCorrectAns.setText(correctAns)


        binding.checkbox.setOnCheckedChangeListener{ checkbox , isChecked->
            if(isChecked){
                val addItem = SavedQuestion(question = question , correctAnswer = correctAns , explain = response)
                bookmarkViewModel.insert(addItem)
            }
        }
    }
}