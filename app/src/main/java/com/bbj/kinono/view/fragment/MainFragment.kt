package com.bbj.kinono.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bbj.kinono.R
import com.bbj.kinono.view.MainActivity
import com.bbj.kinono.view.adapter.MainPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {

    private val TAG = "MAINFRAGMENT"

    init {
        Log.d(TAG,"INSTANCE MAINFRAGMENT")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val pager = view.findViewById<ViewPager2>(R.id.main_view_pager)
        pager.adapter = MainPagerAdapter(requireActivity())
        pager.isUserInputEnabled = false
        val tabLayout = view.findViewById<TabLayout>(R.id.main_tab_layout)

        TabLayoutMediator(tabLayout,pager){tab,position ->
            when (position){
                0 -> {
                    tab.text = resources.getString(R.string.home_page)
                    tab.icon = ResourcesCompat.getDrawable(resources,R.drawable.baseline_home_20,requireActivity().theme)
                }
                1 -> {
                    tab.text = resources.getString(R.string.search_page)
                    tab.icon = ResourcesCompat.getDrawable(resources,R.drawable.baseline_search_20,requireActivity().theme)
                }
                else -> error("Unknown state")
            }
        }.attach()
    }

}