package com.example.playlistmaker.library.ui.fragment.root

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentLibraryBinding
import com.example.playlistmaker.library.ui.ViewPageLibraryAdapter
import com.example.playlistmaker.library.ui.fragment.tracks.FavoriteTracksFragment
import com.example.playlistmaker.library.ui.fragment.playlist.PlayListsFragment
import com.google.android.material.tabs.TabLayoutMediator

class LibraryFragment : Fragment() {

    private val fragmentList = listOf(
        FavoriteTracksFragment.newInstance(),
        PlayListsFragment.newInstance()
    )
    private lateinit var fragListTitles: List<String>
    private lateinit var tabMediator: TabLayoutMediator

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragListTitles = listOf(
            getString(R.string.fav_tracks),
            getString(R.string.fav_playlists)
        )

        val adapter = ViewPageLibraryAdapter(
            fragmentManager = childFragmentManager,
            lifecycle = lifecycle,
            fragmentList
        )
        binding.viewPagerLibrary.adapter = adapter

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPagerLibrary) { tab, pos ->
            tab.text = fragListTitles[pos]
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        tabMediator.detach()

    }
}