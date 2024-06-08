package com.example.quizzhub.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzhub.R
import com.example.quizzhub.model.BookmarkViewModel
import com.example.quizzhub.model.SavedQuestion

class BookmarkAdapter(private val viewModel: BookmarkViewModel): RecyclerView.Adapter<BookmarkAdapter.MyViewHolder>() {

    private lateinit var mListerner: onClickListener
    private var bookmarks = emptyList<SavedQuestion>()

    interface onClickListener {
        fun onClick(position: Int)
    }

    fun setOnClickListener(listener: onClickListener) {

        mListerner = listener

    }

    inner class MyViewHolder(item: View, listener: BookmarkAdapter.onClickListener): RecyclerView.ViewHolder(item){

        var quesNo: TextView = item.findViewById(R.id.tvQuesNoCardBook)
        var quesTitle: TextView = item.findViewById(R.id.tvQuestionCardBook)
        var correctAns: TextView = item.findViewById(R.id.tvCorrectAnsBook)
        var checkBox: CheckBox = item.findViewById(R.id.BookCheck)

        init {

            quesNo = item.findViewById(R.id.tvQuesNoCardBook)
            quesTitle = item.findViewById(R.id.tvQuestionCardBook)
            correctAns = item.findViewById(R.id.tvCorrectAnsBook)
            checkBox = item.findViewById(R.id.BookCheck)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val v =LayoutInflater.from(parent.context).inflate(R.layout.bookmark_card, parent, false)

        return MyViewHolder(v , mListerner)
    }

    override fun getItemCount(): Int {
        return bookmarks.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.itemView.setOnClickListener(){
            mListerner.onClick(position)
        }

        val item = bookmarks[position]

        holder.correctAns.text = item.correctAnswer
        holder.quesNo.text = (1+position).toString()
        holder.quesTitle.text = item.question

        holder.checkBox.isChecked = true

        holder.checkBox.setOnCheckedChangeListener { checkBox, isChecked ->

            if (isChecked){
                viewModel.insert(item)
            }
            else{
                viewModel.delete(item)
            }
        }
    }

    internal fun setBookmarks(bookmarks: List<SavedQuestion>) {
        this.bookmarks = bookmarks
        notifyDataSetChanged()
    }
}