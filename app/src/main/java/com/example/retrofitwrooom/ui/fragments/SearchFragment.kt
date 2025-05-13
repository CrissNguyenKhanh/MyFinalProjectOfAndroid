package com.example.retrofitwrooom.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.adapter.NewsAdapter
import com.example.retrofitwrooom.databinding.FragmentSearchBinding
import com.example.retrofitwrooom.Activity.NewsActivity
import com.example.retrofitwrooom.viewModel.NewsViewModel
import com.example.retrofitwrooom.util.Constants
import com.example.retrofitwrooom.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.retrofitwrooom.util.Resouce
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment(R.layout.fragment_search) {
    lateinit var   newsViewModel: NewsViewModel
    lateinit var   newsAdapter: NewsAdapter
    lateinit var   retryButton: Button
    lateinit var   errorText : TextView
    lateinit var   itemSearchError :CardView
    lateinit var   binding: FragmentSearchBinding
    private  var job :Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSearchBinding.bind(view)
        itemSearchError = view.findViewById(R.id.itemSearchError)

        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view:View = inflater.inflate(R.layout.item_error , null)

        retryButton = view.findViewById(R.id.retryButton)
        errorText = view.findViewById(R.id.errorText)

        newsViewModel = (activity as NewsActivity).newsViewModel
        setupSearchRecycler()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article" , it)
            }
            findNavController().navigate(R.id.action_searchFragment2_to_articleFragment , bundle)
        }

        binding.searchEdit.doOnTextChanged { text, _, _, _ ->
            job?.cancel()
            job = viewLifecycleOwner.lifecycleScope.launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                text?.let {
                    if (it.isNotEmpty()) {
                        newsViewModel.searchNews(it.toString())
                    }
                }
            }
        }
        newsViewModel.SearchNews.observe(viewLifecycleOwner , Observer { response->
            when(response){
                is Resouce.Success<*> ->{
                    hideProgessBar()
                    hideErrorMessage()
                    response.data?.let {newResponse ->
                        newsAdapter.differ.submitList(newResponse.articles.toList())
                        val totalPage = newResponse.totalResults /Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = newsViewModel.searchNewsPage == totalPage
                        if(isLastPage){
                            binding.recyclerSearch.setPadding(0,0,0,0)
                        }
                    }
                }

                is Resouce.Error<*> ->{
                    hideProgessBar()
                    response.message?.let {message ->
                        Toast.makeText(activity, "Sorry error  : $message>>>" , Toast.LENGTH_SHORT ).show()
                        showErrorMessage(message)
                    }
                }
                is Resouce.Loading<*> ->{
                    showProgessBar()
                }

            }

        })
        retryButton.setOnClickListener {
            if(binding.searchEdit.text.toString().isNotEmpty()){
                newsViewModel.searchNews(binding.searchEdit.text.toString())

            }else{
                hideErrorMessage()
            }
        }



    }
    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private fun hideProgessBar(){
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false

    }
    private fun showProgessBar(){
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideErrorMessage(){
        itemSearchError.visibility = View.INVISIBLE
        isError= false
    }

    private  fun showErrorMessage(message:String){
        itemSearchError.visibility = View.VISIBLE
        isError= false
        errorText.text = message
    }

    val scrollListener  = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisiableItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visiableItemcount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoError = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisiableItemPosition + visiableItemcount >= totalItemCount
            val isNotAtBeginning = firstVisiableItemPosition >= 0
            val isTotalMoreThanVisiable = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val showPaginate =
                isNoError && isNotLoadingAndNotLastPage && isNotAtBeginning && isAtLastItem && isTotalMoreThanVisiable && isScrolling
            if (showPaginate) {
                newsViewModel.searchNews(binding.searchEdit.text.toString())
                isScrolling = false
            }


        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }

        }
    }
        private fun setupSearchRecycler(){
            newsAdapter = NewsAdapter()
            binding.recyclerSearch.apply {
                adapter = newsAdapter
                layoutManager = LinearLayoutManager(activity)
                addOnScrollListener(this@SearchFragment.scrollListener)
            }

        }

    }

