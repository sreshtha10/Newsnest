package com.sreshtha.newsnest.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sreshtha.newsnest.repository.NewsRepository

class NewsViewModelFactory (
    private val app:Application,
    private val newsRepository: NewsRepository
        ): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app,newsRepository) as T
    }
}