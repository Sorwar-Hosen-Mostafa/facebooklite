package com.example.facebooklite.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facebooklite.R
import com.example.facebooklite.adapters.LikesListAdapter
import com.example.facebooklite.databinding.FragmentLikersListBinding
import com.example.facebooklite.model.Like
import com.example.facebooklite.ui.viewmodel.LikerFragmentViewModel
import com.example.facebooklite.ui.viewmodel.PostDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LikerListFragment : Fragment() {

    private var postId: Long = -1
    private val likesList: ArrayList<Like> = ArrayList()
    private lateinit var binding: FragmentLikersListBinding
    private val viewModel: LikerFragmentViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_likers_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postId = LikerListFragmentArgs.fromBundle(requireArguments()).postId
        prepareRecyclerView()
        setObservers()
        getData()
    }

    private fun getData() {
        viewModel.getAllLikes(postId)
    }

    private fun setObservers() {
        viewModel.likerLiveData.observe(viewLifecycleOwner){
            likesList.clear()
            likesList.addAll(it.data!!.data!!)
            binding.rvLikes.adapter!!.notifyDataSetChanged()
        }
    }

    private fun prepareRecyclerView() {
        binding.rvLikes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLikes.setHasFixedSize(true)
        binding.rvLikes.adapter = LikesListAdapter(likesList) {

        }
    }


}