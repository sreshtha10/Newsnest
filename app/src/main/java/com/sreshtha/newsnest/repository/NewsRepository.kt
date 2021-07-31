package com.sreshtha.newsnest.repository


import com.sreshtha.newsnest.api.RetrofitInstance
import com.sreshtha.newsnest.database.ArticleDatabase
import com.sreshtha.newsnest.model.Article
import com.sreshtha.newsnest.model.NewsModel
import retrofit2.Response


class NewsRepository(private val database:ArticleDatabase){

    suspend fun getWorldWideHeadlines(category: String):Response<NewsModel>{
        return RetrofitInstance.api.getWorldWideHeadlines(category = category)
    }

    suspend fun getIndianHeadlines(category: String):Response<NewsModel>{
        return RetrofitInstance.api.getIndianHeadlines(category=category)
    }

    suspend fun searchSortByPopularity(query:String):Response<NewsModel>{
        return RetrofitInstance.api.searchSortByNewest(query = query)
    }

    suspend fun searchSortByNewest(query: String):Response<NewsModel>{
        return RetrofitInstance.api.searchSortByNewest(query = query)
    }

    suspend fun searchSortByRelevancy(query: String):Response<NewsModel>{
        return RetrofitInstance.api.searchSortByRelevancy(query=query)
    }

    suspend fun insert(article: Article) = database.getArticleDao().insert(article)

    suspend fun delete(article: Article) = database.getArticleDao().delete(article)

    fun getAllSavedArticles() = database.getArticleDao().getAllSavedArticles()


}