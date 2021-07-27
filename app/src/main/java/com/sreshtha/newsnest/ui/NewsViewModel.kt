package com.sreshtha.newsnest.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sreshtha.newsnest.model.NewsModel
import com.sreshtha.newsnest.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    private  val newsRepository: NewsRepository
) : ViewModel() {

    val responseData:MutableLiveData<NewsModel> = MutableLiveData()

    fun getWorldWideNews(){
        viewModelScope.launch(Dispatchers.IO) {
            val response :Response<NewsModel> = try{
                newsRepository.getWorldWideHeadlines()
            }
            catch (e:Exception){
                // error occured
                return@launch
            }
            if(response.isSuccessful && response.body()!=null){
                responseData.value= response.body()
            }


        }
    }

    fun getIndianHeadlines(){
        viewModelScope.launch(Dispatchers.IO){
            val response = try{
                newsRepository.getIndianHeadlines()
            }
            catch(e:Exception){
                //error occurred
                return@launch
            }

            if(response.isSuccessful&& response.body()!=null){
                responseData.value= response.body()
            }
        }
    }

    fun searchSortByPopularity(query:String){
        viewModelScope.launch(Dispatchers.IO){
            val response = try{
                newsRepository.searchSortByPopularity(query)
            }
            catch (e:Exception){
                //error occurred
                return@launch
            }

            if(response.isSuccessful&& response.body()!=null){
                responseData.value = response.body()
            }
        }
    }

    fun searchSortByRelevancy(query:String){
        viewModelScope.launch(Dispatchers.IO){
            val response = try{
                newsRepository.searchSortByRelevancy(query)
            }
            catch (e:Exception){
                //error occurred
                return@launch
            }

            if(response.isSuccessful&& response.body()!=null){
                responseData.value = response.body()
            }
        }
    }

    fun searchSortByNewest(query:String){
        viewModelScope.launch(Dispatchers.IO){
            val response = try{
                newsRepository.searchSortByNewest(query)
            }
            catch (e:Exception){
                //error occurred
                return@launch
            }

            if(response.isSuccessful&& response.body()!=null){
                responseData.value = response.body()
            }
        }
    }


}