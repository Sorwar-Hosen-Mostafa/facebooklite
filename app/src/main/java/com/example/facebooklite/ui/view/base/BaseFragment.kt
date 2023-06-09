package com.example.facebooklite.ui.view.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView()
        setViewClickListeners()
        setObservers()
        getInitialData()
    }

    abstract fun prepareRecyclerView()
    abstract fun setViewClickListeners()
    abstract fun setObservers()
    abstract fun getInitialData()
    fun showToast(msg: String) {
        (requireActivity() as BaseActivity).showToast(msg)
    }

    fun showSnackbar(
        msg: String,
        view: View,
        length: Int
    ) {
        (requireActivity() as BaseActivity).showSnackbar(msg, view, length)
    }

}