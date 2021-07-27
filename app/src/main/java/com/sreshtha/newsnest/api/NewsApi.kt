package com.sreshtha.newsnest.api

import com.sreshtha.newsnest.model.NewsModel
import com.sreshtha.newsnest.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsApi {

    @GET("/top-headlines")
    suspend fun getWorldWideHeadlines(
        @Query("page") page:Int = 1,
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ):Response<NewsModel>

    @GET("/top-headlines")
    suspend fun getIndianHeadlines(
        @Query("page") page:Int = 1,
        @Query("country") country:String = "in",
        @Query("apiKey") apiKey:String = Constants.API_KEY
    ):Response<NewsModel>


    @GET("/everything")
    suspend fun searchSortByPopularity(
        @Query("page") page:Int = 1,
        @Query("q") query: String,
        @Query("sortBy") sortBy:String = "popularity",
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ):Response<NewsModel>

    @GET("/everything")
    suspend  fun searchSortByRelevancy(
        @Query("page") page:Int = 1,
        @Query("q") query: String,
        @Query("sortBy") sortBy:String = "relevancy",
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ):Response<NewsModel>

    @GET("/everything")
    suspend fun searchSortByNewest(
        @Query("page") page:Int = 1,
        @Query("q") query: String,
        @Query("sortBy") sortBy:String = "publishedAt",
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ):Response<NewsModel>

}