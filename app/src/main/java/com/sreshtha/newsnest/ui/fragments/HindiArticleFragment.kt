package com.sreshtha.newsnest.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sreshtha.newsnest.R
import com.sreshtha.newsnest.databinding.FragmentHindiArticleBinding
import com.sreshtha.newsnest.model.Article
import com.sreshtha.newsnest.ui.MainActivity
import com.sreshtha.newsnest.utils.Constants

class HindiArticleFragment:Fragment() {

    private var hindiArticleBinding : FragmentHindiArticleBinding?=null
    val args:HindiArticleFragmentArgs by navArgs()
    private lateinit var article: Article
    private lateinit var sourceType:String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hindiArticleBinding = FragmentHindiArticleBinding.inflate(inflater,container,false)
        article = args.article
        sourceType = args.type
        return  hindiArticleBinding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav = (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.visibility = View.GONE

        val onBackPressedCallback  = object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                bottomNav.visibility = View.VISIBLE
                Constants.apply {
                    when(sourceType){
                        BREAKING_FRAGMENT -> findNavController().navigate(R.id.action_hindiArticleFragment_to_breakingNewsFragment)
                        SAVED_NEWS_FRAGMENT -> findNavController().navigate(R.id.action_hindiArticleFragment_to_savedNewsFragment)
                        SEARCH_NEWS_FRAGMENT -> findNavController().navigate(R.id.action_hindiArticleFragment_to_breakingNewsFragment)
                    }
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,onBackPressedCallback)

        hindiArticleBinding?.apply {
            tvNewsText.text = article.description
            tvTitle.text= article.title
            Glide.with(view).load(article.urlToImage).into(ivImage)
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        hindiArticleBinding = null
    }
}