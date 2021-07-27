package com.sreshtha.newsnest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.sreshtha.newsnest.R
import com.sreshtha.newsnest.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHost.navController)


    }
}