package com.example.bello.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bello.R
import com.example.bello.databinding.FragmentBroadcastBinding


class BroadcastFragment : Fragment(R.layout.fragment_broadcast) {

    private lateinit var broadBinding: FragmentBroadcastBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        broadBinding = FragmentBroadcastBinding.inflate(inflater,container, false)

        return broadBinding.root
    }

}