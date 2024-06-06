package com.example.quizzhub

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.quizzhub.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)


        val getData = intent.extras

        val response = getData?.get("response").toString()
        val question = getData?.get("question").toString()
        val yourAns = getData?.get("answer").toString()
        val correctAns = getData?.get("correct").toString()
        val quesNo = getData?.get("number").toString()

        // editing response
        val response1 =response.replace("**", " ")
        val response2 =response1.replace("```", " ")

        binding.tvQues.setText(question)
        binding.tvQuesNo.setText(quesNo)
        binding.tvCorrectAns.setText(correctAns)
        if(yourAns != "1"){
            binding.tvYourAns.setText(yourAns)
        }
        else{
            binding.tvYourAns.setText(correctAns)
        }
        binding.tvResponse.setText(response2)


    }
}