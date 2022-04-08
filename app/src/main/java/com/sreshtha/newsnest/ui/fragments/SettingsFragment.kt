package com.sreshtha.newsnest.ui.fragments


import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.sreshtha.newsnest.databinding.FragmentSettingsBinding
import com.sreshtha.newsnest.ui.MainActivity
import com.sreshtha.newsnest.utils.LocaleHelper
import com.sreshtha.newsnest.viewmodel.NewsViewModel

class SettingsFragment: Fragment() {
    private var binding:FragmentSettingsBinding?=null
    private lateinit var viewModel: NewsViewModel
    companion object{
        const val STRING_PREF_NAME="USER_SETTINGS"
        const val STRING_IS_DARK_MODE = "IS_DARK_MODE"
    }

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

        /*
        when(viewModel.currTheme){
            "dark" -> {
                binding?.switchDarkTheme?.isChecked = true
            }
            "light" -> {
                binding?.switchDarkTheme?.isChecked=false
            }
        }*/

        val sharedPrefs = context?.getSharedPreferences(STRING_PREF_NAME,Context.MODE_PRIVATE)
        val editor = sharedPrefs?.edit()
        binding?.switchDarkTheme?.isChecked = sharedPrefs?.getBoolean(STRING_IS_DARK_MODE,false) == true

        binding?.switchDarkTheme?.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true ->{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                    editor?.putBoolean(STRING_IS_DARK_MODE,true)
                    editor?.apply()


                    //viewModel.currTheme = "dark"
                    //viewModel.insert_settings(UserSettings(1,"dark",viewModel.currLang!!))
                    /*lifecycleScope.launch(Dispatchers.IO){
                        val theme = viewModel.get_user_settings().theme
                        Log.d("Settings",theme)
                    }*/
                }
                false -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    editor?.putBoolean(STRING_IS_DARK_MODE,false)
                    editor?.apply()
                    //viewModel.currTheme = "light"
                    //viewModel.insert_settings(UserSettings(1,"light",viewModel.currLang!!))
                    /*lifecycleScope.launch(Dispatchers.IO){
                        val theme = viewModel.get_user_settings().theme
                        Log.d("Settings",theme)
                    }*/
                }

            }
        }




        var isInitialCall = true
        binding?.spLang?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d("DEBUG","sp selection called")
                if(isInitialCall){
                    isInitialCall=false
                    return
                }
                if(parent?.getItemAtPosition(position).toString() == "English"){
                    //viewModel.insert_settings(UserSettings(1,viewModel.currTheme,viewModel.currLang))
                    LocaleHelper.setLocale(activity,"en")
                    (activity as MainActivity).restartActivity()
                }
                else{
                    //viewModel.insert_settings(UserSettings(1,viewModel.currTheme,viewModel.currLang))
                    LocaleHelper.setLocale(activity,"hi")
                    (activity as MainActivity).restartActivity()
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