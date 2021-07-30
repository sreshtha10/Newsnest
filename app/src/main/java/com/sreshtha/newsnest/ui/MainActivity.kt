package com.sreshtha.newsnest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.sreshtha.newsnest.R
import com.sreshtha.newsnest.databinding.ActivityMainBinding
import com.sreshtha.newsnest.repository.NewsRepository
import com.sreshtha.newsnest.viewmodel.NewsViewModel
import com.sreshtha.newsnest.viewmodel.NewsViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val newsRepository = NewsRepository()
        val viewModelFactory = NewsViewModelFactory(newsRepository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(NewsViewModel::class.java)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHost.navController)


    }
}