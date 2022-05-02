package com.sreshtha.newsnest.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.Data
import androidx.work.ExistingWorkPolicy.REPLACE
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.sreshtha.newsnest.R
import com.sreshtha.newsnest.database.ArticleDatabase
import com.sreshtha.newsnest.databinding.ActivityMainBinding
import com.sreshtha.newsnest.notification.NotifyWork
import com.sreshtha.newsnest.repository.NewsRepository
import com.sreshtha.newsnest.utils.Constants
import com.sreshtha.newsnest.utils.Constants.CHANNEL_ID
import com.sreshtha.newsnest.utils.Constants.CHANNEL_NAME
import com.sreshtha.newsnest.utils.Constants.NOTIFICATION_ID
import com.sreshtha.newsnest.viewmodel.NewsViewModel
import com.sreshtha.newsnest.viewmodel.NewsViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

//todo notifications via work manager
// fix theme and lang change bugs

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: NewsViewModel
    var alertDialog:AlertDialog?=null


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
        setLoadingDialog()
        createNotificationChannel()
        scheduleNotifications()

        val sharedPreferences = this.getSharedPreferences(STRING_PREF_NAME,Context.MODE_PRIVATE)
        if(sharedPreferences.getBoolean(STRING_IS_DARK_MODE,false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHost.navController)

        setUpTranslator()

    }


    fun restartActivity(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    private fun setUpTranslator(){
        // Create an English-German translator:
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.HINDI)
            .build()
        val englishHindiTranslator = Translation.getClient(options)
        val conditions = DownloadConditions.Builder()
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            englishHindiTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    Log.d("ALERT","download completed")
                    //todo toast
                }
                .addOnFailureListener { exception ->
                    Log.d("ALERT","download cancelled")
                }
        }

    }


    private fun setLoadingDialog(){
        val loadingView = layoutInflater.inflate(R.layout.custom_loading_alert_diaglog,null)
        alertDialog = AlertDialog.Builder(this).create()
        alertDialog?.setView(loadingView)
        alertDialog?.setCancelable(false)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =NotificationChannel(CHANNEL_ID, CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT).apply {
                lightColor= Color.BLUE
                enableLights(true)
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun scheduleNotifications(){
        val notificationWork = OneTimeWorkRequest.Builder(NotifyWork::class.java)
            .setInitialDelay(10000,TimeUnit.MILLISECONDS).setInputData(Data.Builder().putInt(Constants.NOTIFICATION_ID_TAG,
                NOTIFICATION_ID).build()).build()

        val instanceManager = WorkManager.getInstance(this)
        instanceManager.beginUniqueWork(Constants.NOTIFICATION_WORK,REPLACE,notificationWork).enqueue()
    }


}

