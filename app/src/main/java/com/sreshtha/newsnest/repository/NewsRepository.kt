package com.sreshtha.newsnest.repository


import com.sreshtha.newsnest.api.RetrofitInstance
import com.sreshtha.newsnest.database.ArticleDatabase
import com.sreshtha.newsnest.model.Article
import com.sreshtha.newsnest.model.NewsModel
import retrofit2.Response


class NewsRepository(private val database:ArticleDatabase){

    suspend fun getWorldWideHeadlines(category: String,page:Int):Response<NewsModel>{
        return RetrofitInstance.api.getWorldWideHeadlines(category = category,page = page)
    }

    suspend fun getIndianHeadlines(category: String,page:Int):Response<NewsModel>{
        return RetrofitInstance.api.getIndianHeadlines(category=category,page = page)
    }

    suspend fun searchSortByPopularity(query:String,page:Int):Response<NewsModel>{
        return RetrofitInstance.api.searchSortByNewest(query = query,page = page)
    }

    suspend fun searchSortByNewest(query: String,page:Int):Response<NewsModel>{
        return RetrofitInstance.api.searchSortByNewest(query = query,page = page)
    }

    suspend fun searchSortByRelevancy(query: String,page:Int):Response<NewsModel>{
        return RetrofitInstance.api.searchSortByRelevancy(query=query,page = page)
    }

    suspend fun insert(article: Article) = database.getArticleDao().insert(article)

    suspend fun delete(article: Article) = database.getArticleDao().delete(article)

    fun getAllSavedArticles() = database.getArticleDao().getAllSavedArticles()


}