package com.example.quizzhub.mainFragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.quizzhub.QuestionActivity
import com.example.quizzhub.model.Quiz
import com.example.quizzhub.R
import com.example.quizzhub.databinding.FragmentHomeBinding
import com.google.ai.client.generativeai.GenerativeModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    // api key            AIzaSyB6qGuM47R6uiLIdfL0dJ4XdSOCE6OvNwc
    private lateinit var binding: FragmentHomeBinding
//    lateinit var quiz: Quiz


    var time: Int? = null
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_home, container,false)


        val timer = resources.getStringArray(R.array.timer)

        binding.spinerTimer.isEnabled = false

        binding.toggleButton.setOnCheckedChangeListener{ checkbox , isChecked ->
            if(isChecked){
                binding.spinerTimer.isEnabled = true
            }
            else{
                binding.spinerTimer.isEnabled = false
            }
        }

        if (binding.spinerTimer != null) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1, timer
            )
            binding.spinerTimer.adapter = adapter
        }

        binding.spinerTimer.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ){
                val prompt = timer[position].toString()
                when(prompt){
                    "5"-> time = 5
                    "10"-> time = 10
                    "15"-> time = 15
                    "30"-> time = 30
                    "45"-> time = 45
                    "60"-> time = 60
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }


        }

        binding.btnGenerate.setOnClickListener {
            val topic = binding.etTopic.text.toString()

            val generativeModel = GenerativeModel(
                // For text-only input, use the gemini-pro model
                modelName = "gemini-pro",
                // Access your API key as a Build Configuration variable (see "Set up your API key" above)
                apiKey = "AIzaSyB6qGuM47R6uiLIdfL0dJ4XdSOCE6OvNwc"
            )

//            GlobalScope.launch(Dispatchers.IO) {
//                val prompt = "Make 10 MCQ on topic $topic in JSON format under list named 'quiz'"
//                val response = generativeModel.generateContent(prompt)
//                val response1 = response.text.toString().replace("```" , "")
//                val response2 = response1.replace("json" , "")
//                Log.i("TAGY1" , response2)
//
//                val moshi = Moshi.Builder().build()
//                val quizAdapter: JsonAdapter<Quiz> = moshi.adapter(Quiz::class.java)
//                val quiz = quizAdapter.fromJson(response2)
//
//                Log.i("TAGY" , quiz!!.quiz.get(0).question.toString())
////                val quiz: Quiz = Json.decodeFromString(response.text.toString())
////
////                val list = listOf(quiz)
////                val item = list[0].get(0).question.toString()
////
////                Log.i("TAGY" , item)
//            }

            GlobalScope.launch(Dispatchers.IO) {
                val prompt = "Make 10 MCQ on topic $topic in JSON format under list named 'quiz' and give 4 options under list named 'options' and also provide 'correctAnswer' for each.(carefully use quotation mark in key value pair.)"
                val response = generativeModel.generateContent(prompt)
                val response1 = response.text.toString().replace("```" , "")
                val res = response1.replace("json" , "")
                val response2 = res.replace("JSON" , "")
                Log.i("TAGY1" , response2)

//                quiz = parseQuizResponse(response2)

//                Log.i("TAGY" , quiz.quiz.get(0).correctAnswer.toString())

                val i = Intent(context , QuestionActivity::class.java)
                i.putExtra("quiz" , response2)
                startActivity(i)
            }
        }

        return binding.root
    }

}