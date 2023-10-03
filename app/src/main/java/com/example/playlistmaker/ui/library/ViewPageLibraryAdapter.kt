package com.example.playlistmaker.ui.library

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.ui.library.activity.LibraryActivity

class ViewPageLibraryAdapter (libraryActivity: LibraryActivity, private val list: List<Fragment>): FragmentStateAdapter(libraryActivity) {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
}