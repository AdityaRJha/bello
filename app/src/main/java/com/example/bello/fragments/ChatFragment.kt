package com.example.bello.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bello.R
import com.example.bello.databinding.FragmentChatBinding



class ChatFragment : Fragment(R.layout.fragment_chat) {
    private lateinit var chatBinding: FragmentChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        FragmentChatBinding.inflate(inflater, container, false).also { chatBinding = it }

        return chatBinding.root
    }


}