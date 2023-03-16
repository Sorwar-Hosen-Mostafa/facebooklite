package com.example.facebooklite.ui.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.facebooklite.R
import com.example.facebooklite.databinding.FragmentCreatePostBinding


class CreatePostFragment : Fragment() {

    companion object{
         const val MAX_POST_LENGTH = 500
    }
    private lateinit var binding: FragmentCreatePostBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_post,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.llCreatePost.postBodyEdittext.apply {

            filters = arrayOf(InputFilter.LengthFilter(MAX_POST_LENGTH))

            addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    s?.let {
                        binding.llCreatePost.postLength.text = "${it.length}/$MAX_POST_LENGTH"
                    }?:  binding.llCreatePost.postLength.setText("0/$MAX_POST_LENGTH")
                }

                override fun afterTextChanged(s: Editable?) {
                }

            })
        }
    }

}