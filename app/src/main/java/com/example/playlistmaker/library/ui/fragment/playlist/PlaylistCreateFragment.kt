package com.example.playlistmaker.library.ui.fragment.playlist

import android.net.Uri
import android.text.TextWatcher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.playlistmaker.databinding.FragmentPlaylistCreateBinding

class PlaylistCreateFragment : Fragment() {

    private var _binding: FragmentPlaylistCreateBinding? = null
    private val binding: FragmentPlaylistCreateBinding
        get() = _binding!!

    private var textWatcherName: TextWatcher? = null
    private var uriImage: Uri? = null
    private var playlistImgName: String? = null
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uriImage = uri
        if (uri != null) {
            Glide.with(requireContext())
                .load(uri)
                .into(binding.ivNewImage)
        }
    }
    private val viewModel by viewModel<PlaylistCreateViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPlaylistCreatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBtnBack.setOnClickListener {
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
                saveImageToStorage()
                addPlaylist()
                Toast.makeText(
                    requireContext(),
                    "Плейлист ${binding.etPlaylistName.text.toString()} создан",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().popBackStack()
            } else {

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
    }

    private fun closeKeybord() {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.etPlaylistName.windowToken, 0)
    }

    private fun showDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNegativeButton("Отмена") { dialog, which ->
            }
            .setPositiveButton("Да") { dialog, which ->
                findNavController().popBackStack()
            }
        dialog.show()
    }

    private fun addPlaylist() {
        viewLifecycleOwner.lifecycleScope.launch {
            if (!binding.etPlaylistName.text.isNullOrEmpty()) {
                viewModel.addPlaylist(
                    Playlist(
                        0,
                        binding.etPlaylistName.text.toString(),
                        binding.etPlaylistOverview.text.toString(),
                        playlistImgName,
                        null
                    )
                )
            }
        }
    }

    private fun saveImageToStorage() {
        viewLifecycleOwner.lifecycleScope.launch {
            val filePath =
                File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), PLAYLIST_STORAGE_NAME)

            if (!filePath.exists()) {
                filePath.mkdirs()
            }

            playlistImgName = "${getNameForImage(playlistName = binding.etPlaylistName.text.toString())}.jpg"

            val file = File(filePath, playlistImgName)
            val outputStream = FileOutputStream(file)

            if (uriImage != null) {
                val inputStream = requireContext().contentResolver.openInputStream(uriImage!!)
                BitmapFactory
                    .decodeStream(inputStream)
                    .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

            }
        }
    }

    private fun loadImage() {
        pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}