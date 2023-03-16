package com.example.facebooklite.ui.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facebooklite.R
import com.example.facebooklite.adapters.PostListAdapter
import com.example.facebooklite.databinding.FragmentHomeBinding
import com.example.facebooklite.model.Post
import com.example.facebooklite.model.User
import com.example.facebooklite.ui.view.activity.MainActivity


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var postList : ArrayList<Post> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,null,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.llCreatePost.postOwnerImage.clipToOutline = true
        binding.llCreatePost.postOwnerImage.outlineProvider = ViewOutlineProvider.BACKGROUND


        postList.add(Post(1,
            User(1,"Sorwar Hosen", photo_url = "https://picsum.photos/500"),
            title = "The Art of Baking Bread",
            postImageUrl =  "https://picsum.photos/500/1000",
            content = "Bread baking has been around for thousands of years and is a staple in many cultures. The process of making bread can be a rewarding and enjoyable experience. From the mixing of ingredients, to the kneading of dough, to the baking of the finished product, there is a sense of accomplishment in every step. The aroma of freshly baked bread can fill a room and is sure to impress family and friends. With so many variations of bread, from sourdough to brioche, the possibilities are endless. So why not give bread baking a try and see what delicious creations you can come up with!",
            likes = ArrayList(),
            comments = ArrayList()))
        postList.add(Post(1,
            User(1,"Sorwar Hosen", photo_url = "https://picsum.photos/500"),
            title ="The Benefits of Yoga for Mind and Body",
            postImageUrl =  "https://picsum.photos/500/1001",
            content = "Yoga is a practice that has been around for centuries and has become increasingly popular in recent years. It combines physical postures, breathing techniques, and meditation to improve both mental and physical health. The benefits of yoga are numerous, including increased flexibility, strength, and balance, as well as reduced stress and anxiety. Regular yoga practice can also improve sleep, digestion, and overall well-being. Whether you are a beginner or an experienced yogi, there is always something new to learn and explore on the mat."
            ,likes = ArrayList(),
            comments = ArrayList()))
        postList.add(Post(1,
            User(1,"Sorwar Hosen", photo_url = "https://picsum.photos/500"),
            title ="The Art of Photography",
            content = "Photography is a form of art that allows us to capture and preserve moments in time. With the advent of digital cameras and smartphones, photography has become more accessible than ever before. However, there is still an art to capturing a great photo. From choosing the right lighting and composition, to editing and post-processing, there are many elements to consider. Photography can be a hobby or a profession, and can allow us to express our creativity and view the world through a different lens.",likes = ArrayList(),
            comments = ArrayList()))
        postList.add(Post(1,
            User(1,"Sorwar Hosen", photo_url = "https://picsum.photos/500"),
            title ="The History of Video Games",
            postImageUrl =  "https://picsum.photos/500/1003",
            content = "Video games have come a long way since their inception in the 1950s. From arcade games to home consoles, to mobile apps, video games have become a ubiquitous form of entertainment. They have also evolved into a medium for storytelling and art, with immersive worlds and characters that capture our imagination. The history of video games is a fascinating journey, from the early days of Pong and Space Invaders, to the rise of Nintendo and Sega, to the modern era of virtual reality and esports.",likes = ArrayList(),
            comments = ArrayList()))
        postList.add(Post(1,
            User(1,"Sorwar Hosen", photo_url = "https://picsum.photos/500"),
            title ="The Joy of Traveling",
            postImageUrl =  "https://picsum.photos/500/1004",
            content = "Traveling is a wonderful way to experience new cultures, meet new people, and broaden our horizons. From the bustling streets of Tokyo to the serene beaches of Bali, there is a world of adventure waiting to be explored. Traveling can also be a great way to disconnect from our daily routine and recharge our batteries. Whether it's a weekend getaway or a months-long backpacking trip, there is something special about discovering new places and creating unforgettable memories. So pack your bags, grab your passport, and let the adventure begin!",likes = ArrayList(),
            comments = ArrayList()))

        binding.rvPosts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPosts.setHasFixedSize(true)
        binding.rvPosts.adapter = PostListAdapter(postList){

        }



        binding.llCreatePost.root.setOnClickListener {
            getBaseNavController().navigate(MainFragmentDirections.actionMainFragmentToCreatePostFragment())
        }

    }


    private fun getBaseNavController(): NavController {
        return (requireActivity() as MainActivity).getNavController()
    }

}