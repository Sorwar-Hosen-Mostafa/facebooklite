package com.example.facebooklite.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.facebooklite.databinding.ItemCommentBinding
import com.example.facebooklite.databinding.ItemPostBinding
import com.example.facebooklite.model.Comment
import com.example.facebooklite.model.Post
import com.example.facebooklite.utils.TimeAgo
import com.example.facebooklite.utils.Utils

class CommentsListAdapter(
    var comments: MutableList<Comment>,
    val onItemClick: (Comment) -> Unit
) :
    RecyclerView.Adapter<CommentsListAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val _binding: ItemCommentBinding = binding

        init {
            itemView.setOnClickListener {
                onItemClick(comments[adapterPosition])
            }
        }

        fun bind(comment: Comment) {
            _binding.apply {
                tvComment.text = comment.comment
                postCommenterName.text = comment.actorName
                tvCommentTime.text = TimeAgo.getTimeAgo(comment.createDate!!.time)

                Utils.loadImage(comment.actorImageUrl,postCommenterImage)

                postCommenterImage.clipToOutline = true
                postCommenterImage.outlineProvider = ViewOutlineProvider.BACKGROUND
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomViewHolder {
        val itemCommentBinding =
            ItemCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return CustomViewHolder(itemCommentBinding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    override fun getItemCount(): Int = comments.size

}