package com.example.playlistmaker.ui.main.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.ui.library.activity.LibraryActivity
import com.example.playlistmaker.ui.search.activity.SearchActivity
import com.example.playlistmaker.ui.settings.activity.SettingsActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.searchButton.setOnClickListener {
            navigateTo(SearchActivity::class.java)
        }

        binding.libraryButton.setOnClickListener {
            navigateTo(LibraryActivity::class.java)
        }
        binding.settingsButton.setOnClickListener {
            navigateTo(SettingsActivity::class.java)
        }
    }

    private fun navigateTo(someClass: Class<out AppCompatActivity>) {
        val intent = Intent(this, someClass)
        startActivity(intent)
    }
}