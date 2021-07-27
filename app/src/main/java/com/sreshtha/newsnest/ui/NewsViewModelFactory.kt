package com.sreshtha.newsnest.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sreshtha.newsnest.repository.NewsRepository

class NewsViewModelFactory (
    private val newsRepository: NewsRepository
        ): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository) as T
    }
}