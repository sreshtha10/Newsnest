package com.sreshtha.newsnest.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sreshtha.newsnest.adapter.NewsAdapter
import com.sreshtha.newsnest.databinding.FragmentSearchNewsBinding
import com.sreshtha.newsnest.model.NewsModel
import com.sreshtha.newsnest.ui.MainActivity
import com.sreshtha.newsnest.ui.NewsViewModel
import com.sreshtha.newsnest.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment() {
    private var binding: FragmentSearchNewsBinding? = null
    private lateinit var viewModel: NewsViewModel
    private lateinit var adapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        setUpRecyclerView()

        viewModel.searchResponseData.observe(viewLifecycleOwner, { it ->
            when (it) {
                is Resource.Success<NewsModel> -> {
                    hideProgressBar()
                    it.data?.let {
                        adapter.differ.submitList(it.articles)
                    }
                    Log.d("TAG", it.data.toString())
                }
                is Resource.Error<NewsModel> -> {
                    hideProgressBar()
                    it.message?.let { it1 -> Log.d("TAG", it1) }
                    // display snackbar showing error message
                }
                is Resource.Loading<NewsModel> -> {
                    showProgressBar()
                }
            }

        })


        var job:Job?=null

        binding?.searchViewNews?.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
               return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.isNullOrEmpty()){
                    return false
                }

                job?.cancel()
                job = MainScope().launch {
                    delay(1000L)
                    when (binding?.spinnerSort?.selectedItem.toString().lowercase()) {
                        "newest" -> viewModel.searchSortByNewest(newText)
                        "popularity" -> viewModel.searchSortByPopularity(newText)
                        "relevancy" -> viewModel.searchSortByRelevancy(newText)
                    }

                }

                return true

            }
        })

        binding?.searchViewNews?.isSubmitButtonEnabled=false


        binding?.spinnerSort?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val query = binding?.searchViewNews?.query.toString()
                if(query.isEmpty()){
                    return
                }
                when(parent?.getItemAtPosition(position).toString().lowercase()){
                    "newest" -> viewModel.searchSortByNewest(query)
                    "popularity" -> viewModel.searchSortByPopularity(query)
                    "relevancy" -> viewModel.searchSortByRelevancy(query)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }




    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun setUpRecyclerView() {
        adapter = NewsAdapter()
        binding?.searchNewsRv?.adapter = adapter
        binding?.searchNewsRv?.layoutManager = LinearLayoutManager(activity)

    }


    private fun hideProgressBar() {
        binding?.searchPb?.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding?.searchPb?.visibility = View.VISIBLE
    }

}

