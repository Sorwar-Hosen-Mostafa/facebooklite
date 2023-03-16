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
        }

        fun bind(post: Post) {
            _binding.apply {

                postTitle.text = post.title
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

                Glide.with(postOwnerImage.context)
                    .load("https://7db1-103-87-214-197.ap.ngrok.io"+post.actorImageUrl)
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


                Glide.with(postImage.context)
                    .load("https://7db1-103-87-214-197.ap.ngrok.io"+post.postImageUrl)
                    .apply(
                        RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .transform()
                    )
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(postImage)



                postOwnerName.text = post.actorName
                postTime.text = "2 min ago"

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