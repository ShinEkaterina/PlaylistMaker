package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button)
        val libraryButton = findViewById<Button>(R.id.library_button)
        val settingsButton = findViewById<Button>(R.id.settings_button)


        searchButton.setOnClickListener {
            navigateTo(SearchActivity::class.java)
        }

        libraryButton.setOnClickListener {
            navigateTo(LibraryActivity::class.java)
        }
        settingsButton.setOnClickListener {
            navigateTo(SettingsActivity::class.java)
        }
    }

    private fun navigateTo(someClass: Class<out AppCompatActivity>) {
        val intent = Intent(this, someClass)
        startActivity(intent)
    }
}