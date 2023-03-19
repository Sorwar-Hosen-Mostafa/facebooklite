package com.example.facebooklite.adapters

import android.view.LayoutInflater
import android.view.View
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
import com.example.facebooklite.utils.Utils

class LikesListAdapter(
    var likes: MutableList<Like>,
    var currentUserId: Long,
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
                Utils.loadImage(like.actorImageUrl,postLikerImage)
            }

            if(like.actorId == currentUserId){
                _binding.btnAddFriend.visibility = View.GONE
                _binding.tvMutualFriendCount.text = "You liked the post.."
            }else{
                _binding.btnAddFriend.visibility = View.VISIBLE
                _binding.tvMutualFriendCount.text = "0 Mutual Friend"
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