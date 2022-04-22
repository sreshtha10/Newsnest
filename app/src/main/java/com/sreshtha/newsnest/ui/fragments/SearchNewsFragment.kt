package com.sreshtha.newsnest.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sreshtha.newsnest.R
import com.sreshtha.newsnest.adapter.NewsAdapter
import com.sreshtha.newsnest.databinding.FragmentSearchNewsBinding
import com.sreshtha.newsnest.model.Article
import com.sreshtha.newsnest.model.NewsModel
import com.sreshtha.newsnest.ui.MainActivity
import com.sreshtha.newsnest.utils.Constants
import com.sreshtha.newsnest.viewmodel.NewsViewModel
import com.sreshtha.newsnest.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment() {
    private var binding: FragmentSearchNewsBinding? = null
    private lateinit var viewModel: NewsViewModel
    private lateinit var adapter: NewsAdapter
    private var isLoading = false
    private var isScrolling = false
    private var isLastPage = false
    private var currSorting:String = "newest"
    private var currQuery:String=""

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

        setUpSpinner()

        adapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable(Constants.ARTICLE_TAG,it)
                putString(Constants.TYPE_TAG,Constants.SEARCH_NEWS_FRAGMENT)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel.searchResponseData.observe(viewLifecycleOwner) { it ->
            when (it) {
                is Resource.Success<NewsModel> -> {
                    hideProgressBar()
                    it.data?.let {
                        val newList: List<Article> = it.articles.toList()
                        adapter.differ.submitList(newList)
                        val totalPages = it.totalResults / Constants.PAGE_SIZE + 2
                        isLastPage = viewModel.searchNewsPage == totalPages

                    }
                    Log.d("TAG", it.data.toString())
                }
                is Resource.Error<NewsModel> -> {
                    hideProgressBar()
                    it.message?.let { it1 ->
                        Log.d("TAG", it1)
                        Snackbar.make(view, it1, Snackbar.LENGTH_SHORT).show()
                    }

                }
                is Resource.Loading<NewsModel> -> {
                    showProgressBar()
                }
            }

        }

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
                    currQuery = newText
                    viewModel.totalSearchNewsData = null
                    viewModel.searchNewsPage = 1
                    when (binding?.spinnerSort?.selectedItem.toString().lowercase()) {
                        "newest" -> {
                            viewModel.searchSortByNewest(newText)
                        }
                        "popularity" -> {
                            viewModel.searchSortByPopularity(newText)

                        }
                        "relevancy" ->{
                            viewModel.searchSortByRelevancy(newText)
                        }
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
                currQuery = query
                viewModel.totalSearchNewsData = null
                viewModel.searchNewsPage = 1
                when(parent?.getItemAtPosition(position).toString().lowercase()){
                    "newest" -> {
                        viewModel.searchSortByNewest(query)
                        currSorting = "newest"
                    }
                    "popularity" -> {
                        viewModel.searchSortByPopularity(query)
                        currSorting = "popularity"
                    }
                    "relevancy" -> {
                        viewModel.searchSortByRelevancy(query)
                        currSorting = "relevancy"
                    }
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
        binding?.searchNewsRv?.addOnScrollListener(customScrollListener)

    }


    private fun hideProgressBar() {
        binding?.searchPb?.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding?.searchPb?.visibility = View.VISIBLE
        isLoading = true
    }




    private val customScrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
                Log.d("TAG","Scrolling")
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager

            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemCount = layoutManager.findFirstVisibleItemPosition()

            if(!isLoading && !isLastPage){
                if(visibleItemCount+firstVisibleItemCount>=totalItemCount &&firstVisibleItemCount>=0 && totalItemCount>= Constants.PAGE_SIZE){
                    // load current function
                    when(currSorting){
                        "newest" ->{
                            viewModel.searchSortByNewest(query = currQuery)
                            Log.d("TAG","Last")
                        }
                        "popularity" ->{
                            viewModel.searchSortByPopularity(query = currQuery)
                            Log.d("TAG","Last")
                        }

                        "relevancy" ->{
                            viewModel.searchSortByRelevancy(query = currQuery)
                        }

                    }
                    isScrolling = false
                }
            }
        }
    }


    private fun setUpSpinner(){
        val arr = resources.getStringArray(R.array.sortBy)
        val sortAdapter = activity?.let { ArrayAdapter(it,R.layout.custom_spinner,arr) }
        binding?.spinnerSort?.adapter = sortAdapter
    }




}

