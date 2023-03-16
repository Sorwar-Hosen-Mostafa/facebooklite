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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.facebooklite.R
import com.example.facebooklite.adapters.PostListAdapter
import com.example.facebooklite.databinding.FragmentHomeBinding
import com.example.facebooklite.model.Post
import com.example.facebooklite.model.User
import com.example.facebooklite.ui.view.activity.MainActivity
import com.example.facebooklite.ui.viewmodel.HomeFragmentViewModel
import com.example.facebooklite.utils.SharedPreferenceConfiguration
import com.example.facebooklite.utils.Status
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var postList : ArrayList<Post> = ArrayList()
    private lateinit var user: User


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

        user = SharedPreferenceConfiguration.getInstance(requireContext()).userInfo!!

        Glide.with(requireContext())
            .load("https://7db1-103-87-214-197.ap.ngrok.io"+user.photo_url)
            .apply(
                RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .transform()
            )
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.llCreatePost.postOwnerImage)


        binding.llCreatePost.postOwnerImage.clipToOutline = true
        binding.llCreatePost.postOwnerImage.outlineProvider = ViewOutlineProvider.BACKGROUND

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
                    getBaseNavController().navigate(MainFragmentDirections.actionMainFragmentToPostDetailsFragment(post))
                }
            }

        }


        viewModel.getAllPost()

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


        binding.llCreatePost.root.setOnClickListener {
            getBaseNavController().navigate(MainFragmentDirections.actionMainFragmentToCreatePostFragment())
        }

    }


    private fun getBaseNavController(): NavController {
        return (requireActivity() as MainActivity).getNavController()
    }

    fun findPostById(id:Long): Int{
        for(p in postList){
            if(p.id == id){
                return postList.indexOf(p)
            }
        }
        return -1
    }

}