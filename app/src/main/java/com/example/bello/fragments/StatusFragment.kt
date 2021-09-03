package com.example.bello.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bello.R
import com.example.bello.databinding.FragmentStatusBinding

class StatusFragment : Fragment(R.layout.fragment_status) {
    private lateinit var statusBinding: FragmentStatusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FragmentStatusBinding.inflate(inflater, container, false).also { statusBinding = it }

        return statusBinding.root
    }



}