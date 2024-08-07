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
import com.example.quizzhub.ResultActivity
import com.example.quizzhub.databinding.FragmentHomeBinding
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    var response: String? = null

    var time: Int = 0
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.progressBar.visibility = View.GONE
        val timer = resources.getStringArray(R.array.timer)

        binding.spinerTimer.isEnabled = false

        binding.toggleButton.setOnCheckedChangeListener { checkbox, isChecked ->
            if (isChecked) {
                binding.spinerTimer.isEnabled = true
            } else {
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
            ) {
                val prompt = timer[position].toString()
                when (prompt) {
                    "2 min" -> time = 2
                    "5 min" -> time = 5
                    "10 min" -> time = 10
                    "15 min" -> time = 15
                    "30 min" -> time = 30
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }


        }

        binding.btnGenerate.setOnClickListener {
            if (binding.spinerTimer.isEnabled == false) {
                time = -1
            }
            binding.btnGenerate.isEnabled = false
            binding.progressBar.visibility = View.VISIBLE
            val topic = binding.etTopic.text.toString()


            val generativeModel = GenerativeModel(
                // For text-only input, use the gemini-pro model
                modelName = "gemini-pro",
                // Access your API key as a Build Configuration variable (see "Set up your API key" above)
                apiKey = "AIzaSyBtnEUCexEGK6w_PWxZPxkqPRsGOntO1bM",
            )
//            GlobalScope.launch(Dispatchers.IO) { val ss = generativeModel.generateContent("hello ? ").text.toString()
//            Log.i("hahah" , ss)}


            GlobalScope.launch(Dispatchers.IO) {
                try {
                    var prompt =
                        "Make 10 MCQ on topic $topic in JSON format under list named 'quiz' and give 4 options under list named 'options' and also provide 'correctAnswer' for each.(carefully use quotation mark in key value pair.)  DO NOT USE WORD JSON AT BEGINNING"
                    if(topic == ""){
                        prompt = "Make 10 MCQ on random topic in JSON format under list named 'quiz' and give 4 options under list named 'options' and also provide 'correctAnswer' for each.(carefully use quotation mark in key value pair.)  DO NOT USE WORD JSON AT BEGINNING"
                    }
                    response = generativeModel.generateContent(prompt).text.toString()
                } catch (e: Exception) {
                    Log.e("Generation", "Could not generate quiz")
//                Toast.makeText(context, "Check Your Network Connection", Toast.LENGTH_SHORT).show()
                }
                val response1 = response.toString().replace("```", "")
//                val res = response1.replace("json", "")
//                val response2 = res.replace("JSON", "")
                Log.i("TAGY", response1)

                val i = Intent(context, QuestionActivity::class.java)
                i.putExtra("quiz", response1)
                i.putExtra("time", time)
                startActivity(i)
                activity?.finish()
            }


        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()

        binding.btnGenerate.isEnabled = true
        binding.progressBar.visibility = View.GONE
    }

}