package com.sreshtha.newsnest.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sreshtha.newsnest.model.Article
import com.sreshtha.newsnest.model.UserSettings

@Dao
interface ArticleDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article)

    @Query("SELECT * FROM articles")
    fun getAllSavedArticles(): LiveData<List<Article>>

    @Delete
    suspend fun delete(article: Article)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert_settings(settings:UserSettings)

    @Query("SELECT * FROM user_settings")
    fun get_user_settings():UserSettings
}