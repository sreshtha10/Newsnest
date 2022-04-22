package com.sreshtha.newsnest.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.sreshtha.newsnest.R
import com.sreshtha.newsnest.databinding.FragmentArticleBinding
import com.sreshtha.newsnest.model.Article
import com.sreshtha.newsnest.ui.MainActivity
import com.sreshtha.newsnest.utils.Constants
import com.sreshtha.newsnest.viewmodel.NewsViewModel

class ArticleFragment : Fragment() {

    private lateinit var viewModel: NewsViewModel
    private var binding:FragmentArticleBinding?= null
    val args:ArticleFragmentArgs by navArgs()
    private lateinit var article:Article
    private lateinit var sourceType:String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater,container,false)
        viewModel = (activity as MainActivity).viewModel
        article = args.article
        sourceType = args.type
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val bottomNav = (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.visibility = View.GONE


        Log.d(Constants.ARTICLE_TAG,sourceType+" "+article.toString())



        val onBackPressedCallback  = object:OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                bottomNav.visibility = View.VISIBLE
                //todo navigate
                Constants.apply {
                    when(sourceType){
                        BREAKING_FRAGMENT -> findNavController().navigate(R.id.action_articleFragment_to_breakingNewsFragment)
                        SAVED_NEWS_FRAGMENT -> findNavController().navigate(R.id.action_articleFragment_to_savedNewsFragment)
                        SEARCH_NEWS_FRAGMENT -> findNavController().navigate(R.id.action_articleFragment_to_breakingNewsFragment)
                    }
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,onBackPressedCallback)




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