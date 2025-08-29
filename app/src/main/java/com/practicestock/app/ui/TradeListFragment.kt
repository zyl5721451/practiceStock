package com.practicestock.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicestock.app.R
import com.practicestock.app.data.DataManager
import com.practicestock.app.databinding.FragmentTradeListBinding
import com.practicestock.app.model.OpenReason
import com.practicestock.app.model.TradeRecord
import com.practicestock.app.model.TradeResult
import com.practicestock.app.ui.adapter.TradeRecordAdapter

class TradeListFragment : Fragment() {
    
    private var _binding: FragmentTradeListBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var dataManager: DataManager
    private lateinit var tradeRecordAdapter: TradeRecordAdapter
    private var allRecords: List<TradeRecord> = emptyList()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTradeListBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        dataManager = DataManager.getInstance(requireContext())
        
        setupRecyclerView()
        setupFilters()
        loadData()
    }
    
    override fun onResume() {
        super.onResume()
        // 从详情页面返回时刷新数据
        loadData()
    }
    
    private fun setupRecyclerView() {
        tradeRecordAdapter = TradeRecordAdapter { record ->
            // 点击记录跳转到详情页面
            val intent = Intent(requireContext(), TradeDetailActivity::class.java)
            intent.putExtra("record_id", record.id)
            startActivity(intent)
        }
        
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tradeRecordAdapter
        }
    }
    
    private fun setupFilters() {
        // 开仓理由筛选
        val reasonOptions = mutableListOf(getString(R.string.all_reasons))
        reasonOptions.addAll(OpenReason.values().map { it.displayName })
        val reasonAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, reasonOptions)
        binding.reasonFilterSpinner.setAdapter(reasonAdapter)
        binding.reasonFilterSpinner.setText(reasonOptions[0], false)
        
        // 结果筛选
        val resultOptions = mutableListOf(getString(R.string.all_results))
        resultOptions.addAll(TradeResult.values().map { it.displayName })
        val resultAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, resultOptions)
        binding.resultFilterSpinner.setAdapter(resultAdapter)
        binding.resultFilterSpinner.setText(resultOptions[0], false)
        
        // 设置筛选监听器
        binding.reasonFilterSpinner.setOnItemClickListener { _, _, _, _ ->
            applyFilters()
        }
        
        binding.resultFilterSpinner.setOnItemClickListener { _, _, _, _ ->
            applyFilters()
        }
    }
    
    private fun loadData() {
        allRecords = dataManager.getAllRecords().sortedByDescending { it.createdTime }
        applyFilters()
    }
    
    private fun applyFilters() {
        val reasonFilter = binding.reasonFilterSpinner.text.toString()
        val resultFilter = binding.resultFilterSpinner.text.toString()
        
        var filteredRecords = allRecords
        
        // 按开仓理由筛选
        if (reasonFilter != getString(R.string.all_reasons)) {
            val selectedReason = OpenReason.fromDisplayName(reasonFilter)
            filteredRecords = filteredRecords.filter { it.openReason == selectedReason }
        }
        
        // 按结果筛选
        if (resultFilter != getString(R.string.all_results)) {
            val selectedResult = TradeResult.fromDisplayName(resultFilter)
            filteredRecords = filteredRecords.filter { it.result == selectedResult }
        }
        
        updateUI(filteredRecords)
    }
    
    private fun updateUI(records: List<TradeRecord>) {
        if (records.isEmpty()) {
            binding.recyclerView.visibility = View.GONE
            binding.emptyLayout.visibility = View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.emptyLayout.visibility = View.GONE
            tradeRecordAdapter.updateRecords(records)
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}