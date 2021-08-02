package com.sreshtha.newsnest.model

data class NewsModel(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)


