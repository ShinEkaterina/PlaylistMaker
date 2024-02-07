package com.example.playlistmaker.library.ui.playlist_edit.fragment

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.library.ui.playlist_create.fragment.PlaylistCreateFragment
import com.example.playlistmaker.library.ui.playlist_edit.view_model.PlaylistEditViewModel
import com.example.playlistmaker.util.PLAYLIST_ID
import com.example.playlistmaker.util.createPlaylistFromJson
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistEditFragment: PlaylistCreateFragment() {

    private val viewModel by viewModel<PlaylistEditViewModel>()
    private lateinit var playlist: Playlist
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlist = createPlaylistFromJson(requireArguments().getString(PLAYLIST_ID))
        initView()

        binding.playlistCreateToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.tvCreate.setOnClickListener {
           // val imageName = "${getNameForImage(playlistName = binding.etPlaylistName.text.toString())}.jpg"
            val imageName = viewModel.saveImageToStorage(playlistNewImgUri!!)
            updateImage(imageName)
            updatePlaylist(playlistNewImgUri!!)
            findNavController().popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })
    }

    private fun initView(){
        binding.tvHeading.text = getString(R.string.edit_playlist)
        binding.tvCreate.text = getString(R.string.save)
        binding.etPlaylistName.setText(playlist.name)
        binding.etPlaylistOverview.setText(playlist.description)
        binding.ivNewImage.background = null
        binding.ivNewImage.setImageUriOrDefault(
            playlist.imageUri,
            R.drawable.playlist_card_placeholder_with_padding
        )
        Glide.with(requireContext())
            .load(playlist.imageUri)
            .placeholder(R.drawable.playlist_card_placeholder_with_padding)
            .transform(RoundedCorners(28))
            .into(binding.ivNewImage)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun updateImage(newImageName: String){
/*        viewModel.deleteOldImage(playlist.imageName!!)
        if (binding.ivNewImage.drawable != null &&
            !binding.ivNewImage.drawable.bytesEqualTo(requireContext().getDrawable(R.drawable.playlist_card_placeholder_with_padding)) &&
            !binding.ivNewImage.drawable.pixelsEqualTo(requireContext().getDrawable(R.drawable.playlist_card_placeholder_with_padding)))
            viewModel.saveImageToStorage(
                newImageName,
                binding.ivNewImage.drawable.toBitmap()
            )*/
    }

    private fun updatePlaylist(imageUri: Uri){
        viewModel.updatePlaylist(
            Playlist(
                playlist.id,
                binding.etPlaylistName.text.toString(),
                binding.etPlaylistOverview.text.toString(),
                imageUri,
                playlist.tracks
            )
        )
    }
}