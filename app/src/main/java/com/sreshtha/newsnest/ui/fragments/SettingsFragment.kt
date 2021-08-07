package com.sreshtha.newsnest.ui.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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


        when(viewModel.currLang){
            "hi" -> binding?.spLang?.setSelection(1)
            "en" -> binding?.spLang?.setSelection(0)
        }

        binding?.switchDarkTheme?.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true ->{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    viewModel.currTheme = "dark"
                    viewModel.insert_settings(UserSettings(1,"dark",viewModel.currLang!!))
                    lifecycleScope.launch(Dispatchers.IO){
                        val theme = viewModel.get_user_settings().theme
                        Log.d("Settings",theme)
                    }
                }
                false -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    viewModel.currTheme = "light"
                    viewModel.insert_settings(UserSettings(1,"light",viewModel.currLang!!))
                    lifecycleScope.launch(Dispatchers.IO){
                        val theme = viewModel.get_user_settings().theme
                        Log.d("Settings",theme)
                    }
                }

            }
        }

        binding?.spLang?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(parent?.getItemAtPosition(position) == "English"){
                    viewModel.currLang = "en"
                    viewModel.insert_settings(UserSettings(1,viewModel.currTheme!!,viewModel.currLang!!))
                }
                else{
                    viewModel.currLang = "hi"
                    viewModel.insert_settings(UserSettings(1,viewModel.currTheme!!,viewModel.currLang!!))
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null

    }




}