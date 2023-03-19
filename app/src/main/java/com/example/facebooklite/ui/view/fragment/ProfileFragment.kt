package com.example.facebooklite.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facebooklite.R
import com.example.facebooklite.adapters.PostListAdapter
import com.example.facebooklite.databinding.FragmentProfileBinding
import com.example.facebooklite.model.Post
import com.example.facebooklite.model.User
import com.example.facebooklite.ui.view.base.BaseFragment
import com.example.facebooklite.ui.viewmodel.HomeFragmentViewModel
import com.example.facebooklite.ui.viewmodel.ProfileFragmentViewModel
import com.example.facebooklite.utils.Status
import com.example.facebooklite.utils.Utils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentProfileBinding
    private var postList : ArrayList<Post> = ArrayList()
    private val viewModel: ProfileFragmentViewModel by viewModels()
    private lateinit var user: User

    override fun prepareRecyclerView() {
        binding.rvPosts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPosts.setHasFixedSize(true)
        binding.rvPosts.adapter = PostListAdapter(postList){ post, view ->
            when(view.id){
                R.id.ivLike ->{
                    if(post.liked){
                        viewModel.unlikePost(post.id)
                    }else{
                        viewModel.likePost(post.id)
                    }
                }
                R.id.ivComment->{

                }
                else ->{
                    findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToPostDetailsFragment(post))
                }
            }
        }
    }

    override fun setViewClickListeners() {
    }

    override fun setObservers() {
        viewModel.allPostLiveData.observe(viewLifecycleOwner){
            when (it.status) {
                Status.SUCCESS -> {
                    postList.clear()
                    postList.addAll(it.data!!.data!!)
                    binding.rvPosts!!.adapter!!.notifyDataSetChanged()
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {}
            }
        }

        viewModel.likeLiveData.observe(viewLifecycleOwner){
            when (it.status) {
                Status.SUCCESS -> {
                    val like = it.data!!.data
                    val pos = findPostById(like!!.postId)
                    postList[pos].liked = true
                    postList[pos].likesCount++
                    binding.rvPosts!!.adapter!!.notifyItemChanged(pos)
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {}
            }
        }

        viewModel.unlikeLiveData.observe(viewLifecycleOwner){
            when (it.status) {
                Status.SUCCESS -> {
                    val like = it.data!!.data
                    val pos = findPostById(like!!.postId)
                    postList[pos].liked = false
                    postList[pos].likesCount--
                    binding.rvPosts!!.adapter!!.notifyItemChanged(pos)
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {}
            }
        }

    }

    override fun getInitialData() {
        viewModel.getAllPost(userId = user.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  DataBindingUtil.inflate(inflater,R.layout.fragment_profile, container, false)

        user = ProfileFragmentArgs.fromBundle(requireArguments()).user

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUserProfileData()
    }

    private fun setUserProfileData() {
        binding.apply {
            tvUserName.text = user.name
            tvEmail.text = user.email
            tvPhone.text = user.phone
            tvAddress.text = user.address

            Utils.loadImage(user.photo_url,ivProfilePicture)
        }
    }

    private fun findPostById(id:Long): Int{
        for(p in postList){
            if(p.id == id){
                return postList.indexOf(p)
            }
        }
        return -1
    }
}