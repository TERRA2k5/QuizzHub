package com.example.quizzhub

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.quizzhub.databinding.ActivityQuestionBinding
import com.example.quizzhub.model.QuesViewModel
import com.example.quizzhub.model.Quiz
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class QuestionActivity : AppCompatActivity() {

    lateinit var model: QuesViewModel
    lateinit var binding: ActivityQuestionBinding
    private var answers = arrayListOf(" "," "," "," "," "," "," "," "," "," ")
    private lateinit var quiz: Quiz
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this , R.layout.activity_question)

        model = ViewModelProvider(this).get(QuesViewModel::class.java)

        val getQuiz: Bundle? = intent.extras
        val response = getQuiz?.get("quiz")

        quiz = parseQuizResponse(response.toString())

        binding.tvQuesNo.setText((model.getQuesNo()!! + 1).toString())


        binding.tvQues.setText(quiz.quiz.get(model.getQuesNo()!!).question.toString())
        binding.optA.setText(quiz.quiz.get(model.getQuesNo()!!).options.get(0).toString())
        binding.optB.setText(quiz.quiz.get(model.getQuesNo()!!).options.get(1).toString())
        binding.optC.setText(quiz.quiz.get(model.getQuesNo()!!).options.get(2).toString())
        binding.optD.setText(quiz.quiz.get(model.getQuesNo()!!).options.get(3).toString())


    }

    fun nextQ(quiz: Quiz){
        model.goToNext()
        binding.radioOptions.clearCheck()
        Log.d("quesNo" ,model.quesNo.value.toString())

        //getting answer
        if(answers[model.getQuesNo()!!] == binding.optA.text.toString()){
            binding.optA.isChecked = true
        }
        if(answers[model.getQuesNo()!!] == binding.optB.text.toString()){
            binding.optB.isChecked = true
        }
        if(answers[model.getQuesNo()!!] == binding.optC.text.toString()){
            binding.optC.isChecked = true
        }
        if(answers[model.getQuesNo()!!] == binding.optD.text.toString()){
            binding.optD.isChecked = true
        }


        // setting answer
        if(binding.optA.isChecked){
            answers[model.getQuesNo()!!] = binding.optA.text.toString()
        }
        if(binding.optB.isChecked){
            answers[model.getQuesNo()!!] = binding.optB.text.toString()
        }
        if(binding.optC.isChecked){
            answers[model.getQuesNo()!!] = binding.optC.text.toString()
        }
        if(binding.optD.isChecked){
            answers[model.getQuesNo()!!] = binding.optD.text.toString()
        }


        // for correct
//        if(answers[model.getQuesNo()!!] == quiz.quiz.get(quesNo).correctAnswer.toString()){
//            model.increaseScore()
//        }
        Log.d("Score" ,answers[model.getQuesNo()!!].toString())

        binding.tvQuesNo.setText((model.getQuesNo()!! + 1).toString())


        binding.tvQues.setText(quiz.quiz.get(model.getQuesNo()!!).question.toString())
        binding.optA.setText(quiz.quiz.get(model.getQuesNo()!!).options.get(0).toString())
        binding.optB.setText(quiz.quiz.get(model.getQuesNo()!!).options.get(1).toString())
        binding.optC.setText(quiz.quiz.get(model.getQuesNo()!!).options.get(2).toString())
        binding.optD.setText(quiz.quiz.get(model.getQuesNo()!!).options.get(3).toString())

//        startActivity(Intent(this, QuestionActivity::class.java))
//        finish()

    }

    fun prevQ(quiz: Quiz){
        model.goToPrev()
        binding.radioOptions.clearCheck()
        Log.d("quesNo" ,model.quesNo.value.toString())

        //getting answer
        if(answers[model.getQuesNo()!!] == binding.optA.text.toString()){
            binding.optA.isChecked = true
        }
        if(answers[model.getQuesNo()!!] == binding.optB.text.toString()){
            binding.optB.isChecked = true
        }
        if(answers[model.getQuesNo()!!] == binding.optC.text.toString()){
            binding.optC.isChecked = true
        }
        if(answers[model.getQuesNo()!!] == binding.optD.text.toString()){
            binding.optD.isChecked = true
        }


        //setting answer
        if(binding.optA.isChecked){
            answers[model.getQuesNo()!!] = binding.optA.text.toString()
        }
        if(binding.optB.isChecked){
            answers[model.getQuesNo()!!] = binding.optB.text.toString()
        }
        if(binding.optC.isChecked){
            answers[model.getQuesNo()!!] = binding.optC.text.toString()
        }
        if(binding.optD.isChecked){
            answers[model.getQuesNo()!!] = binding.optD.text.toString()
        }


        // for correct

//        if(answered == quiz.quiz.get(quesNo).correctAnswer.toString()){
//            model.increaseScore()
//        }
//        Log.d("Score" ,model.score.value.toString())

        Log.d("Score" ,answers[model.getQuesNo()!!].toString())


        binding.tvQuesNo.setText((model.getQuesNo()!! + 1).toString())


        binding.tvQues.setText(quiz.quiz.get(model.getQuesNo()!!).question.toString())
        binding.optA.setText(quiz.quiz.get(model.getQuesNo()!!).options.get(0).toString())
        binding.optB.setText(quiz.quiz.get(model.getQuesNo()!!).options.get(1).toString())
        binding.optC.setText(quiz.quiz.get(model.getQuesNo()!!).options.get(2).toString())
        binding.optD.setText(quiz.quiz.get(model.getQuesNo()!!).options.get(3).toString())

//        startActivity(Intent(this, QuestionActivity::class.java))
//        finish()


    }

    private fun parseQuizResponse(jsonResponse: String): Quiz {

        val gson = Gson()
        val quizType = object: TypeToken<Quiz>(){}.type

        return gson.fromJson(jsonResponse , quizType)
    }

    override fun onStart() {
        super.onStart()

        binding.btnNext.setOnClickListener {
            nextQ(quiz)
        }

        binding.btnPrev.setOnClickListener {
            prevQ(quiz)
        }


    }
}