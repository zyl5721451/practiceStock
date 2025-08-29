package com.practicestock.app.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TradingPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    
    override fun getItemCount(): Int = 2
    
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TradeListFragment()
            1 -> StatisticsFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}