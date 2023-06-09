package com.example.facebooklite.ui.view.fragment

import android.animation.Animator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facebooklite.R
import com.example.facebooklite.adapters.CommentsListAdapter
import com.example.facebooklite.databinding.FragmentPostDetailsBinding
import com.example.facebooklite.model.Comment
import com.example.facebooklite.model.Like
import com.example.facebooklite.model.Post
import com.example.facebooklite.ui.view.activity.MainActivity
import com.example.facebooklite.ui.view.base.BaseFragment
import com.example.facebooklite.ui.viewmodel.PostDetailsViewModel
import com.example.facebooklite.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class PostDetailsFragment : BaseFragment() {

    private lateinit var post: Post
    private val commentsList: ArrayList<Comment> = ArrayList()
    private val likesList: ArrayList<Like> = ArrayList()
    private lateinit var binding: FragmentPostDetailsBinding
    private val viewModel: PostDetailsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_post_details, container, false)

        post = PostDetailsFragmentArgs.fromBundle(requireArguments()).post

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPostDetails()
    }

    override fun prepareRecyclerView() {
        binding.rvComments.layoutManager = LinearLayoutManager(requireContext())
        binding.rvComments.setHasFixedSize(true)
        binding.rvComments.adapter = CommentsListAdapter(commentsList) {

        }
    }


    override fun setObservers() {

        binding.llPost.ivLike.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                if(post.liked){
                    binding.llPost.ivLike.progress = 1f
                    binding.llPost.ivLike.isEnabled = true
                }else{
                    binding.llPost.ivLike.progress = 0f
                }
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }

        })


        viewModel.commentsLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    commentsList.clear()
                    commentsList.addAll(it.data!!.data!!)
                    binding.rvComments.adapter!!.notifyDataSetChanged()
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {}
            }
        }

        viewModel.commentLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    val c = it.data!!.data!!
                    c.createDate = Date()
                    commentsList.add(0,c)
                    post.commentsCount++
                    binding.llPost.totalComments.text = "${post.commentsCount} Comments"
                    binding.rvComments!!.adapter!!.notifyDataSetChanged()
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {}
            }
        }

        viewModel.likeLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    post.liked = true
                    post.likesCount++
                    updateLikeStatus()
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {}
            }
        }

        viewModel.unlikeLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    post.liked = false
                    post.likesCount--
                    updateLikeStatus()
                    binding.llPost.ivLike.isEnabled = true
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {}
            }
        }
    }

    override fun getInitialData() {
        viewModel.getAllComments(postId = post.id)
    }

    override fun setViewClickListeners() {
        binding.llPost.ivLike.setOnClickListener {
            it.isEnabled = false
            if(post.liked){
                viewModel.unlikePost(postId = post.id)
            }else{
                viewModel.likePost(postId = post.id)
            }
        }

        binding.ivSendComment.setOnClickListener {
            Comment(comment = binding.etComment.text.toString(), postId = post.id, createDate = null).also {
                viewModel.comment(it)
            }
            binding.etComment.text.clear()
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }

        binding.llPost.totalLikes.setOnClickListener {
            findNavController().navigate(PostDetailsFragmentDirections.actionPostDetailsFragmentToLikersListFragment(post.id))
        }

        binding.llPost.postImage.setOnClickListener {
            findNavController().navigate(PostDetailsFragmentDirections.actionPostDetailsFragmentToPhotoPreviewFragment(post.postImageUrl!!))
        }

        binding.llPost.postOwnerImage.setOnClickListener {
            if(post.actorId == SharedPreferenceConfiguration.getInstance(requireContext()).userInfo!!.id){
                findNavController().navigate(PostDetailsFragmentDirections.actionPostDetailsFragmentToProfileFragment(true,post.actorId))
            }else{
                findNavController().navigate(PostDetailsFragmentDirections.actionPostDetailsFragmentToProfileFragment(false,post.actorId))
            }
        }

    }

    private fun setPostDetails() {
        binding.llPost.apply {


            if(post.title.isNullOrEmpty()){
                postTitle.visibility = View.GONE
            }else{
                postTitle.text = post.title
                postTitle.visibility = View.VISIBLE
            }

            postBody.text = post.content
            postOwnerName.text = post.actorName
            postTime.text = TimeAgo.getTimeAgo(post.createDate.time)
            totalLikes.text = "${post.likesCount} likes"
            totalComments.text = "${post.commentsCount} Comments"

            updateLikeStatus()

            Utils.loadImage(post.actorImageUrl,postOwnerImage)
            Utils.loadImage(post.postImageUrl,postImage)

            if(post.postImageUrl.isNullOrEmpty()){
                postImage.visibility = View.GONE
            }else{
                postImage.visibility = View.VISIBLE
            }

        }
    }

    private fun updateLikeStatus() {
        binding.llPost.totalLikes.text = "${post.likesCount} likes"
        if(post.liked){
            binding.llPost.ivLike.playAnimation()
        }else{
            binding.llPost.ivLike.progress = 0f
        }

    }

    private fun getBaseNavController(): NavController {
        return (requireActivity() as MainActivity).getNavController()
    }

}