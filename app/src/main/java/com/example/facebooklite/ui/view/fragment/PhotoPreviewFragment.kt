package com.example.facebooklite.ui.view.fragment

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.facebooklite.R
import com.example.facebooklite.databinding.FragmentPhotoViewBinding
import com.example.facebooklite.ui.view.base.BaseFragment
import com.example.facebooklite.utils.Constants.BASE_URL
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_photo_view.*
import java.util.*

@AndroidEntryPoint
class PhotoPreviewFragment : BaseFragment() {

    private lateinit var binding: FragmentPhotoViewBinding
    private lateinit var photoUrl: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo_view,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photoUrl = BASE_URL+PhotoPreviewFragmentArgs.fromBundle(requireArguments()).photoUrl
        updateProgress(true)
        loadPhoto()
        initClickListeners()
    }

    override fun prepareRecyclerView() {
    }

    override fun setViewClickListeners() {
    }

    override fun setObservers() {
    }

    override fun getInitialData() {
    }

    private fun loadPhoto() {
        Glide.with(requireContext())
            .load(photoUrl)
            .apply(
                RequestOptions()
                    .transform(FitCenter())
                    .error(R.drawable.ic_photo)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .priority(Priority.HIGH)
            )
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    updateProgress(false)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    updateProgress(false)
                    return false
                }

            })
            .into(photoView)
    }

    private fun updateProgress(isLoading: Boolean) {
        if (isLoading) {
            lottieProgress?.let {
                it.visibility = View.VISIBLE
            }
        } else {
            lottieProgress?.let {
                it.visibility = View.GONE
            }
        }

    }

    private fun initClickListeners() {

        saveImage.setOnClickListener {
            if (checkPermissions()) {
                downloadPhoto()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }

        shareImage.setOnClickListener {
            shareImage()
        }

        back.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PermissionChecker.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                downloadPhoto()
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    showToast(
                        "Storage permission is required to download image")
                } else {
                    showToast(
                        "Storage permission is denied, please provide access to the permission from app settings"
                    )
                }
            }
        }

    private fun downloadPhoto() {

        val imageName =   "${Date().time}.jpeg"
        val request = DownloadManager.Request(Uri.parse(photoUrl))
            .setTitle(imageName)
            .setDescription("Downloading $imageName")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, imageName)
        val manager =
            requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
        showToast("Download Started")
    }

    private fun shareImage() {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, photoUrl)
            startActivity(Intent.createChooser(this, "Share image using"))
        }
    }

}