package com.example.facebooklite.adapters

import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.facebooklite.databinding.ItemPostBinding
import com.example.facebooklite.model.Post
import kotlin.math.round

class PostListAdapter(
    var postList: MutableList<Post>,
    val onItemClick: (Post) -> Unit
) :
    RecyclerView.Adapter<PostListAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val _binding: ItemPostBinding = binding

        init {
            itemView.setOnClickListener {
                onItemClick(postList[adapterPosition])
            }
        }

        fun bind(post: Post) {
            _binding.apply {

                postTitle.text = post.title
                postBody.text = post.content


                Glide.with(postOwnerImage.context)
                    .load("https://7db1-103-87-214-197.ap.ngrok.io"+post.actorImageUrl)
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
                    .load("https://7db1-103-87-214-197.ap.ngrok.io"+post.postImageUrl)
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