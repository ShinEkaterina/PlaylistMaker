package com.example.playlistmaker.library.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentLibraryTracksBinding
import com.example.playlistmaker.library.view_model.LibraryTracksFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class LibraryTracksFragment : Fragment() {
    private val libTracksFragmentViewModel: LibraryTracksFragmentViewModel by activityViewModel()

    private var _binding: FragmentLibraryTracksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = LibraryTracksFragment()
    }
}