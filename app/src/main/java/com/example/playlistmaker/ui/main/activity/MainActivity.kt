package com.example.playlistmaker.ui.main.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.ui.library.activity.LibraryActivity
import com.example.playlistmaker.ui.main.view_model.MainViewModel
import com.example.playlistmaker.ui.search.activity.SearchActivity
import com.example.playlistmaker.ui.settings.activity.SettingsActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this, MainViewModel.getViewModelFactory())[MainViewModel::class.java]

        binding.searchButton.setOnClickListener {
            viewModel.toSearchScreen()
        }
        binding.libraryButton.setOnClickListener {
            viewModel.toLibraryScreen()
        }
        binding.settingsButton.setOnClickListener {

            viewModel.toSettingsScreen()
        }
    }

}