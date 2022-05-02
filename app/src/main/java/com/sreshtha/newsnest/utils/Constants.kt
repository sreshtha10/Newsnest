package com.sreshtha.newsnest.utils
import com.sreshtha.newsnest.BuildConfig


class Constants {
    companion object{
        const val BASE_URL = "https://newsapi.org/"
        const val API_KEY = BuildConfig.API_KEY
        const val PAGE_SIZE = 20
        const val ARTICLE_TAG="article"
        const val TYPE_TAG="type"
        const val BREAKING_FRAGMENT="breaking_fragment"
        const val SAVED_NEWS_FRAGMENT="saved_news_fragment"
        const val SEARCH_NEWS_FRAGMENT="search_news_fragment"
        const val NEWS_VIEW_MODEL = "news_view_model"
    }
}