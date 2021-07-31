package com.sreshtha.newsnest.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sreshtha.newsnest.adapter.NewsAdapter
import com.sreshtha.newsnest.databinding.FragmentSavedNewsBinding
import com.sreshtha.newsnest.ui.MainActivity
import com.sreshtha.newsnest.viewmodel.NewsViewModel

class SavedNewsFragment : Fragment() {
    private var binding:FragmentSavedNewsBinding? = null
    private lateinit var viewModel: NewsViewModel
    private lateinit var adapter:NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedNewsBinding.inflate(inflater,container,false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        setUpRecyclerView()

        viewModel.getAllSavedArticles().observe(viewLifecycleOwner,{
            if(it != null){
                adapter.differ.submitList(it)
            }

        })

        viewModel.getAllSavedArticles()


    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


    private fun setUpRecyclerView(){
        adapter = NewsAdapter()
        binding?.savedNewsRv?.adapter = adapter
        binding?.savedNewsRv?.layoutManager = LinearLayoutManager(activity)

    }
}