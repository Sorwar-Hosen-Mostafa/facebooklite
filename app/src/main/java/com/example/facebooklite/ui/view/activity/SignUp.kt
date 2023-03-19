package com.example.facebooklite.ui.view.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.facebooklite.R
import com.example.facebooklite.databinding.ActivitySignUpBinding
import com.example.facebooklite.ui.view.base.BaseActivity
import com.example.facebooklite.ui.viewmodel.SignUpViewModel
import com.example.facebooklite.utils.Status
import com.example.facebooklite.utils.Utils
import com.example.facebooklite.utils.Utils.getRealPathFromURI
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

@AndroidEntryPoint
class SignUp : BaseActivity() {


    private var profilePicture: Uri? = null
    private val IMAGE_CAPTURE_CODE: Int = 11
    private val IMAGE_PICK_CODE: Int = 12
    private lateinit var binding: ActivitySignUpBinding
    private val _signUpViewModel: SignUpViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

       /* binding.emailField.setText("mostafa@gmail.com")
        binding.nameField.setText("mostafa")
        binding.passwordField.setText("123")
        binding.phoneField.setText("01765242424")
        binding.addressField.setText("dhaka")*/


        viewClickEvents()
        prepareObservers()
    }

    private fun viewClickEvents() {
        binding.signInButton.setOnClickListener {
            Intent(this, SignIn::class.java).run {
                startActivity(this)
            }
        }


        binding.btnUploadPicture.setOnClickListener {
            checkPermission()
        }

        binding.signUpButton.setOnClickListener {
            if (isAllDataValid()) {

                binding.run {
                    _signUpViewModel.signUp(
                        email = emailField.text.toString(),
                        name = nameField.text.toString(),
                        password = passwordField.text.toString(),
                        phone = phoneField.text.toString(),
                        address = addressField.text.toString(),
                        profilePic = getRealPathFromURI(profilePicture,this@SignUp)?.let { uri -> File(uri) }
                    )
                }

            }
        }
    }

    private fun checkPermission() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) -> {
                openOptionMenu()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun openOptionMenu() {
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
            profilePicture = FileProvider.getUriForFile(
                this,
                "com.example.facebooklite.provider",
                it
            )
            // Create an intent to launch the camera app and capture an image
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, profilePicture)

            resultLauncherForCamera.launch(takePictureIntent)

        }

    }

    private var resultLauncherForCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Utils.loadImage(profilePicture,binding.ivProfilePicture)
            }
        }

    private fun isAllDataValid(): Boolean {
        return true
    }

    private fun prepareObservers() {
        _signUpViewModel.signUpResponseLiveData.observe(this) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    Log.e("TAG", resource.data!!.data.toString())
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {}
            }

        }
    }






    @RequiresApi(Build.VERSION_CODES.O)
    fun createFileFromContentUri(fileUri: Uri): File {

        var fileName: String = ""

        fileUri.let { returnUri ->
            applicationContext.contentResolver.query(returnUri, null, null, null)
        }?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            fileName = cursor.getString(nameIndex)
        }

        //  For extract file mimeType
        val fileType: String? = fileUri.let { returnUri ->
            applicationContext.contentResolver.getType(returnUri)
        }

        val iStream: InputStream = applicationContext.contentResolver.openInputStream(fileUri)!!
        val outputDir: File = applicationContext?.cacheDir!!
        val outputFile: File = File(outputDir, fileName)
        copyStreamToFile(iStream, outputFile)
        iStream.close()
        return outputFile
    }

    fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }
    }

    private fun startGalleryIntent() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        resultLauncherForGallery.launch(galleryIntent)

    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                openOptionMenu()
            } else {
                Toast.makeText(this, "Permission rejected", Toast.LENGTH_SHORT).show()
            }
        }







    private var resultLauncherForGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                profilePicture = result.data!!.data
                Utils.loadImage(profilePicture,binding.ivProfilePicture)
            }
        }
}