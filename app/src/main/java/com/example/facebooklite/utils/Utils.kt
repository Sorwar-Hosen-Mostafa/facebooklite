package com.example.facebooklite.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.facebooklite.R

object Utils {
    fun loadImage(
        imageUrl: String?,
        imageView: ImageView,
        placeHolder: Drawable = getImageLoaderDrawable(imageView.context),
        errorImage: Int = R.drawable.ic_photo,
        clipToOutline: Boolean = true
    ){

        Glide.with(imageView.context)
            .load("https://7db1-103-87-214-197.ap.ngrok.io$imageUrl")
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
    ){

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

    private fun getImageLoaderDrawable(context: Context): Drawable{
        val drawable = CircularProgressDrawable(context)
        drawable.setColorSchemeColors(R.color.black,android.R.color.darker_gray)
        drawable.centerRadius = 20f
        drawable.strokeWidth = 5f
        drawable.start()
        return drawable
    }
}