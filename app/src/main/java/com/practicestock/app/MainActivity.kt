package com.practicestock.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicestock.app.databinding.ActivityMainBinding
import com.practicestock.app.ui.OpenPositionFragment
import com.practicestock.app.ui.TradingFragment

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupViewPager()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }
    
    private fun setupViewPager() {
        val adapter = MainPagerAdapter(this)
        binding.viewPager.adapter = adapter
        
        // 禁用滑动切换，只能通过底部导航点击切换
        binding.viewPager.isUserInputEnabled = false
        
        // 设置底部导航
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_open_position -> {
                    binding.viewPager.currentItem = 0
                    true
                }
                R.id.nav_trading -> {
                    binding.viewPager.currentItem = 1
                    true
                }
                else -> false
            }
        }
        
        // 设置ViewPager页面变化监听
        binding.viewPager.registerOnPageChangeCallback(object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> binding.bottomNavigation.selectedItemId = R.id.nav_open_position
                    1 -> binding.bottomNavigation.selectedItemId = R.id.nav_trading
                }
            }
        })
        
        // 默认选中第一个tab
        binding.bottomNavigation.selectedItemId = R.id.nav_open_position
    }
    
    private class MainPagerAdapter(fragmentActivity: FragmentActivity) : 
        FragmentStateAdapter(fragmentActivity) {
        
        override fun getItemCount(): Int = 2
        
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> OpenPositionFragment()
                1 -> TradingFragment()
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }
    }
}