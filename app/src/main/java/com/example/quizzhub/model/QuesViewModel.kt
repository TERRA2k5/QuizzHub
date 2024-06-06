package com.example.quizzhub.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class QuesViewModel: ViewModel() {

    private var quesNo = MutableLiveData<Int>().apply { value = 0 }
    private var first = MutableLiveData<Int>().apply { value = 0 }
    private var score = MutableLiveData<Int>().apply { value = 0 }
    private var answers = arrayListOf("Not Answered","Not Answered","Not Answered","Not Answered","Not Answered","Not Answered","Not Answered","Not Answered","Not Answered","Not Answered")
    private var minute = MutableLiveData<Int>().apply { value = 0 }
    private var second = MutableLiveData<Int>().apply { value = 0 }

    fun getAllAns(): ArrayList<String>{
        return answers
    }

    fun noFirst(){
        first.value = first.value?.plus(1)
    }

    fun getFirst():Int{
        return first.value!!
    }
    fun goToNext(){
        quesNo.value = quesNo.value?.plus(1)
    }

    fun goToPrev(){
        quesNo.value = quesNo.value?.minus(1)
    }

    fun getQuesNo():Int?{
        return quesNo.value
    }

    fun updateAnswer(ques: Int , value: String){
        answers[ques] = value
    }

    fun getAnswer(ques: Int): String{
        return answers[ques]
    }

    fun setMinute(input: Int){
        minute.value = input
    }

    fun getMinute(): Int{
        return minute.value!!
    }

    fun setSecond(input: Int){
        second.value = input
    }

    fun getSecond(): Int{
        return second.value!!
    }

    fun getScore(): Int{
        return score.value!!
    }

    fun increaseScore(){
        score.value = score.value?.plus(1)
    }
}

