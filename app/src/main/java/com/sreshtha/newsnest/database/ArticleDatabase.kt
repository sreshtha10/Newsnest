package com.sreshtha.newsnest.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sreshtha.newsnest.model.Article

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase(){
    abstract fun getArticleDao():ArticleDAO

    companion object{
        @Volatile
        private var INSTANCE:ArticleDatabase?=null

        fun getDatabase(context: Context):ArticleDatabase{
            val tmpInstance = INSTANCE
            if(tmpInstance!=null){
                return tmpInstance
            }
            else{
                synchronized(this){
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        ArticleDatabase::class.java,
                        "article_db.db"
                    ).build()

                    INSTANCE= instance
                    return instance
                }
            }
        }
    }
}