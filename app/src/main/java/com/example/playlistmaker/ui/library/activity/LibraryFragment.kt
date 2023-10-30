package com.example.playlistmaker.ui.library.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentLibraryBinding
import com.example.playlistmaker.ui.library.ViewPageLibraryAdapter
import com.google.android.material.tabs.TabLayoutMediator


class LibraryFragment : Fragment() {
    companion object {
        const val TAG = "LibraryFragment"

        fun newInstance(): Fragment {
            return LibraryFragment()
        }
    }

    private val fragList = listOf(
        LibraryTracksFragment.newInstance(),
        PlayListsFragment.newInstance()
    )
    private lateinit var fragListTitles: List<String>
    private lateinit var tabMediator: TabLayoutMediator
    private lateinit var binding: FragmentLibraryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
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
            fragList)
        binding.viewPagerLibrary.adapter = adapter

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPagerLibrary) { tab, pos ->
            tab.text = fragListTitles[pos]
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()

    }
}