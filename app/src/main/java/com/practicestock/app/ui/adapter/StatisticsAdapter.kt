package com.practicestock.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.practicestock.app.R
import com.practicestock.app.databinding.ItemStatisticsBinding
import com.practicestock.app.model.StatisticsData

class StatisticsAdapter : RecyclerView.Adapter<StatisticsAdapter.ViewHolder>() {
    
    private var statistics: List<StatisticsData> = emptyList()
    private var totalCount: Int = 0
    
    fun updateStatistics(newStatistics: List<StatisticsData>) {
        statistics = newStatistics
        totalCount = statistics.sumOf { it.count }
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStatisticsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(statistics[position], position)
    }
    
    override fun getItemCount(): Int = statistics.size
    
    inner class ViewHolder(private val binding: ItemStatisticsBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(statisticsData: StatisticsData, position: Int) {
            // 设置颜色指示器
            val predefinedColors = listOf(
                ContextCompat.getColor(binding.root.context, R.color.md_theme_light_primary),
                ContextCompat.getColor(binding.root.context, R.color.md_theme_light_secondary),
                ContextCompat.getColor(binding.root.context, R.color.md_theme_light_tertiary),
                ContextCompat.getColor(binding.root.context, R.color.success_color),
                ContextCompat.getColor(binding.root.context, R.color.warning_color),
                ContextCompat.getColor(binding.root.context, R.color.error_color)
            )
            
            val colorIndicator = predefinedColors[position % predefinedColors.size]
            binding.colorIndicator.setBackgroundColor(colorIndicator)
            
            // 设置开仓理由
            binding.reasonText.text = statisticsData.openReason.displayName
            
            // 设置数量
            binding.countText.text = statisticsData.count.toString()
            
            // 计算并设置百分比
            val percentage = if (totalCount > 0) {
                (statisticsData.count * 100.0 / totalCount)
            } else {
                0.0
            }
            binding.percentageText.text = String.format("%.1f%%", percentage)
        }
    }
}