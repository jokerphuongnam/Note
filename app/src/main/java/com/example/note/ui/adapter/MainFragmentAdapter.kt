package com.example.note.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainFragmentAdapter(fragmentActivity: FragmentActivity,private val fragments: MutableList<Fragment> = mutableListOf()) :
    FragmentStateAdapter(fragmentActivity) {

    fun addFragment(fragment: Fragment){
        fragments.add(fragment)
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}