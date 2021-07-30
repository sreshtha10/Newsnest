package com.sreshtha.newsnest.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sreshtha.newsnest.model.NewsModel
import com.sreshtha.newsnest.repository.NewsRepository
import com.sreshtha.newsnest.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    private  val newsRepository: NewsRepository
) : ViewModel() {

    val breakingResponseData:MutableLiveData<Resource<NewsModel>> = MutableLiveData()
    val searchResponseData :MutableLiveData<Resource<NewsModel>> = MutableLiveData()
    var breakingNewsPage = 1
    var searchNewsPage =1


    fun getWorldWideNews(category:String) = viewModelScope.launch{
            breakingResponseData.value = Resource.Loading()
            val response= newsRepository.getWorldWideHeadlines(category=category)
            breakingResponseData.value = handleBreakingNewsResponse(response)
    }

    fun getIndianHeadlines(category: String) = viewModelScope.launch {
        breakingResponseData.postValue(Resource.Loading())
        val response= newsRepository.getIndianHeadlines(category=category)
        breakingResponseData.postValue(handleBreakingNewsResponse(response))
    }


    fun searchSortByPopularity(query:String) = viewModelScope.launch {
        searchResponseData.postValue(Resource.Loading())
        val response = newsRepository.searchSortByPopularity(query)
        searchResponseData.postValue(handleBreakingNewsResponse(response))
    }


    fun searchSortByNewest(query:String) = viewModelScope.launch {
        searchResponseData.postValue(Resource.Loading())
        val response = newsRepository.searchSortByNewest(query)
        searchResponseData.postValue(handleBreakingNewsResponse(response))
    }

    fun searchSortByRelevancy(query:String) = viewModelScope.launch {
        searchResponseData.postValue(Resource.Loading())
        val response = newsRepository.searchSortByRelevancy(query)
        searchResponseData.postValue(handleBreakingNewsResponse(response))
    }


    private fun handleBreakingNewsResponse(response: Response<NewsModel>):Resource<NewsModel>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }



}