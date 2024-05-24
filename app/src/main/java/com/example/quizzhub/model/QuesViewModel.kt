package com.example.quizzhub.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class QuesViewModel: ViewModel() {

    var quesNo = MutableLiveData<Int>().apply { value = 0 }
    var score = MutableLiveData<Int>().apply { value = 0 }
    var answers = arrayListOf(" "," "," "," "," "," "," "," "," "," ")

    fun goToNext(){
        quesNo.value = quesNo.value?.plus(1)
    }

    fun goToPrev(){
        quesNo.value = quesNo.value?.minus(1)
    }

    fun getQuesNo():Int?{
        return quesNo.value
    }

    fun updateAnswer(ques: Int){
        answers[ques]
    }

    fun increaseScore(){
        score.value = score.value?.plus(1)
    }
}

