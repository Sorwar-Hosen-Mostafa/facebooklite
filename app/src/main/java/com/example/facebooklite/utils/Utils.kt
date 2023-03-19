package com.example.facebooklite.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.facebooklite.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*
import kotlin.math.min
import com.example.facebooklite.utils.Constants.BASE_URL

object Utils {
    fun loadImage(
        imageUrl: String?,
        imageView: ImageView,
        placeHolder: Drawable = getImageLoaderDrawable(imageView.context),
        errorImage: Int = R.drawable.ic_photo,
        clipToOutline: Boolean = true
    ) {

        Glide.with(imageView.context)
            .load(BASE_URL + imageUrl)
            .apply(
                RequestOptions()
                    .placeholder(placeHolder)
                    .error(errorImage)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .transform()
            )
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)

        imageView.clipToOutline = clipToOutline
        imageView.outlineProvider = ViewOutlineProvider.BACKGROUND
    }

    fun loadImage(
        imageUri: Uri?,
        imageView: ImageView,
        placeHolder: Drawable = getImageLoaderDrawable(imageView.context),
        errorImage: Int = R.drawable.ic_photo,
        clipToOutline: Boolean = true
    ) {

        Glide.with(imageView.context)
            .load(imageUri)
            .apply(
                RequestOptions()
                    .placeholder(placeHolder)
                    .error(errorImage)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .transform()
            )
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)

        imageView.clipToOutline = clipToOutline
        imageView.outlineProvider = ViewOutlineProvider.BACKGROUND
    }

    private fun getImageLoaderDrawable(context: Context): Drawable {
        val drawable = CircularProgressDrawable(context)
        drawable.setColorSchemeColors(R.color.black, android.R.color.darker_gray)
        drawable.centerRadius = 20f
        drawable.strokeWidth = 5f
        drawable.start()
        return drawable
    }

    fun getRealPathFromURI(uri: Uri?, applicationContext: Context) = uri?.let {
            applicationContext.contentResolver.query(uri, null, null, null, null).use {
                val nameIndex = it!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
                it.moveToFirst()
                val name = it.getString(nameIndex)
                val size = it.getLong(sizeIndex).toString()
                val file = File(applicationContext.filesDir, name)
                try {
                    val inputStream: InputStream? =
                        applicationContext.contentResolver.openInputStream(uri)
                    val outputStream = FileOutputStream(file)
                    var read = 0
                    val maxBufferSize = 1 * 1024 * 1024
                    val bytesAvailable: Int = inputStream?.available() ?: 0
                    //int bufferSize = 1024;
                    val bufferSize = min(bytesAvailable, maxBufferSize)
                    val buffers = ByteArray(bufferSize)
                    while (inputStream?.read(buffers).also { buffer ->
                            if (buffer != null) {
                                read = buffer
                            }
                        } != -1) {
                        outputStream.write(buffers, 0, read)
                    }
                    Log.e("File Size", "Size " + file.length())
                    inputStream?.close()
                    outputStream.close()
                    Log.e("File Path", "Path " + file.path)

                } catch (e: java.lang.Exception) {
                    Log.e("Exception", e.message!!)
                }
                file.path
            }
        }
}