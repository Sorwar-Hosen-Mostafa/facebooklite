package com.example.facebooklite.ui.view.fragment

import android.animation.Animator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.facebooklite.R
import com.example.facebooklite.adapters.CommentsListAdapter
import com.example.facebooklite.databinding.FragmentPostDetailsBinding
import com.example.facebooklite.model.Comment
import com.example.facebooklite.model.Like
import com.example.facebooklite.model.Post
import com.example.facebooklite.ui.view.activity.MainActivity
import com.example.facebooklite.ui.viewmodel.PostDetailsViewModel
import com.example.facebooklite.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailsFragment : Fragment() {

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareCommentRecyclerView()
        setViewClickListeners()


        post = PostDetailsFragmentArgs.fromBundle(requireArguments()).post
        setPostDetails()
        setObservers()
        getData()


    }

    private fun prepareCommentRecyclerView() {
        binding.rvComments.layoutManager = LinearLayoutManager(requireContext())
        binding.rvComments.setHasFixedSize(true)
        binding.rvComments.adapter = CommentsListAdapter(commentsList) {

        }

    }

    private fun getData() {
        viewModel.getAllComments(postId = post.id)
    }

    private fun setObservers() {

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
                    binding.rvComments!!.adapter!!.notifyDataSetChanged()
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {}
            }
        }

        viewModel.commentLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    commentsList.add(it.data!!.data!!)
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

    private fun setViewClickListeners() {
        binding.llPost.ivLike.setOnClickListener {
            it.isEnabled = false
            if(post.liked){
                viewModel.unlikePost(postId = post.id)
            }else{
                viewModel.likePost(postId = post.id)
            }
        }

        binding.ivSendComment.setOnClickListener {
            Comment(comment = binding.etComment.text.toString(), postId = post.id).also {
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


    }

    private fun setPostDetails() {
        binding.llPost.apply {
            postTitle.text = post.title
            postBody.text = post.content
            postOwnerName.text = post.actorName
            postTime.text = "2 min ago"
            totalLikes.text = "${post.likesCount} likes"
            totalComments.text = "${post.commentsCount} Comments"

            updateLikeStatus()

            Glide.with(postOwnerImage.context)
                .load("https://7db1-103-87-214-197.ap.ngrok.io" + post.actorImageUrl)
                .apply(
                    RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .transform()
                )
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(postOwnerImage)
            postOwnerImage.clipToOutline = true
            postOwnerImage.outlineProvider = ViewOutlineProvider.BACKGROUND


            post.postImageUrl?.let {
                Glide.with(postImage.context)
                    .load("https://7db1-103-87-214-197.ap.ngrok.io$it")
                    .apply(
                        RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .transform()
                    )
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(postImage)
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