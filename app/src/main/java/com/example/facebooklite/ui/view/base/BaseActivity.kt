package com.example.facebooklite.ui.view.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.layout_create_post_short.*
import java.time.Duration

abstract class BaseActivity : AppCompatActivity() {
    fun showToast(
        msg: String,
        length: Int = Toast.LENGTH_SHORT
    ) {
        Toast.makeText(this, msg, length).show()
    }

    fun showSnackbar(
        msg: String,
        view: View = root,
        length: Int = Snackbar.LENGTH_INDEFINITE
    ) {
        val snackBar = Snackbar.make(view, msg, length)
        snackBar.setAction("OK") {
            snackBar.dismiss()
        }.show()
    }

}