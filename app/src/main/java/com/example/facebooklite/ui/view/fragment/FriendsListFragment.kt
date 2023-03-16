package com.example.facebooklite.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.facebooklite.R
import com.example.facebooklite.ui.view.base.BaseFragment


class FriendsListFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_friends_list, container, false)
    }

    override fun prepareRecyclerView() {

    }

    override fun setViewClickListeners() {
    }

    override fun setObservers() {
    }

    override fun getInitialData() {
    }


}