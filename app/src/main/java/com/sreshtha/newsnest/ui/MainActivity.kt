package com.sreshtha.newsnest.ui

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.sreshtha.newsnest.R
import com.sreshtha.newsnest.database.ArticleDatabase
import com.sreshtha.newsnest.databinding.ActivityMainBinding
import com.sreshtha.newsnest.model.UserSettings
import com.sreshtha.newsnest.repository.NewsRepository
import com.sreshtha.newsnest.viewmodel.NewsViewModel
import com.sreshtha.newsnest.viewmodel.NewsViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: NewsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = ArticleDatabase.getDatabase(this)
        val newsRepository = NewsRepository(database)
        val viewModelFactory = NewsViewModelFactory(application,newsRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewsViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)


        setContentView(binding.root)

        try{
            var theme:String?=null
            lifecycleScope.launch(Dispatchers.IO){
                val userSettings = viewModel.get_user_settings()

                if(userSettings==null){
                    return@launch
                }

                theme = userSettings.theme
                viewModel.currTheme = theme as String
                viewModel.currLang = userSettings.lang
                Log.d("Settings","$theme n")

                withContext(Dispatchers.Main){
                    when(theme){
                        "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                }

            }

        }
        catch (e:Exception){
            Log.d("Settings",e.message.toString())
        }


        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHost.navController)

        when(this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)){
            Configuration.UI_MODE_NIGHT_YES->{
                viewModel.currTheme="dark"
            }
        }



    }





}

