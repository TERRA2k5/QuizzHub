package com.example.quizzhub

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.quizzhub.databinding.ActivityResultBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class ResultActivity : AppCompatActivity() {

    lateinit var binding: ActivityResultBinding
    private lateinit var response: String
    private var answers: ArrayList<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_result)

        val getData: Bundle? = intent.extras

        val score: Int = getData?.get("score").hashCode()
        val minLeft: Int = getData?.get("minLeft").hashCode()
        val secLeft: Int = getData?.get("secLeft").hashCode()
        answers = getData?.getStringArrayList("answers")
        response = getData?.get("response").toString()

        //setting time left
        if(minLeft == -1){
            binding.ResMin.setText("OOF")
            binding.ResSec.visibility = View.GONE
            binding.ResTime.visibility = View.GONE
        }
        else{
            binding.ResMin.setText(minLeft.toString())
            binding.ResSec.setText(secLeft.toString())
        }

        val incorrectAnswers = 10 - score

        binding.correctAns.text = score.toString()

        val entries = ArrayList<PieEntry>()

        entries.add(PieEntry(score.toFloat(), "Correct"))
        entries.add(PieEntry(incorrectAnswers.toFloat(), "Incorrect"))


        val dataSet = PieDataSet(entries , " ")
        dataSet.colors = listOf(Color.GREEN, Color.RED)
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 10f


        // Create the pie data object
        val data = PieData(dataSet)

        // Configure the pie chart
        binding.pieChart.data = data
        binding.pieChart.setUsePercentValues(true)
        binding.pieChart.description = Description().apply { text = "Results" }
        binding.pieChart.description.textColor = Color.WHITE
        binding.pieChart.isDrawHoleEnabled = true
        binding.pieChart.holeRadius = 50f
        binding.pieChart.setHoleColor(Color.TRANSPARENT)
        binding.pieChart.setEntryLabelTextSize(12f)
        binding.pieChart.setEntryLabelColor(Color.WHITE)
        binding.pieChart.animateY(1000 , Easing.EaseInOutCubic)
    }


    override fun onStart() {
        super.onStart()

        binding.btnReturn.setOnClickListener {
            finishAffinity()
            startActivity(Intent(this , MainActivity::class.java))
            finish()
        }

        binding.btnAnalyse.setOnClickListener {
            val intent = Intent(this , AnalyseActivity::class.java)
            intent.putExtra("response" , response)
            intent.putExtra("answers", answers)
            startActivity(intent)
        }
    }
}