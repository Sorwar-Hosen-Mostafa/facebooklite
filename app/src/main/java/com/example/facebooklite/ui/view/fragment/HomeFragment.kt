package com.example.facebooklite.ui.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facebooklite.R
import com.example.facebooklite.adapters.PostListAdapter
import com.example.facebooklite.databinding.FragmentHomeBinding
import com.example.facebooklite.model.Post
import com.example.facebooklite.model.User
import com.example.facebooklite.ui.view.activity.MainActivity
import com.example.facebooklite.ui.view.base.BaseFragment
import com.example.facebooklite.ui.viewmodel.HomeFragmentViewModel
import com.example.facebooklite.utils.SharedPreferenceConfiguration
import com.example.facebooklite.utils.Status
import com.example.facebooklite.utils.Utils
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding
    private var postList : ArrayList<Post> = ArrayList()
    private lateinit var user: User


    private val viewModel: HomeFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,null,false)

        user = SharedPreferenceConfiguration.getInstance(requireContext()).userInfo!!

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Utils.loadImage(user.photo_url,binding.llCreatePost.postOwnerImage)

    }

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
                R.id.post_owner_image->{
                    if(post.actorId == user.id){
                        getBaseNavController().navigate(MainFragmentDirections.actionMainFragmentToProfileFragment(true,user.id))
                    }else{
                        getBaseNavController().navigate(MainFragmentDirections.actionMainFragmentToProfileFragment(false,post.actorId))
                    }
                }
                else ->{
                    getBaseNavController().navigate(MainFragmentDirections.actionMainFragmentToPostDetailsFragment(post))
                }
            }
        }
    }

    override fun setViewClickListeners() {
        binding.llCreatePost.root.setOnClickListener {
            getBaseNavController().navigate(MainFragmentDirections.actionMainFragmentToCreatePostFragment())
        }

        binding.llCreatePost.postOwnerImage.setOnClickListener {
            val user = SharedPreferenceConfiguration(requireContext()).userInfo!!
            getBaseNavController().navigate(MainFragmentDirections.actionMainFragmentToProfileFragment(true,user.id))
        }
    }

    override fun setObservers() {
        viewModel.allPostLiveData.observe(viewLifecycleOwner){
            when (it.status) {
                Status.SUCCESS -> {
                    postList.clear()
                    postList.addAll(it.data!!.data!!)
                    updatePostList()
                    binding.loaderLayout.root.visibility = View.GONE
                }
                Status.ERROR -> {
                    binding.loaderLayout.root.visibility = View.GONE
                    showSnackbar(it.message.toString(),binding.root,Snackbar.LENGTH_INDEFINITE)
                }
                Status.LOADING -> {
                    binding.loaderLayout.root.visibility = View.VISIBLE
                }
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
                Status.LOADING -> {

                }
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
        viewModel.getAllPost()
    }


    private fun getBaseNavController(): NavController {
        return (requireActivity() as MainActivity).getNavController()
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