package com.example.quizzhub.mainFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizzhub.DetailActivity
import com.example.quizzhub.R
import com.example.quizzhub.adapter.AnalysisAdapter
import com.example.quizzhub.adapter.BookmarkAdapter
import com.example.quizzhub.database.BookmarkRepository
import com.example.quizzhub.database.QuizDatabase
import com.example.quizzhub.databinding.FragmentBookmarkBinding
import com.example.quizzhub.model.BookmarkViewModel
import com.example.quizzhub.model.BookmarkViewModelFactory
import com.example.quizzhub.model.SavedQuestion
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class BookmarkFragment : Fragment() {

    lateinit var binding: FragmentBookmarkBinding
    private lateinit var bookmarkViewModel: BookmarkViewModel
    private lateinit var bookmarkAdapter: BookmarkAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_bookmark , container , false)

        binding.bookRecycler.layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL , false)

        val repository = BookmarkRepository(QuizDatabase.getDatabase(requireContext()))
        val factory = activity?.let { BookmarkViewModelFactory(it.application , repository) }
        val bookmarkViewModel: BookmarkViewModel by viewModels { factory!! }

        bookmarkAdapter = BookmarkAdapter(bookmarkViewModel)

        binding.bookRecycler.adapter = bookmarkAdapter

        bookmarkViewModel.allBookmarks.observe( viewLifecycleOwner , Observer { bookmarks ->
            bookmarks?.let { bookmarkAdapter.setBookmarks(it) }
        })

        bookmarkAdapter.setOnClickListener(object : BookmarkAdapter.onClickListener{
            override fun onClick(position: Int) {
                val intent = Intent(context , DetailActivity::class.java)
                intent.putExtra("number" , (position+1).toString())
                intent.putExtra("question", (bookmarkViewModel.allBookmarks.value?.get(position)?.question))
                intent.putExtra("correct", bookmarkViewModel.allBookmarks.value?.get(position)?.correctAnswer)
                intent.putExtra("response",bookmarkViewModel.allBookmarks.value?.get(position)?.explain)
                startActivity(intent)
            }
        })

        return binding.root
    }
}