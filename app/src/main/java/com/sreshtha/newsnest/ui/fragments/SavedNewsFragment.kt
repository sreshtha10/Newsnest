package com.sreshtha.newsnest.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sreshtha.newsnest.databinding.FragmentSavedNewsBinding

class SavedNewsFragment : Fragment() {
    private var binding:FragmentSavedNewsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedNewsBinding.inflate(inflater,container,false)
        return binding?.root
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}