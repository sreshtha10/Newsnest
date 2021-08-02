package com.sreshtha.newsnest.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sreshtha.newsnest.model.Article
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
    var breakingNewsPage =1
    var searchNewsPage =1
    var totalBreakingNewsData:NewsModel? = null
    var totalSearchNewsData:NewsModel?=null


    fun getWorldWideNews(category:String) = viewModelScope.launch{
            breakingResponseData.value = Resource.Loading()
            val response= newsRepository.getWorldWideHeadlines(category=category,breakingNewsPage)
            breakingResponseData.value = handleBreakingNewsResponse(response)
    }

    fun getIndianHeadlines(category: String) = viewModelScope.launch {
        breakingResponseData.postValue(Resource.Loading())
        val response= newsRepository.getIndianHeadlines(category=category,breakingNewsPage)
        breakingResponseData.postValue(handleBreakingNewsResponse(response))
    }


    fun searchSortByPopularity(query:String) = viewModelScope.launch {
        searchResponseData.postValue(Resource.Loading())
        val response = newsRepository.searchSortByPopularity(query,page = searchNewsPage)
        searchResponseData.postValue(handleSearchNewsResponse(response))
    }


    fun searchSortByNewest(query:String) = viewModelScope.launch {
        searchResponseData.postValue(Resource.Loading())
        val response = newsRepository.searchSortByNewest(query,page = searchNewsPage)
        searchResponseData.postValue(handleSearchNewsResponse(response))
    }

    fun searchSortByRelevancy(query:String) = viewModelScope.launch {
        searchResponseData.postValue(Resource.Loading())
        val response = newsRepository.searchSortByRelevancy(query,page = searchNewsPage)
        searchResponseData.postValue(handleSearchNewsResponse(response))
    }


    private fun handleBreakingNewsResponse(response: Response<NewsModel>):Resource<NewsModel>{
        if(response.isSuccessful){
            response.body()?.let {
                breakingNewsPage++
                if(totalBreakingNewsData == null){
                    totalBreakingNewsData = it
                }
                else{
                    val prevResponse = totalBreakingNewsData?.articles
                    val newArticles = it.articles
                    prevResponse?.addAll(newArticles)
                }
                return Resource.Success(totalBreakingNewsData?:it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsModel>):Resource<NewsModel>{
        if(response.isSuccessful){
            searchNewsPage++
            response.body()?.let {
                if(totalSearchNewsData == null){
                        totalSearchNewsData= it
                }
                else{

                    val prevResponse = totalSearchNewsData?.articles
                    val newArticles = it.articles
                    prevResponse?.addAll(newArticles)
                }

                return Resource.Success(totalSearchNewsData?:it)
            }
        }
        return Resource.Error(response.message())
    }


    fun insert(article: Article) = viewModelScope.launch {
        newsRepository.insert(article)
    }

    fun delete(article: Article) = viewModelScope.launch {
        newsRepository.delete(article)
    }

    fun getAllSavedArticles() = newsRepository.getAllSavedArticles()


}