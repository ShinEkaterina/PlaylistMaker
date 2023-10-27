package com.example.playlistmaker.ui.main.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMainBinding
import com.example.playlistmaker.ui.library.activity.LibraryFragment
import com.example.playlistmaker.ui.main.view_model.MainViewModel
import com.example.playlistmaker.ui.search.activity.SearchFragment
import com.example.playlistmaker.ui.settings.activity.SettingsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment:Fragment() {

    companion object{
        const val TAG = "MainFragment"

        fun newInstance(): Fragment {
            return MainFragment()
        }
    }
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
           // viewModel.goToSearchScreen()
            parentFragmentManager.commit {
                replace(
                    // Указали, в каком контейнере работаем
                    R.id.rootFragmentContainerView,
                    // Создали фрагмент
                    SearchFragment.newInstance(),
                    // Указали тег фрагмента
                    SearchFragment.TAG
                )

                // Добавляем фрагмент в Back Stack
                addToBackStack(SearchFragment.TAG)
            }
        }
        binding.libraryButton.setOnClickListener {
           // viewModel.goToLibraryScreen()
            parentFragmentManager.commit {
                replace(
                    // Указали, в каком контейнере работаем
                    R.id.rootFragmentContainerView,
                    // Создали фрагмент
                    LibraryFragment.newInstance(),
                    // Указали тег фрагмента
                    LibraryFragment.TAG
                )

                // Добавляем фрагмент в Back Stack
                addToBackStack(LibraryFragment.TAG)
            }
        }
        binding.settingsButton.setOnClickListener {

          //  viewModel.goToSettingsScreen()

            parentFragmentManager.commit {
                replace(
                    // Указали, в каком контейнере работаем
                    R.id.rootFragmentContainerView,
                    // Создали фрагмент
                    SettingsFragment.newInstance(),
                    // Указали тег фрагмента
                    SettingsFragment.TAG
                )

                // Добавляем фрагмент в Back Stack
                addToBackStack(SettingsFragment.TAG)
            }
        }
    }
}