package com.example.bello.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeAdapter(private val activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val chatFragment = ChatFragment()
    private val statusFragment = StatusFragment()
    private val broadcastFragment = BroadcastFragment()

    override fun getItemCount(): Int{
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> chatFragment
            1 -> statusFragment
            2 -> broadcastFragment
            else -> chatFragment
        }

    }
}