package com.sreshtha.newsnest.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sreshtha.newsnest.databinding.FragmentArticleBinding

class ArticleFragment : Fragment() {

    private var binding:FragmentArticleBinding?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater,container,false)
        return binding?.root
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}