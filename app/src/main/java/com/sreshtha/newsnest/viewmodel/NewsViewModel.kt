package com.sreshtha.newsnest.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sreshtha.newsnest.MyApplication
import com.sreshtha.newsnest.model.Article
import com.sreshtha.newsnest.model.NewsModel
import com.sreshtha.newsnest.repository.NewsRepository
import com.sreshtha.newsnest.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    app:Application,
    private  val newsRepository: NewsRepository
) : AndroidViewModel(app) {

    val breakingResponseData:MutableLiveData<Resource<NewsModel>> = MutableLiveData()
    val searchResponseData :MutableLiveData<Resource<NewsModel>> = MutableLiveData()
    var breakingNewsPage =1
    var searchNewsPage =1
    var totalBreakingNewsData:NewsModel? = null
    var totalSearchNewsData:NewsModel?=null


    fun getWorldWideNews(category:String) = viewModelScope.launch{
            breakingResponseData.value = Resource.Loading()
            try {
                if(hasInternetConnection()){
                    val response= newsRepository.getWorldWideHeadlines(category=category,breakingNewsPage)
                    breakingResponseData.value = handleBreakingNewsResponse(response)
                }
                else{
                    breakingResponseData.value = Resource.Error("No Internet Connection")
                }
            }
            catch (t:Throwable){
                when(t){
                    is IOException -> breakingResponseData.value = Resource.Error("Network Failure")
                    else -> breakingResponseData.value = Resource.Error("Conversion Error")
                }
            }

    }

    fun getIndianHeadlines(category: String) = viewModelScope.launch {
        breakingResponseData.value = Resource.Loading()
        try {
            if(hasInternetConnection()){
                val response= newsRepository.getIndianHeadlines(category=category,breakingNewsPage)
                breakingResponseData.value = handleBreakingNewsResponse(response)
            }
            else{
                breakingResponseData.value = Resource.Error("No Internet Connection")
            }
        }
        catch (t:Throwable){
            when(t){
                is IOException -> breakingResponseData.value = Resource.Error("Network Failure")
                else -> breakingResponseData.value = Resource.Error("Conversion Error")
            }
        }
    }


    fun searchSortByPopularity(query:String) = viewModelScope.launch {
        searchResponseData.value = Resource.Loading()
        try {
            if(hasInternetConnection()){
                val response= newsRepository.searchSortByPopularity(query = query,page =searchNewsPage)
                searchResponseData.value = handleSearchNewsResponse(response)
            }
            else{
                searchResponseData.value = Resource.Error("No Internet Connection")
            }
        }
        catch (t:Throwable){
            when(t){
                is IOException -> searchResponseData.value = Resource.Error("Network Failure")
                else -> searchResponseData.value = Resource.Error("Conversion Error")
            }
        }

    }


    fun searchSortByNewest(query:String) = viewModelScope.launch {
        searchResponseData.value = Resource.Loading()
        try {
            if(hasInternetConnection()){
                val response= newsRepository.searchSortByNewest(query = query,page =searchNewsPage)
                searchResponseData.value = handleSearchNewsResponse(response)
            }
            else{
                searchResponseData.value = Resource.Error("No Internet Connection")
            }
        }
        catch (t:Throwable){
            when(t){
                is IOException -> searchResponseData.value = Resource.Error("Network Failure")
                else -> searchResponseData.value = Resource.Error("Conversion Error")
            }
        }
    }

    fun searchSortByRelevancy(query:String) = viewModelScope.launch {
        searchResponseData.value = Resource.Loading()
        try {
            if(hasInternetConnection()){
                val response= newsRepository.searchSortByRelevancy(query = query,page =searchNewsPage)
                searchResponseData.value = handleSearchNewsResponse(response)
            }
            else{
                searchResponseData.value = Resource.Error("No Internet Connection")
            }
        }
        catch (t:Throwable){
            when(t){
                is IOException -> searchResponseData.value = Resource.Error("Network Failure")
                else -> searchResponseData.value = Resource.Error("Conversion Error")
            }
        }
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



    private fun hasInternetConnection():Boolean{
        val connectivityManager = getApplication<MyApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork?:return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)?:return false
            return when{
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> return false
            }

        }
        else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}