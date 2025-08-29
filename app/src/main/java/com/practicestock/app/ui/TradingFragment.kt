package com.practicestock.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.practicestock.app.R
import com.practicestock.app.databinding.FragmentTradingBinding

class TradingFragment : Fragment() {
    
    private var _binding: FragmentTradingBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var tradingPagerAdapter: TradingPagerAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTradingBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViewPager()
    }
    
    private fun setupViewPager() {
        tradingPagerAdapter = TradingPagerAdapter(this)
        binding.viewPager.adapter = tradingPagerAdapter
        
        // 连接TabLayout和ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.trade_list)
                1 -> getString(R.string.statistics)
                else -> ""
            }
        }.attach()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}