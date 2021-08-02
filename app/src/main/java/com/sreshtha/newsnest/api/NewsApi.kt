package com.sreshtha.newsnest.api

import com.sreshtha.newsnest.model.NewsModel
import com.sreshtha.newsnest.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getWorldWideHeadlines(
        @Query("language") lang:String="en",
        @Query("page") page:Int,
        @Query("apiKey") apiKey: String = Constants.API_KEY,
        @Query("category") category: String,
    ):Response<NewsModel>

    @GET("v2/top-headlines")
    suspend fun getIndianHeadlines(
        @Query("country") country:String = "in",
        @Query("page") page:Int,
        @Query("apiKey") apiKey:String = Constants.API_KEY,
        @Query("category") category: String,
    ):Response<NewsModel>


    @GET("v2/everything")
    suspend fun searchSortByPopularity(
        @Query("page") page:Int,
        @Query("q") query: String,
        @Query("sortBy") sortBy:String = "popularity",
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ):Response<NewsModel>

    @GET("v2/everything")
    suspend  fun searchSortByRelevancy(
        @Query("page") page:Int,
        @Query("q") query: String,
        @Query("sortBy") sortBy:String = "relevancy",
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ):Response<NewsModel>

    @GET("v2/everything")
    suspend fun searchSortByNewest(
        @Query("page") page:Int,
        @Query("q") query: String,
        @Query("sortBy") sortBy:String = "publishedAt",
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ):Response<NewsModel>

}