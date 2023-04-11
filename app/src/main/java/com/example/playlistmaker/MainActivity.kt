package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button)
        val libraryButton = findViewById<Button>(R.id.library_button)
        val settingsButton = findViewById<Button>(R.id.settings_button)

        val searchButtonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val displayIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(displayIntent)
            }
        }
        searchButton.setOnClickListener(searchButtonClickListener)

        libraryButton.setOnClickListener {
            val displayIntent = Intent(this, LibraryActivity::class.java)
            startActivity(displayIntent)
        }
        settingsButton.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
    }
}