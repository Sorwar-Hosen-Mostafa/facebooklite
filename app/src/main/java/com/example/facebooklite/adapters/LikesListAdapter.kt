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
import com.example.facebooklite.databinding.ItemLikerBinding
import com.example.facebooklite.model.Like

class LikesListAdapter(
    var likes: MutableList<Like>,
    val onItemClick: (Like) -> Unit
) :
    RecyclerView.Adapter<LikesListAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(binding: ItemLikerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val _binding: ItemLikerBinding = binding

        init {
            itemView.setOnClickListener {
                onItemClick(likes[adapterPosition])
            }
        }

        fun bind(like: Like) {
            _binding.apply {
                postLikerName.text = like.actorName
                tvMutualFriendCount.text = "0 mutual friends"
                Glide.with(postLikerImage.context)
                    .load("https://7db1-103-87-214-197.ap.ngrok.io"+like.actorImageUrl)
                    .apply(
                        RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .transform()
                    )
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(postLikerImage)

                postLikerImage.clipToOutline = true
                postLikerImage.outlineProvider = ViewOutlineProvider.BACKGROUND
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomViewHolder {
        val itemLikerBinding =
            ItemLikerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return CustomViewHolder(itemLikerBinding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(likes[position])
    }

    override fun getItemCount(): Int = likes.size

}