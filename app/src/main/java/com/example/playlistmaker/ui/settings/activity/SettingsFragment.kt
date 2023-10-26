package com.example.playlistmaker.ui.settings.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.ui.main.activity.MainFragment
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: Fragment() {
    companion object{
        const val TAG = "SettingsFragment"

        fun newInstance(): Fragment {
            return SettingsFragment()
        }
    }
    private lateinit var binding:FragmentSettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getModeLiveData().observe(viewLifecycleOwner) { isDarkMode ->
            changeThemeMode(isDarkMode)
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.changeMode(isChecked)
        }

        binding.supportIcon.setOnClickListener {
            viewModel.openSupport(
                getString(R.string.write_support_subject),
                getString(R.string.write_support_message)
            )
        }

        binding.shareAppIcon.setOnClickListener {
            viewModel.shareApp()
        }

        binding.agreementIcon.setOnClickListener {
            viewModel.legalAgreement()
        }

        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.commit {
                replace(
                    // Указали, в каком контейнере работаем
                    R.id.rootFragmentContainerView,
                    // Создали фрагмент
                    MainFragment.newInstance(),
                    // Указали тег фрагмента
                    MainFragment.TAG
                )

                // Добавляем фрагмент в Back Stack
                addToBackStack(MainFragment.TAG)
            }
        }
    }

    private fun changeThemeMode(isDarkMode: Boolean) {
        if (isDarkMode == true) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.themeSwitcher.isChecked = true
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.themeSwitcher.isChecked = false
        }
    }
}