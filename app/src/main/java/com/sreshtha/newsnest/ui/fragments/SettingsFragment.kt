package com.sreshtha.newsnest.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.sreshtha.newsnest.databinding.FragmentSettingsBinding
import com.sreshtha.newsnest.model.UserSettings
import com.sreshtha.newsnest.ui.MainActivity
import com.sreshtha.newsnest.viewmodel.NewsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsFragment: Fragment() {
    private var binding:FragmentSettingsBinding?=null
    private lateinit var viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        when(viewModel.currTheme){
            "dark" -> {
                binding?.switchDarkTheme?.isChecked = true
            }
            "light" -> {
                binding?.switchDarkTheme?.isChecked=false
            }
        }


        binding?.switchDarkTheme?.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true ->{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    viewModel.insert_settings(UserSettings(1,"dark"))
                    lifecycleScope.launch(Dispatchers.IO){
                        val theme = viewModel.get_user_settings().theme
                        Log.d("Settings",theme)
                    }
                }
                false -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    viewModel.insert_settings(UserSettings(1,"light"))
                    lifecycleScope.launch(Dispatchers.IO){
                        val theme = viewModel.get_user_settings().theme
                        Log.d("Settings",theme)
                    }
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null

    }


}