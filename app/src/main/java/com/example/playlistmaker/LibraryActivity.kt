package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class LibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)
        val backButton = findViewById<ImageView>(R.id.back_arrow_image)

        backButton.setOnClickListener {
            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
        }
    }
}