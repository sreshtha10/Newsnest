package com.sreshtha.newsnest.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sreshtha.newsnest.model.Article

@Dao
interface ArticleDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article)

    @Query("SELECT * FROM articles")
    suspend fun getAllSavedArticles(): LiveData<List<Article>>

    @Delete
    suspend fun delete(article: Article)
}