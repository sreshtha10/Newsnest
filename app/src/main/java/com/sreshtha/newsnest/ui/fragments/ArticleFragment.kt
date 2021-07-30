package com.sreshtha.newsnest.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.sreshtha.newsnest.databinding.FragmentArticleBinding
import com.sreshtha.newsnest.ui.MainActivity
import com.sreshtha.newsnest.viewmodel.NewsViewModel

class ArticleFragment : Fragment() {

    private lateinit var viewModel: NewsViewModel
    private var binding:FragmentArticleBinding?= null
    val args:ArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater,container,false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        val article = args.article

        try{
            binding?.webView?.apply {
                webViewClient = WebViewClient()
                loadUrl(article.url)
            }
        }
        catch (e:Exception){
            Log.d("TAG","Article cannot be opened")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}