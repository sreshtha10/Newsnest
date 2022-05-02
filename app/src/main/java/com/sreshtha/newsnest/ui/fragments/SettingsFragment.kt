package com.sreshtha.newsnest.ui.fragments


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.mlkit.nl.translate.Translator
import com.sreshtha.newsnest.databinding.FragmentSettingsBinding
import com.sreshtha.newsnest.ui.MainActivity
import com.sreshtha.newsnest.utils.LocaleHelper
import com.sreshtha.newsnest.viewmodel.NewsViewModel

class SettingsFragment : Fragment() {
    private var binding: FragmentSettingsBinding? = null
    private lateinit var viewModel: NewsViewModel
    private var builder: AlertDialog? = null
    private var englishHindiTranslator: Translator? = null

    companion object {
        const val STRING_PREF_NAME = "USER_SETTINGS"
        const val STRING_IS_DARK_MODE = "IS_DARK_MODE"
        const val STRING_IS_LANG_ENG = "IS_LANG_ENG"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        val sharedPrefs = context?.getSharedPreferences(STRING_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPrefs?.edit()
        binding?.switchDarkTheme?.isChecked =
            sharedPrefs?.getBoolean(STRING_IS_DARK_MODE, false) == true

        binding?.switchDarkTheme?.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                    editor?.putBoolean(STRING_IS_DARK_MODE, true)
                    editor?.apply()

                }
                false -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    editor?.putBoolean(STRING_IS_DARK_MODE, false)
                    editor?.apply()
                }

            }
        }

        if (sharedPrefs?.getBoolean(STRING_IS_LANG_ENG, true) == true) {
            binding?.spLang?.setSelection(0)
        } else {
            binding?.spLang?.setSelection(1)
        }


        var isInitialCall = true
        binding?.spLang?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d("DEBUG", "sp selection called")
                if (isInitialCall) {
                    isInitialCall = false
                    return
                }
                if (parent?.getItemAtPosition(position).toString() == "English") {
                    LocaleHelper.setLocale(activity, "en")
                    editor?.putBoolean(STRING_IS_LANG_ENG, true)?.apply()
                    (activity as MainActivity).restartActivity()
                } else {
                    LocaleHelper.setLocale(activity, "hi")
                    editor?.putBoolean(STRING_IS_LANG_ENG, false)?.apply()
                    (activity as MainActivity).restartActivity()
                    //setUpTranslator()
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