package com.example.playlistmaker.ui.settings.activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getModeLiveData().observe(this) { isDarkMode ->
            changeThemeMode(isDarkMode)
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.changeMode(isChecked)
        }

        binding.supportIcon.setOnClickListener {
            viewModel.openSupport(
                getString(R.string.write_support_subject),
                getString(R.string.write_support_message)
            )
        }

        binding.shareAppIcon.setOnClickListener {
            viewModel.shareApp()
        }

        binding.agreementIcon.setOnClickListener {
            viewModel.legalAgreement()
        }

        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun changeThemeMode(isDarkMode: Boolean) {
        if (isDarkMode == true) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.themeSwitcher.isChecked = true
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.themeSwitcher.isChecked = false
        }
    }

}