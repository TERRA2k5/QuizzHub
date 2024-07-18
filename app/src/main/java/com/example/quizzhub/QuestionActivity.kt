package com.example.quizzhub

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.quizzhub.databinding.ActivityQuestionBinding
import com.example.quizzhub.model.QuesViewModel
import com.example.quizzhub.model.Quiz
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QuestionActivity : AppCompatActivity() {

    private lateinit var model: QuesViewModel
    lateinit var binding: ActivityQuestionBinding
    private lateinit var quiz: Quiz
    private lateinit var timer: CountDownTimer
    private lateinit var response: String
    override fun onBackPressed() {

        if(model.getMinute() >= 0) timer.cancel()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
        super.onBackPressed()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_question)

//        Toast.makeText(this, "create", Toast.LENGTH_SHORT).show()
        model = ViewModelProvider(this).get(QuesViewModel::class.java)

        val getData: Bundle? = intent.extras
        response = getData?.get("quiz").toString()

        // setting timer only on FIRST

        if(model.getFirst() == 0){
            model.noFirst()
            model.setMinute(getData?.get("time").hashCode())
        }

        // timer off text
        if(model.getMinute() == -1){
            binding.tvSec.setText("OFF")
            binding.tvTime.visibility = View.GONE
            binding.tvMin.visibility = View.GONE
        }


        Log.d("Timer" , model.getMinute().toString())

        // generating response
        try {
            quiz = parseQuizResponse(response.toString())
            binding.tvQuesNo.setText((model.getQuesNo()!! + 1).toString())
            binding.btnPrev.isEnabled = false

            //1st question
            binding.tvQues.setText(quiz.quiz.get(model.getQuesNo()!!).question.toString())
            binding.optA.setText(quiz.quiz.get(model.getQuesNo()!!).options.get(0).toString())
            binding.optB.setText(quiz.quiz.get(model.getQuesNo()!!).options.get(1).toString())
            binding.optC.setText(quiz.quiz.get(model.getQuesNo()!!).options.get(2).toString())
            binding.optD.setText(quiz.quiz.get(model.getQuesNo()!!).options.get(3).toString())


            // setting timmer:
            if(model.getMinute() >= 0){
                runTimer()
                timer.start()
            }
        }catch (e: Exception){
            Log.e("Generation" , "Un-identified object in response", e)
            Toast.makeText(this, "Something went wrong.\nTry Again.", Toast.LENGTH_SHORT).show()
            finishAffinity()
            startActivity(Intent(this, MainActivity::class.java))
        }

    }

    private fun runTimer(){
        val time : Long = 1000*(model.getMinute()*60L + model.getSecond())
        timer = object : CountDownTimer( time, 1000){
            override fun onTick(millisUntilFinished: Long) {
                val sec: Int = (millisUntilFinished.toInt()/1000)%60
                model.setSecond(sec)
                val min: Int = (millisUntilFinished.toInt()/1000)/60
                model.setMinute(min)

                if(sec >= 10) binding.tvSec.setText(sec.toString())
                else binding.tvSec.setText("0$sec")

                if(min >= 10) binding.tvMin.setText(min.toString())
                else binding.tvMin.setText("0$min")
            }

            override fun onFinish() {

                //setting up last select option

                if(binding.optA.isChecked){
                    model.updateAnswer(model.getQuesNo()!! , binding.optA.text.toString())
                }
                if(binding.optB.isChecked){
                    model.updateAnswer(model.getQuesNo()!! , binding.optB.text.toString())
                }
                if(binding.optC.isChecked){
                    model.updateAnswer(model.getQuesNo()!! , binding.optC.text.toString())
                }
                if(binding.optD.isChecked){
                    model.updateAnswer(model.getQuesNo()!! , binding.optD.text.toString())
                }

                // calculate score & show result

                calculateScore()

                val finalScore = model.getScore()
                val intent = Intent(this@QuestionActivity , ResultActivity::class.java)
                intent.putExtra("score" , finalScore)
                intent.putExtra("minLeft", model.getMinute())
                intent.putExtra("secLeft", model.getSecond())
                intent.putExtra("response" , response)
                intent.putExtra("answers", model.getAllAns())
                startActivity(intent)
                finish()
                if(model.getMinute() >= 0) timer.cancel()
            }

        }
    }

    fun nextQ(quiz: Quiz){

        // setting answer
        if(binding.optA.isChecked){
            model.updateAnswer(model.getQuesNo()!! , binding.optA.text.toString())
        }
        if(binding.optB.isChecked){
            model.updateAnswer(model.getQuesNo()!! , binding.optB.text.toString())
        }
        if(binding.optC.isChecked){
            model.updateAnswer(model.getQuesNo()!! , binding.optC.text.toString())
        }
        if(binding.optD.isChecked){
            model.updateAnswer(model.getQuesNo()!! , binding.optD.text.toString())
        }

        if(model.getQuesNo()!! == 9){

            calculateScore()

            val finalScore = model.getScore()
            val intent = Intent(this , ResultActivity::class.java)
            intent.putExtra("score" , finalScore)
            intent.putExtra("minLeft", model.getMinute())
            intent.putExtra("secLeft", model.getSecond())
            intent.putExtra("response" , response)
            intent.putExtra("answers", model.getAllAns())
            startActivity(intent)
            finish()
            if(model.getMinute() >= 0) timer.cancel()
        }
        binding.btnPrev.isEnabled = true


        if(model.getQuesNo()!! == 9) return

        model.goToNext()
        val quesNo: Int = model.getQuesNo()!!
        if(quesNo == 9) binding.btnNext.setText("Submit")
        binding.radioOptions.clearCheck()

        binding.tvQuesNo.setText((model.getQuesNo()!! + 1).toString())


        binding.tvQues.setText(quiz.quiz.get(model.getQuesNo()!!).question.toString())
        binding.optA.setText(quiz.quiz.get(model.getQuesNo()!!).options.get(0).toString())
        binding.optB.setText(quiz.quiz.get(model.getQuesNo()!!).options.get(1).toString())
        binding.optC.setText(quiz.quiz.get(model.getQuesNo()!!).options.get(2).toString())
        binding.optD.setText(quiz.quiz.get(model.getQuesNo()!!).options.get(3).toString())



//        Log.d("quesNo" ,model.quesNo.value.toString())

        //getting answer
        if(model.getAnswer(quesNo) == binding.optA.text.toString()){
            binding.radioOptions.check(binding.optA.id)
        }
        if(model.getAnswer(quesNo) == binding.optB.text.toString()){
            binding.radioOptions.check(binding.optB.id)
        }
        if(model.getAnswer(quesNo) == binding.optC.text.toString()){
            binding.radioOptions.check(binding.optC.id)
        }
        if(model.getAnswer(quesNo) == binding.optD.text.toString()){
            binding.radioOptions.check(binding.optD.id)
        }

    }

    fun prevQ(quiz: Quiz){

        // setting answer
        if(binding.optA.isChecked){
            model.updateAnswer(model.getQuesNo()!! , binding.optA.text.toString())
        }
        if(binding.optB.isChecked){
            model.updateAnswer(model.getQuesNo()!! , binding.optB.text.toString())
        }
        if(binding.optC.isChecked){
            model.updateAnswer(model.getQuesNo()!! , binding.optC.text.toString())
        }
        if(binding.optD.isChecked){
            model.updateAnswer(model.getQuesNo()!! , binding.optD.text.toString())
        }

        model.goToPrev()

        val quesNo :Int = model.getQuesNo()!!
        if(quesNo == 0) binding.btnPrev.isEnabled = false
        binding.btnNext.setText("Next")
//        Log.d("quesNo" ,quesNo.toString())

        binding.radioOptions.clearCheck()

        binding.tvQuesNo.setText((model.getQuesNo()!! + 1).toString())


        binding.tvQues.setText(quiz.quiz.get(model.getQuesNo()!!).question.toString())
        binding.optA.setText(quiz.quiz.get(model.getQuesNo()!!).options.get(0).toString())
        binding.optB.setText(quiz.quiz.get(model.getQuesNo()!!).options.get(1).toString())
        binding.optC.setText(quiz.quiz.get(model.getQuesNo()!!).options.get(2).toString())
        binding.optD.setText(quiz.quiz.get(model.getQuesNo()!!).options.get(3).toString())


        //getting answer
        if(model.getAnswer(quesNo) == binding.optA.text.toString()){
//            Toast.makeText(this, "toaastttt", Toast.LENGTH_SHORT).show()
            binding.radioOptions.check(binding.optA.id)
        }
        if(model.getAnswer(quesNo) == binding.optB.text.toString()){
            binding.radioOptions.check(binding.optB.id)
        }
        if(model.getAnswer(quesNo) == binding.optC.text.toString()){
            binding.radioOptions.check(binding.optC.id)
        }
        if(model.getAnswer(quesNo) == binding.optD.text.toString()){
            binding.radioOptions.check(binding.optD.id)
        }

    }

    private fun parseQuizResponse(jsonResponse: String): Quiz {

        val gson = Gson()
        val quizType = object: TypeToken<Quiz>(){}.type

        return gson.fromJson(jsonResponse , quizType)
    }

    fun calculateScore(){

        if(model.getAnswer(0) == quiz.quiz.get(0).correctAnswer){
            model.increaseScore()
//            model.updateAnswer(0, "1")
        }
        if(model.getAnswer(1) == quiz.quiz.get(1).correctAnswer){
            model.increaseScore()
//            model.updateAnswer(1, "1")
        }
        if(model.getAnswer(2) == quiz.quiz.get(2).correctAnswer){
            model.increaseScore()
//            model.updateAnswer(2, "1")
        }
        if(model.getAnswer(3) == quiz.quiz.get(3).correctAnswer){
            model.increaseScore()
//            model.updateAnswer(3, "1")
        }
        if(model.getAnswer(4) == quiz.quiz.get(4).correctAnswer){
            model.increaseScore()
//            model.updateAnswer(4, "1")
        }
        if(model.getAnswer(5) == quiz.quiz.get(5).correctAnswer){
            model.increaseScore()
//            model.updateAnswer(5, "1")
        }
        if(model.getAnswer(6) == quiz.quiz.get(6).correctAnswer){
            model.increaseScore()
//            model.updateAnswer(6, "1")
        }
        if(model.getAnswer(7) == quiz.quiz.get(7).correctAnswer){
            model.increaseScore()
//            model.updateAnswer(7, "1")
        }
        if(model.getAnswer(8) == quiz.quiz.get(8).correctAnswer){
            model.increaseScore()
//            model.updateAnswer(8, "1")
        }
        if(model.getAnswer(9) == quiz.quiz.get(9).correctAnswer){
            model.increaseScore()
//            model.updateAnswer(9, "1")
        }
    }

    override fun onStart() {
        super.onStart()

//        Toast.makeText(this, "start", Toast.LENGTH_SHORT).show()
        binding.btnNext.setOnClickListener {
            nextQ(quiz)
        }

        binding.btnPrev.setOnClickListener {
            prevQ(quiz)
        }

//        if(model.getMinute() >= 0){
//            timer.start()
//        }

        binding.btnClear.setOnClickListener {
            model.updateAnswer(model.getQuesNo()!! , "Not Answered")
            binding.radioOptions.clearCheck()
        }

    }
}