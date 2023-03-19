package com.example.facebooklite.ui.view.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.facebooklite.R
import com.example.facebooklite.databinding.FragmentCreatePostBinding
import com.example.facebooklite.model.User
import com.example.facebooklite.ui.view.base.BaseFragment
import com.example.facebooklite.ui.viewmodel.CreatePostViewModel
import com.example.facebooklite.ui.viewmodel.ProfileFragmentViewModel
import com.example.facebooklite.utils.SharedPreferenceConfiguration
import com.example.facebooklite.utils.Status
import com.example.facebooklite.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class CreatePostFragment : BaseFragment() {

    companion object{
         const val MAX_POST_LENGTH = 500
    }
    private lateinit var binding: FragmentCreatePostBinding

    private var postImage: Uri? = null

    private val viewModel: CreatePostViewModel by viewModels()
    private lateinit var user: User


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_post,container,false)
        return binding.root
    }

    override fun prepareRecyclerView() {
    }

    private fun isAllValid(): Boolean {
        binding.run {
            if (
                binding.llCreatePost.postBodyEdittext.text.toString().isEmpty()
            ) {
                showToast("Enter post content")
                return false
            }
            return true
        }
    }

    override fun setViewClickListeners() {

        binding.llCreatePost.ivInsertImage.setOnClickListener {
            openOptionMenu()
        }

        binding.llCreatePost.postButton.setOnClickListener {
            if(isAllValid()){
                viewModel.addPost(
                    binding.llCreatePost.postTitleEdittext.text.toString(),
                    binding.llCreatePost.postBodyEdittext.text.toString(),
                    Utils.getRealPathFromURI(postImage, requireContext())?.let { uri -> File(uri) }
                )
            }
        }
    }

    override fun setObservers() {
        binding.llCreatePost.postBodyEdittext.apply {
            filters = arrayOf(InputFilter.LengthFilter(MAX_POST_LENGTH))
            addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    s?.let {
                        binding.llCreatePost.postLength.text = "${it.length}/$MAX_POST_LENGTH"
                    }?:  binding.llCreatePost.postLength.setText("0/$MAX_POST_LENGTH")
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }

        viewModel.postLiveData.observe(viewLifecycleOwner){
            when (it.status) {
                Status.SUCCESS -> {
                    showToast("Post successfully created")
                    findNavController().navigateUp()
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {}
            }
        }
    }

    override fun getInitialData() {
        user = SharedPreferenceConfiguration.getInstance(requireContext()).userInfo!!
        binding.llCreatePost.postOwnerName.text = user.name


        Utils.loadImage(user.photo_url,binding.llCreatePost.postOwnerImage)

    }


    private fun openOptionMenu() {
        val pictureDialog = AlertDialog.Builder(requireContext())
        pictureDialog.setTitle("Select Action")
        val pictureDialogItem = arrayOf(
            "Select photo from Gallery",
            "Capture photo from Camera"
        )
        pictureDialog.setItems(pictureDialogItem) { dialog, which ->

            when (which) {
                0 -> startGalleryIntent()
                1 -> takePicture()
            }
        }

        pictureDialog.show()
    }

    private fun takePicture() {
        // Create a unique file name for the image
        val profilePicture : Uri?
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

        val fileName = "JPEG_${timeStamp}_"
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile(fileName, ".jpg", storageDir)

        file.also {
            profilePicture = FileProvider.getUriForFile(
                requireContext(),
                "com.example.facebooklite.provider",
                it
            )
            // Create an intent to launch the camera app and capture an image
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, profilePicture)

            resultLauncherForCamera.launch(takePictureIntent)

        }

    }

    private fun startGalleryIntent() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        resultLauncherForGallery.launch(galleryIntent)

    }

    private var resultLauncherForCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Utils.loadImage(postImage,binding.llCreatePost.postImage)
                binding.llCreatePost.postImage.visibility = View.VISIBLE
            }
        }

    private var resultLauncherForGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                postImage = result.data!!.data

                Utils.loadImage(postImage,binding.llCreatePost.postImage)
                binding.llCreatePost.postImage.visibility = View.VISIBLE
            }
        }

}