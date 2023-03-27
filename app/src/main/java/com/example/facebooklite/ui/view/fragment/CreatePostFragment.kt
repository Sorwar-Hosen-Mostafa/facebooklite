package com.example.facebooklite.ui.view.fragment

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.facebooklite.R
import com.example.facebooklite.databinding.FragmentCreatePostBinding
import com.example.facebooklite.model.User
import com.example.facebooklite.ui.view.base.ImageCaptureFragment
import com.example.facebooklite.ui.viewmodel.CreatePostViewModel
import com.example.facebooklite.utils.ImageType
import com.example.facebooklite.utils.SharedPreferenceConfiguration
import com.example.facebooklite.utils.Status
import com.example.facebooklite.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class CreatePostFragment : ImageCaptureFragment() {

    companion object{
         const val MAX_POST_LENGTH = 500
    }
    private lateinit var binding: FragmentCreatePostBinding


    private val viewModel: CreatePostViewModel by viewModels()
    private lateinit var user: User


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_post,container,false)
        return binding.root
    }

    override fun onImageSelected(image: Uri?,imageType: ImageType) {
        when(imageType){
            ImageType.POST_IMAGE -> {
                Utils.loadImage(image, binding.llCreatePost.postImage)
                binding.llCreatePost.postImage.visibility = View.VISIBLE
            }
            else -> {}
        }

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
            openOptionMenu(ImageType.POST_IMAGE)
        }

        binding.llCreatePost.postButton.setOnClickListener {
            if(isAllValid()){
                viewModel.addPost(
                    binding.llCreatePost.postTitleEdittext.text.toString(),
                    binding.llCreatePost.postBodyEdittext.text.toString(),
                    Utils.getRealPathFromURI(image, requireContext())?.let { uri -> File(uri) }
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


}