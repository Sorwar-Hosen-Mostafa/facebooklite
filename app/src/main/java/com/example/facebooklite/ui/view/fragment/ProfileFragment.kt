package com.example.facebooklite.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facebooklite.R
import com.example.facebooklite.adapters.PostListAdapter
import com.example.facebooklite.databinding.FragmentProfileBinding
import com.example.facebooklite.model.Post
import com.example.facebooklite.model.User
import com.example.facebooklite.ui.view.activity.SignIn
import com.example.facebooklite.ui.view.base.BaseActivity
import com.example.facebooklite.ui.view.base.BaseFragment
import com.example.facebooklite.ui.viewmodel.ProfileFragmentViewModel
import com.example.facebooklite.utils.SharedPreferenceConfiguration
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
        binding.llLogout.setOnClickListener {

            SharedPreferenceConfiguration.getInstance(requireContext()).apply {
                setUserInfo(null)
                putBoolean(SharedPreferenceConfiguration.KEY_IS_LOGGED_IN, false)
            }

            Intent(requireContext(),SignIn::class.java).also {
                startActivity(it)
                (requireActivity() as BaseActivity).finishAffinity()
            }
        }
    }

    override fun setObservers() {
        viewModel.allPostLiveData.observe(viewLifecycleOwner){
            when (it.status) {
                Status.SUCCESS -> {
                    postList.clear()
                    postList.addAll(it.data!!.data!!)

                    updatePostList()

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

        viewModel.userLiveData.observe(viewLifecycleOwner){
            when (it.status) {
                Status.SUCCESS -> {
                    user = it.data!!.data!!
                    setUserProfileData()
                    viewModel.getAllPost(user.id)
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {}
            }
        }

    }

    private fun updatePostList() {
        if(postList.size>0){
            binding.rvPosts.visibility = View.VISIBLE
            binding.emptyView.root.visibility = View.GONE
            binding.rvPosts!!.adapter!!.notifyDataSetChanged()
        }else{
            binding.rvPosts.visibility = View.GONE
            binding.emptyView.root.visibility = View.VISIBLE
        }

    }

    override fun getInitialData() {
        if(ProfileFragmentArgs.fromBundle(requireArguments()).selfProfile){
            user = SharedPreferenceConfiguration.getInstance(requireContext()).userInfo!!
            setUserProfileData()
            viewModel.getAllPost(user.id)
            binding.includeCreatePost.llPost.visibility = View.VISIBLE
            binding.uploadPP.visibility = View.VISIBLE
            binding.llLogout.visibility = View.VISIBLE
        }else{
            viewModel.getUserData(ProfileFragmentArgs.fromBundle(requireArguments()).userId)
            binding.includeCreatePost.llPost.visibility = View.GONE
            binding.uploadPP.visibility = View.GONE
            binding.llLogout.visibility = View.GONE
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  DataBindingUtil.inflate(inflater,R.layout.fragment_profile, container, false)

        return binding.root
    }

    private fun setUserProfileData() {

        binding.apply {
            tvUserName.text = user.name
            tvEmail.text = user.email
            tvPhone.text = user.phone
            tvAddress.text = user.address


            Utils.loadImage(user.photo_url,ivProfilePicture)
            Utils.loadImage(user.photo_url,includeCreatePost.postOwnerImage)
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