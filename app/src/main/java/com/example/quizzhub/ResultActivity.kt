package com.example.quizzhub

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.quizzhub.databinding.ActivityResultBinding
import com.github.mikephil.charting.animation.Easing

class ResultActivity : AppCompatActivity() {

    lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_result)

        binding.pieChart.description.isEnabled = false


        binding.pieChart.isDrawHoleEnabled = true

        binding.pieChart.setHoleColor(Color.TRANSPARENT)
        binding.pieChart.animateY(1000 , Easing.EaseInOutCubic)
    }
}