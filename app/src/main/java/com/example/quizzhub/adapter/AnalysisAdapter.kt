package com.example.quizzhub.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzhub.R
import com.example.quizzhub.model.RecyclerModel


class AnalysisAdapter(val itemList: ArrayList<RecyclerModel>): RecyclerView.Adapter<AnalysisAdapter.MyViewHolder>() {

    private lateinit var mListerner: onClickListener

    interface onClickListener{
        fun onClick(position: Int)
    }

    fun setOnClickListener(listener: onClickListener){

        mListerner = listener

    }

    inner class MyViewHolder(item: View , listener: onClickListener): RecyclerView.ViewHolder(item){

        var quesNo: TextView = item.findViewById(R.id.tvQuesNoCard)
        var quesTitle: TextView = item.findViewById(R.id.tvQuestionCard)
        var yourAns: TextView = item.findViewById(R.id.tvYourAns)
        var correctAns: TextView = item.findViewById(R.id.tvCorrectAns)
        var status: ImageView = item.findViewById(R.id.icon_status)

        init {

            quesNo = item.findViewById(R.id.tvQuesNoCard)
            quesTitle = item.findViewById(R.id.tvQuestionCard)
            yourAns = item.findViewById(R.id.tvYourAns)
            correctAns = item.findViewById(R.id.tvCorrectAns)
            status = item.findViewById(R.id.icon_status)



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val v =LayoutInflater.from(parent.context).inflate(R.layout.ques_card, parent, false)

        return MyViewHolder(v , mListerner)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.quesNo.text = (position+1).toString()
        holder.quesTitle.text = itemList[position].question
        holder.correctAns.text = itemList[position].correctAnswer


        if(itemList[position].yourAns == "1"){
            holder.yourAns.text = itemList[position].correctAnswer
            holder.status.setImageResource(R.drawable.tick_icon)
        }
        else{
            holder.yourAns.text = itemList[position].yourAns
        }

        holder.itemView.setOnClickListener(){
            mListerner.onClick(position)
        }
    }
}