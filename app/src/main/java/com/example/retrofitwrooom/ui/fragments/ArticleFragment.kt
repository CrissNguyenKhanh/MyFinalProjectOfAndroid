package com.example.retrofitwrooom.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.databinding.FragmentArticleBinding
import com.example.retrofitwrooom.Activity.NewsActivity
import com.example.retrofitwrooom.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar


class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var newsViewModel: NewsViewModel
    val args :ArticleFragmentArgs by navArgs()
    lateinit var  binding :FragmentArticleBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)

        newsViewModel = (activity  as NewsActivity).newsViewModel
        val article = args.article

        binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.let {
                loadUrl(it)
            }
        }
        binding.fab.setOnClickListener {
            newsViewModel.addToFavourite(article)
            Snackbar.make(view , "added to favourite" ,Snackbar.LENGTH_SHORT).show()

        }
    }
}