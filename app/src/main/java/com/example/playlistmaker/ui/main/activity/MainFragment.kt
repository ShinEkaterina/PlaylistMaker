package com.example.playlistmaker.ui.main.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentMainBinding
import com.example.playlistmaker.ui.main.view_model.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment:Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val viewModel by viewModel<MainViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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