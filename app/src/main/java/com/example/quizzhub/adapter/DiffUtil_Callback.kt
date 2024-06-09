package com.example.quizzhub.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.quizzhub.model.SavedQuestion

class DiffUtil_Callback(
    private val oldList: List<SavedQuestion>,
    private val newList: List<SavedQuestion>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}