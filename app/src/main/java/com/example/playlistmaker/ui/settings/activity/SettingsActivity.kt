package com.example.playlistmaker.ui.settings.activity


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.DARK_THEME_KEY
import com.example.playlistmaker.R
import com.example.playlistmaker.SHARED_PREFERENCES
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }


        binding.shareAppIcon.setOnClickListener {
            var intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/android-developer/")
            startActivity(intent)
        }

        binding.supportIcon.setOnClickListener {
            val subject = this.getString(R.string.write_support_subject)
            val message = this.getString(R.string.write_support_message)
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("Shinus90@yandex.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(intent)
        }
        binding.agreementIcon.setOnClickListener {
            val url = Uri.parse("https://yandex.ru/legal/practicum_offer/")
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }


        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)

        binding.themeSwitcher.isChecked = sharedPreferences.getBoolean(DARK_THEME_KEY, false)

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            sharedPreferences.edit().putBoolean(DARK_THEME_KEY, checked).apply()
            binding.themeSwitcher.setChecked(checked)
        }

    }

}