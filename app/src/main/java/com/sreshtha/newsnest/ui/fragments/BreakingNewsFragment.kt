package com.sreshtha.newsnest.ui.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sreshtha.newsnest.R
import com.sreshtha.newsnest.adapter.NewsAdapter
import com.sreshtha.newsnest.databinding.FragmentBreakingNewsBinding
import com.sreshtha.newsnest.model.NewsModel
import com.sreshtha.newsnest.ui.MainActivity
import com.sreshtha.newsnest.viewmodel.NewsViewModel
import com.sreshtha.newsnest.utils.Resource



class BreakingNewsFragment : Fragment() {

    private var binding:FragmentBreakingNewsBinding? = null
    private lateinit var viewModel : NewsViewModel
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

        binding?.btnSearch?.setOnClickListener {
            val category = binding?.categorySpinner?.selectedItem.toString().lowercase()
            when(binding?.breakingNewsSpinner?.selectedItem.toString().lowercase()){
                "global" -> viewModel.getWorldWideNews(category=category)
                "india" -> viewModel.getIndianHeadlines(category=category)
            }
        }




        //initially displaying worldwide news (general)
        viewModel.getWorldWideNews("general")

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