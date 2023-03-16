package com.example.facebooklite.ui.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facebooklite.R
import com.example.facebooklite.adapters.PostListAdapter
import com.example.facebooklite.databinding.FragmentHomeBinding
import com.example.facebooklite.model.Post
import com.example.facebooklite.ui.view.activity.MainActivity
import com.example.facebooklite.ui.viewmodel.HomeFragmentViewModel
import com.example.facebooklite.utils.Status
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var postList : ArrayList<Post> = ArrayList()


    private val viewModel: HomeFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,null,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.llCreatePost.postOwnerImage.clipToOutline = true
        binding.llCreatePost.postOwnerImage.outlineProvider = ViewOutlineProvider.BACKGROUND

        binding.rvPosts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPosts.setHasFixedSize(true)
        binding.rvPosts.adapter = PostListAdapter(postList){

        }


        viewModel.getAllPost()

        viewModel.allPostLiveData.observe(viewLifecycleOwner){
            when (it.status) {
                Status.SUCCESS -> {
                    postList.addAll(it.data!!.data!!)
                    binding.rvPosts!!.adapter!!.notifyDataSetChanged()
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {}
            }
        }


        binding.llCreatePost.root.setOnClickListener {
            getBaseNavController().navigate(MainFragmentDirections.actionMainFragmentToCreatePostFragment())
        }

    }


    private fun getBaseNavController(): NavController {
        return (requireActivity() as MainActivity).getNavController()
    }

}