package com.example.playlistmaker.ui.main.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.ui.main.view_model.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchButton.setOnClickListener {
            viewModel.goToSearchScreen()
        }
        binding.libraryButton.setOnClickListener {
            viewModel.goToLibraryScreen()
        }
        binding.settingsButton.setOnClickListener {

            viewModel.goToSettingsScreen()
        }
    }

}