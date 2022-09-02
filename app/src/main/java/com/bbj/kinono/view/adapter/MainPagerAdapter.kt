package com.bbj.kinono.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bbj.kinono.view.fragment.HomeFragment
import com.bbj.kinono.view.fragment.SearchFragment

class MainPagerAdapter(fragmentActivity : FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> HomeFragment()
            1 -> SearchFragment()
            else -> error("Unknown state")
        }
    }
}