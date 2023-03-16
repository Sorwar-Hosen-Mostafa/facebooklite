package com.example.facebooklite.ui.view.fragment

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
import com.example.facebooklite.ui.viewmodel.PostDetailsViewModel
import com.example.facebooklite.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailsFragment : Fragment() {

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

        val post = PostDetailsFragmentArgs.fromBundle(requireArguments()).post

        setPostDetails(post)

        binding.ivSendComment.setOnClickListener {
            Comment(comment = binding.etComment.text.toString(), postId = post.id).also {
                viewModel.comment(it)
            }
            binding.etComment.text.clear()
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        binding.rvComments.layoutManager = LinearLayoutManager(requireContext())
        binding.rvComments.setHasFixedSize(true)
        binding.rvComments.adapter = CommentsListAdapter(commentsList){

        }

        viewModel.getAllComments(postId = post.id)

        viewModel.commentsLiveData.observe(viewLifecycleOwner){
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

        viewModel.commentLiveData.observe(viewLifecycleOwner){
            when (it.status) {
                Status.SUCCESS -> {
                    commentsList.add(it.data!!.data!!)
                    binding.rvComments!!.adapter!!.notifyDataSetChanged()
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {}
            }
        }

    }

    private fun setPostDetails(post:Post) {
        binding.llPost.apply {
            postTitle.text = post.title
            postBody.text = post.content


            Glide.with(postOwnerImage.context)
                .load("https://7db1-103-87-214-197.ap.ngrok.io" + post.actorImageUrl)
                .apply(
                    RequestOptions()
                        //.placeholder(Utility.showImageLoader(photo.context))
                        //.error(R.drawable.load_failed)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .transform()
                    //.priority(RenderScript.Priority.HIGH)
                )
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(postOwnerImage)

            postOwnerImage.clipToOutline = true
            postOwnerImage.outlineProvider = ViewOutlineProvider.BACKGROUND


            Glide.with(postImage.context)
                .load("https://7db1-103-87-214-197.ap.ngrok.io" + post.postImageUrl)
                .apply(
                    RequestOptions()
                        //.placeholder(Utility.showImageLoader(photo.context))
                        //.error(R.drawable.load_failed)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .transform()
                    //.priority(RenderScript.Priority.HIGH)
                )
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(postImage)



            postOwnerName.text = post.actorName
            postTime.text = "2 min ago"

            totalLikes.text = "${post.likesCount} likes"
            totalComments.text = "${post.commentsCount} Comments"


            ivLike.setOnClickListener {
                ivLike.playAnimation()
            }

            ivComment.setOnClickListener {
                ivComment.playAnimation()
            }

        }

    }
}