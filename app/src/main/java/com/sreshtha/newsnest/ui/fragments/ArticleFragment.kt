package com.sreshtha.newsnest.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.sreshtha.newsnest.databinding.FragmentArticleBinding
import com.sreshtha.newsnest.model.Article
import com.sreshtha.newsnest.ui.MainActivity
import com.sreshtha.newsnest.viewmodel.NewsViewModel

class ArticleFragment : Fragment() {

    private lateinit var viewModel: NewsViewModel
    private var binding:FragmentArticleBinding?= null
    val args:ArticleFragmentArgs by navArgs()
    private lateinit var article:Article

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater,container,false)
        viewModel = (activity as MainActivity).viewModel
        article = args.article
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.webView?.apply {
                webViewClient = WebViewClient()
                loadUrl(article.url)
        }

        binding?.fab?.setOnClickListener {
            viewModel.insert(article)
            Snackbar.make(view,"Article Saved Successfully",Snackbar.LENGTH_SHORT).apply {
                setAction("Undo"){
                    viewModel.delete(article)
                }
                show()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }




}