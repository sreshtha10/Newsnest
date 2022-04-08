package com.sreshtha.newsnest.ui

import android.content.Context
import android.content.Intent
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
import com.sreshtha.newsnest.repository.NewsRepository
import com.sreshtha.newsnest.viewmodel.NewsViewModel
import com.sreshtha.newsnest.viewmodel.NewsViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: NewsViewModel

    companion object{
        const val STRING_PREF_NAME="USER_SETTINGS"
        const val STRING_IS_DARK_MODE = "IS_DARK_MODE"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = ArticleDatabase.getDatabase(this)
        val newsRepository = NewsRepository(database)
        val viewModelFactory = NewsViewModelFactory(application,newsRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[NewsViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)


        setContentView(binding.root)

        val sharedPreferences = this.getSharedPreferences(STRING_PREF_NAME,Context.MODE_PRIVATE)
        if(sharedPreferences.getBoolean(STRING_IS_DARK_MODE,false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        /*
        try{
            var theme:String?=null
            lifecycleScope.launch(Dispatchers.IO){

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
        }*/


        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHost.navController)

        /*when(this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)){
            Configuration.UI_MODE_NIGHT_YES->{
                //viewModel.currTheme="dark"
            }
        }*/



    }


    fun restartActivity(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }




}

