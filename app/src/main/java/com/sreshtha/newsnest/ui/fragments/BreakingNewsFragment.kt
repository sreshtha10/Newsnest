package com.sreshtha.newsnest.ui.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sreshtha.newsnest.R
import com.sreshtha.newsnest.adapter.NewsAdapter
import com.sreshtha.newsnest.databinding.FragmentBreakingNewsBinding
import com.sreshtha.newsnest.model.Article
import com.sreshtha.newsnest.model.NewsModel
import com.sreshtha.newsnest.ui.MainActivity
import com.sreshtha.newsnest.utils.Constants
import com.sreshtha.newsnest.viewmodel.NewsViewModel
import com.sreshtha.newsnest.utils.Resource



class BreakingNewsFragment : Fragment() {

    private var binding:FragmentBreakingNewsBinding? = null
    private lateinit var viewModel : NewsViewModel
    private lateinit var adapter:NewsAdapter
    private var isLoading = false
    private var isScrolling = false
    private var isLastPage = false
    private var currCategory:String = "general"
    private var currRegion:String="global"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBreakingNewsBinding.inflate(inflater,container,false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        setUpRecyclerView()

        adapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel.breakingResponseData.observe(viewLifecycleOwner, { it ->
            when(it){
                is Resource.Success<NewsModel> ->{
                        hideProgressBar()
                        it.data?.let {
                            val newList : List<Article> = it.articles.toList()
                            adapter.differ.submitList(newList)
                            val totalPages = it.totalResults/Constants.PAGE_SIZE +2
                            isLastPage = viewModel.breakingNewsPage == totalPages
                        }
                    Log.d("TAG",it.data.toString())
                }
                is Resource.Error<NewsModel> ->{
                    hideProgressBar()
                    it.message?.let { it1 -> Log.d("TAG", it1) }
                        // display snackbar showing error message
                }
                is Resource.Loading<NewsModel> ->{
                        showProgressBar()
                }
            }

        })


        //initially displaying worldwide news (general)
        viewModel.getWorldWideNews("general")




        binding?.categorySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val category = parent?.getItemAtPosition(position).toString().lowercase()
                currCategory = category
                val region = binding?.breakingNewsSpinner?.selectedItem.toString().lowercase()
                currRegion = region
                viewModel.breakingNewsPage = 1
                viewModel.totalBreakingNewsData = null
                when(region){
                    "global" -> viewModel.getWorldWideNews(category=category)
                    "india" -> viewModel.getIndianHeadlines(category=category)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


        binding?.breakingNewsSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val region = parent?.getItemAtPosition(position).toString().lowercase()
                currRegion = region
                val category = binding?.categorySpinner?.selectedItem.toString().lowercase()
                currCategory = category
                viewModel.breakingNewsPage = 1
                viewModel.totalBreakingNewsData = null
                when(region){
                    "global" -> viewModel.getWorldWideNews(category=category)
                    "india" -> viewModel.getIndianHeadlines(category=category)
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


    private fun setUpRecyclerView(){
        adapter = NewsAdapter()
        binding?.breakingNewsRv?.adapter = adapter
        binding?.breakingNewsRv?.layoutManager = LinearLayoutManager(activity)
        binding?.breakingNewsRv?.addOnScrollListener(customScrollListener)

    }


    private fun hideProgressBar(){
        binding?.breakingNewsPb?.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar(){
        binding?.breakingNewsPb?.visibility = View.VISIBLE
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
                    when(currRegion){
                        "india" ->{
                            viewModel.getIndianHeadlines(category = currCategory)
                            Log.d("TAG","Last")
                        }
                        "global" ->{
                            viewModel.getWorldWideNews(category = currCategory)
                            Log.d("TAG","Last")
                        }
                    }
                    isScrolling = false
                }
            }
        }
    }

}