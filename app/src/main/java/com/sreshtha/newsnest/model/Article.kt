package com.sreshtha.newsnest.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "articles")
data class Article(
    var author: String?,
    var content: String?,
    var description: String,
    val publishedAt: String,
    var source: Source,
    var title: String,
    @PrimaryKey
    val url: String,
    var urlToImage: String
):Serializable