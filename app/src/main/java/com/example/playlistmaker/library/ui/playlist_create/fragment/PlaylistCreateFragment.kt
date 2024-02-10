package com.example.playlistmaker.library.ui.playlist_create.fragment

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.common.presentation.ConfirmationDialog
import com.example.playlistmaker.databinding.FragmentPlaylistCreateBinding
import com.example.playlistmaker.library.ui.playlist_create.view_model.PlaylistCreateViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

open class PlaylistCreateFragment : Fragment() {

    private var _binding: FragmentPlaylistCreateBinding? = null
     val binding: FragmentPlaylistCreateBinding
        get() = _binding!!

    private var textWatcherName: TextWatcher? = null
    private var uriImage: Uri? = null
     var playlistNewImgUri: Uri? = null

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uriImage = uri
            if (uri != null) {
                playlistNewImgUri = uri
                //обрабатываем событие выбора пользователем фотографии

                Glide.with(requireContext())
                    .load(uri.toString())
                    .centerCrop()
                    .transform(RoundedCorners(28))
                    .into(binding.ivNewImage)
            }
        }
    private val viewModel by viewModel<PlaylistCreateViewModel>()
  //  private val confirmator: ConfirmationDialog by inject { parametersOf(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playlistCreateToolbar.setNavigationOnClickListener {
            if (binding.ivNewImage.drawable != null
                || !binding.etPlaylistName.text.isNullOrEmpty()
                || !binding.etPlaylistOverview.text.isNullOrEmpty()
            ) {
                showDialog()
            } else {
                findNavController().popBackStack()
            }
        }

        binding.ivNewImage.setOnClickListener {
            loadImage()
        }

        binding.tvCreate.setOnClickListener {
            if (it.isEnabled) {
                if (playlistNewImgUri!= null){
                    viewModel.saveImageToStorage(playlistNewImgUri!!)
                }
                addPlaylist()
                Toast.makeText(
                    requireContext(),
                    "Плейлист ${binding.etPlaylistName.text.toString()} создан",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().popBackStack()
            }
        }

        textWatcherName = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tvCreate.isEnabled =
                    s?.isNotEmpty() == true
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        textWatcherName.let {
            binding.etPlaylistName.addTextChangedListener(it)
        }

        binding.etPlaylistName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                closeKeybord()
                true
            } else {
                false
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isEnabled) {
                    if (binding.ivNewImage.drawable != null
                        || !binding.etPlaylistName.text.isNullOrEmpty()
                        || !binding.etPlaylistOverview.text.isNullOrEmpty()
                    ) {
                        showDialog()
                    } else {
                        findNavController().popBackStack()
                    }
                }
            }

        })
        hideNavigation()

    }

    private fun hideNavigation() {
        // Скрыть нижнюю панель навигации
        activity?.window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                )

        // Скрыть панель навигации на устройствах с Android 10 (API уровень 29) и выше
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity?.window?.insetsController?.hide(WindowInsets.Type.navigationBars())
        }
    }

    private fun closeKeybord() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.etPlaylistName.windowToken, 0)
    }

    private fun showDialog() {
        ConfirmationDialog.showConfirmationDialog(
            requireContext(),
            title = getString(R.string.exit_confirmation_title),
            message = getString(R.string.exit_confirmation_message),
            positiveButton = getString(R.string.finish),
            negativeButton = getString(R.string.cancel),
            positiveAction = {
                findNavController().popBackStack()

            },
            negativeAction = {
            },
            positiveColor = ContextCompat.getColor(requireContext(), (R.color.blue)),
            negativeColor = ContextCompat.getColor(requireContext(), (R.color.blue))
        )
    }

    private fun addPlaylist() {
        viewLifecycleOwner.lifecycleScope.launch {
            if (!binding.etPlaylistName.text.isNullOrEmpty()) {
                viewModel.addPlaylist(
                    Playlist(
                        0,
                        binding.etPlaylistName.text.toString(),
                        binding.etPlaylistOverview.text.toString(),
                        if(playlistNewImgUri!= null) playlistNewImgUri else null,
                        null
                    )
                )
            }
        }
    }


    private fun loadImage() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun ImageView.setImageUriOrDefault(uri: Uri?, default: Int) {
        if (uri != null) {
            this.setImageURI(uri)
            // Проверка, что изображение действительно установлено, если drawable не установлен, используем плейсхолдер
            if (this.drawable == null) {
                this.setImageResource(default)
            }
        } else {
            this.setImageResource(default)
        }
    }
}