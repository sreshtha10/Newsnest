package com.sreshtha.newsnest.ui.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sreshtha.newsnest.adapter.NewsAdapter
import com.sreshtha.newsnest.databinding.FragmentBreakingNewsBinding
import com.sreshtha.newsnest.model.NewsModel
import com.sreshtha.newsnest.ui.MainActivity
import com.sreshtha.newsnest.ui.NewsViewModel
import com.sreshtha.newsnest.utils.Resource


class BreakingNewsFragment : Fragment() {

    private var binding:FragmentBreakingNewsBinding? = null
    private lateinit var viewModel :NewsViewModel
    private lateinit var adapter:NewsAdapter

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

        viewModel.breakingResponseData.observe(viewLifecycleOwner, { it ->
            when(it){
                is Resource.Success<NewsModel> ->{
                        hideProgressBar()
                        it.data?.let {
                            adapter.differ.submitList(it.articles)
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





        binding?.breakingNewsSpinner?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{ override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent != null) {
                    if(parent.getItemAtPosition(position) == "India"){
                            viewModel.getIndianHeadlines()
                    }
                    else if(parent.getItemAtPosition(position) == "Global"){
                        viewModel.getWorldWideNews()
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


        //initially displaying worldwide news
        viewModel.getWorldWideNews()

    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


    private fun setUpRecyclerView(){
        adapter = NewsAdapter()
        binding?.breakingNewsRv?.adapter = adapter
        binding?.breakingNewsRv?.layoutManager = LinearLayoutManager(activity)

    }


    private fun hideProgressBar(){
        binding?.breakingNewsPb?.visibility = View.INVISIBLE
    }

    private fun showProgressBar(){
        binding?.breakingNewsPb?.visibility = View.VISIBLE
    }
}