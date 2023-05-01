package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar

class SearchActivity : AppCompatActivity() {
    private lateinit var searchEditText: EditText
    private lateinit var clearButton: ImageView
    private var searchText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val toolbar = findViewById<Toolbar>(R.id.search_toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        searchEditText = findViewById<EditText>(R.id.input_edit_text)
        clearButton = findViewById<ImageView>(R.id.clear_icon)

        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString(SEARCH_TEXT).toString()
        }
        searchEditText.setText(searchText)

        clearButton.setOnClickListener {
            //Clear text
            searchEditText.setText("")

            //Hide the keyboard
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(searchEditText.windowToken, 0)
            searchEditText.clearFocus()
        }

        searchEditText.addTextChangedListener(searchTextWatcher)
    }

    private val searchTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // empty
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s != null) {
                searchText = s.toString()
            }
        }

        override fun afterTextChanged(s: Editable?) {
            // empty
        }
    }


    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }
}