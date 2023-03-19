package com.example.facebooklite.adapters

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.facebooklite.databinding.ItemPostBinding
import com.example.facebooklite.model.Post
import com.example.facebooklite.utils.TimeAgo
import com.example.facebooklite.utils.Utils
import okio.Utf8

class PostListAdapter(
    var postList: MutableList<Post>,
    val onItemClick: (Post, View) -> Unit
) :
    RecyclerView.Adapter<PostListAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val _binding: ItemPostBinding = binding

        init {
            itemView.setOnClickListener {
                onItemClick(postList[adapterPosition],it)
            }

            _binding.ivLike.setOnClickListener {
                onItemClick(postList[adapterPosition],it)
            }

            _binding.ivComment.setOnClickListener {
                onItemClick(postList[adapterPosition],it)
            }

            _binding.postOwnerImage.setOnClickListener {
                onItemClick(postList[adapterPosition],it)
            }
        }

        fun bind(post: Post) {
            _binding.apply {

                if(post.title.isNullOrEmpty()){
                    postTitle.visibility = View.GONE
                }else{
                    postTitle.text = post.title
                    postTitle.visibility = View.VISIBLE
                }

                postBody.text = post.content

                ivLike.addAnimatorListener(object : AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        ivLike.progress = 1f
                    }

                    override fun onAnimationCancel(animation: Animator) {
                    }

                    override fun onAnimationRepeat(animation: Animator) {
                    }

                })

                Utils.loadImage(post.actorImageUrl,postOwnerImage)
                Utils.loadImage(post.postImageUrl,postImage)

                if(post.postImageUrl.isNullOrEmpty()){
                    postImage.visibility = View.GONE
                }else{
                    postImage.visibility = View.VISIBLE
                }


                postOwnerName.text = post.actorName
                postTime.text = TimeAgo.getTimeAgo(post.createDate.time)

                totalLikes.text = "${post.likesCount} likes"
                totalComments.text = "${post.commentsCount} Comments"

                if(post.liked){
                    ivLike.playAnimation()
                }else{
                    ivLike.progress = 0f
                }

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomViewHolder {
        val itemPostBinding =
            ItemPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return CustomViewHolder(itemPostBinding)
    }

    override fun onBindViewHolder(holder: PostListAdapter.CustomViewHolder, position: Int) {
        holder.bind(postList[position])
    }

    override fun getItemCount(): Int = postList.size

}