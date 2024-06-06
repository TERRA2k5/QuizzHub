package com.example.quizzhub

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizzhub.databinding.ActivityAnalyseBinding
import com.example.quizzhub.model.QuesViewModel
import com.example.quizzhub.model.Quiz
import com.example.quizzhub.model.RecyclerModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AnalyseActivity : AppCompatActivity() {

    lateinit var quiz: Quiz
    lateinit var binding: ActivityAnalyseBinding
    private lateinit var model: QuesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_analyse)

        model = ViewModelProvider(this).get(QuesViewModel::class.java)

        binding = DataBindingUtil.setContentView(this , R.layout.activity_analyse)

        binding.analyseLoading.visibility = View.GONE

        val getData: Bundle? = intent.extras
        val response = getData?.get("response").toString()
        val answers: ArrayList<String> = getData?.getStringArrayList("answers")!!
        quiz = parseQuizResponse(response)




        val data: ArrayList<RecyclerModel> = ArrayList()

        data.add(RecyclerModel(answers[0].toString(), quiz.quiz.get(0).correctAnswer.toString() , quiz.quiz.get(0).question.toString()))
        data.add(RecyclerModel(answers[1].toString(), quiz.quiz.get(1).correctAnswer.toString() , quiz.quiz.get(1).question.toString()))
        data.add(RecyclerModel(answers[2].toString(), quiz.quiz.get(2).correctAnswer.toString() , quiz.quiz.get(2).question.toString()))
        data.add(RecyclerModel(answers[3].toString(), quiz.quiz.get(3).correctAnswer.toString() , quiz.quiz.get(3).question.toString()))
        data.add(RecyclerModel(answers[4].toString(), quiz.quiz.get(4).correctAnswer.toString() , quiz.quiz.get(4).question.toString()))
        data.add(RecyclerModel(answers[5].toString(), quiz.quiz.get(5).correctAnswer.toString() , quiz.quiz.get(5).question.toString()))
        data.add(RecyclerModel(answers[6].toString(), quiz.quiz.get(6).correctAnswer.toString() , quiz.quiz.get(6).question.toString()))
        data.add(RecyclerModel(answers[7].toString(), quiz.quiz.get(7).correctAnswer.toString() , quiz.quiz.get(7).question.toString()))
        data.add(RecyclerModel(answers[8].toString(), quiz.quiz.get(8).correctAnswer.toString() , quiz.quiz.get(8).question.toString()))
        data.add(RecyclerModel(answers[9].toString(), quiz.quiz.get(9).correctAnswer.toString() , quiz.quiz.get(9).question.toString()))

        val adapter = AnalysisAdapter(data)
        binding.recyclerView.adapter = adapter

        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false)

        adapter.setOnClickListener(object : AnalysisAdapter.onClickListener{
            override fun onClick(position: Int) {

//                Toast.makeText(this@AnalyseActivity, "touchhh", Toast.LENGTH_SHORT).show()
                binding.analyseLoading.visibility = View.VISIBLE
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                val generativeModel = GenerativeModel(
                    // For text-only input, use the gemini-pro model
                    modelName = "gemini-pro",
                    // Access your API key as a Build Configuration variable (see "Set up your API key" above)
                    apiKey = "AIzaSyB6qGuM47R6uiLIdfL0dJ4XdSOCE6OvNwc"
                )

                GlobalScope.launch(Dispatchers.IO) {
                    val prompt = quiz.quiz[position].question + " (Explain why correct answer must be ${quiz.quiz[position].correctAnswer} in 50-100 words.)"
                    val response = generativeModel.generateContent(prompt).text.toString()
                    val intent = Intent(this@AnalyseActivity , DetailActivity::class.java)
                    intent.putExtra("question", quiz.quiz.get(position).question)
                    intent.putExtra("correct", quiz.quiz.get(position).correctAnswer)
                    intent.putExtra("answer", answers[position])
                    intent.putExtra("response",response)
                    intent.putExtra("number",(position+1).toString())
                    startActivity(intent)
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        binding.analyseLoading.visibility = View.GONE
    }


    private fun parseQuizResponse(jsonResponse: String): Quiz {

        val gson = Gson()
        val quizType = object: TypeToken<Quiz>(){}.type

        return gson.fromJson(jsonResponse , quizType)
    }
}