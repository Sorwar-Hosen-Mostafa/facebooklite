package com.example.facebooklite.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.facebooklite.R
import com.example.facebooklite.databinding.FragmentProfileBinding
import com.example.facebooklite.ui.view.base.BaseFragment


class ProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentProfileBinding
    override fun prepareRecyclerView() {
    }

    override fun setViewClickListeners() {
    }

    override fun setObservers() {
    }

    override fun getInitialData() {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  DataBindingUtil.inflate(inflater,R.layout.fragment_profile, container, false)
        return binding.root
    }


}