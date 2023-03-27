package com.example.facebooklite.ui.view.base

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

abstract class ImageCaptureActivity : BaseActivity(){

    protected var image: Uri? = null

    protected fun openOptionMenu() {
        val pictureDialog = AlertDialog.Builder(this)
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
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

        val fileName = "JPEG_${timeStamp}_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile(fileName, ".jpg", storageDir)

        file.also {
            image = FileProvider.getUriForFile(
                this,
                "com.example.facebooklite.provider",
                it
            )
            // Create an intent to launch the camera app and capture an image
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, image)

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
                onImageSelected(image)
            }
        }

    private var resultLauncherForGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                image = result.data!!.data
                onImageSelected(image)
            }
        }

    abstract fun onImageSelected(image: Uri?)

}