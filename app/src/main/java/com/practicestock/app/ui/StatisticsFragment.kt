package com.practicestock.app.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.practicestock.app.R
import com.practicestock.app.data.DataManager
import com.practicestock.app.databinding.FragmentStatisticsBinding
import com.practicestock.app.model.OpenReason
import com.practicestock.app.model.StatisticsData
import com.practicestock.app.ui.adapter.StatisticsAdapter

class StatisticsFragment : Fragment() {
    
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var dataManager: DataManager
    private lateinit var statisticsAdapter: StatisticsAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        dataManager = DataManager.getInstance(requireContext())
        
        setupRecyclerView()
        setupPieChart()
        loadStatistics()
    }
    
    override fun onResume() {
        super.onResume()
        // 从其他页面返回时刷新统计数据
        loadStatistics()
    }
    
    private fun setupRecyclerView() {
        statisticsAdapter = StatisticsAdapter()
        binding.statisticsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = statisticsAdapter
        }
    }
    
    private fun setupPieChart() {
        binding.pieChart.apply {
            // 基本设置
            description.isEnabled = false
            isRotationEnabled = true
            isHighlightPerTapEnabled = true
            
            // 设置中心文本
            isDrawHoleEnabled = true
            setHoleColor(Color.TRANSPARENT)
            holeRadius = 40f
            transparentCircleRadius = 45f
            
            // 设置图例
            legend.isEnabled = false
            
            // 设置入场动画
            animateY(1000)
        }
    }
    
    private fun loadStatistics() {
        val statisticsData = dataManager.getStatisticsData()
        
        if (statisticsData.isEmpty()) {
            showEmptyState()
        } else {
            showStatistics(statisticsData)
        }
    }
    
    private fun showEmptyState() {
        binding.pieChart.visibility = View.GONE
        binding.statisticsRecyclerView.visibility = View.GONE
        binding.emptyLayout.visibility = View.VISIBLE
    }
    
    private fun showStatistics(statisticsData: List<StatisticsData>) {
        binding.pieChart.visibility = View.VISIBLE
        binding.statisticsRecyclerView.visibility = View.VISIBLE
        binding.emptyLayout.visibility = View.GONE
        
        setupPieChartData(statisticsData)
        statisticsAdapter.updateStatistics(statisticsData)
    }
    
    private fun setupPieChartData(statisticsData: List<StatisticsData>) {
        val entries = mutableListOf<PieEntry>()
        val colors = mutableListOf<Int>()
        
        val predefinedColors = listOf(
            ContextCompat.getColor(requireContext(), R.color.md_theme_light_primary),
            ContextCompat.getColor(requireContext(), R.color.md_theme_light_secondary),
            ContextCompat.getColor(requireContext(), R.color.md_theme_light_tertiary),
            ContextCompat.getColor(requireContext(), R.color.success_color),
            ContextCompat.getColor(requireContext(), R.color.warning_color),
            ContextCompat.getColor(requireContext(), R.color.error_color)
        )
        
        statisticsData.forEachIndexed { index, data ->
            entries.add(PieEntry(data.count.toFloat(), data.openReason.displayName))
            colors.add(predefinedColors[index % predefinedColors.size])
        }
        
        val dataSet = PieDataSet(entries, "").apply {
            this.colors = colors
            sliceSpace = 2f
            selectionShift = 8f
            valueTextSize = 12f
            valueTextColor = Color.WHITE
            valueFormatter = PercentFormatter(binding.pieChart)
        }
        
        val pieData = PieData(dataSet)
        binding.pieChart.data = pieData
        binding.pieChart.invalidate()
        
        // 设置中心文本
        val totalCount = statisticsData.sumOf { it.count }
        binding.pieChart.centerText = "总计\n$totalCount 条记录"
        binding.pieChart.setCenterTextSize(14f)
        binding.pieChart.setCenterTextColor(ContextCompat.getColor(requireContext(), R.color.md_theme_light_onSurface))
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}