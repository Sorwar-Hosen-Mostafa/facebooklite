package com.example.facebooklite.ui.view.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class BaseActivity : AppCompatActivity() {
    fun showToast(
        msg: String,
        length: Int = Toast.LENGTH_SHORT
    ) {
        Toast.makeText(this, msg, length).show()
    }

}