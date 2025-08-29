package com.practicestock.app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practicestock.app.R
import com.practicestock.app.databinding.ItemTradeRecordBinding
import com.practicestock.app.model.TradeRecord
import com.practicestock.app.model.TradeResult
import com.practicestock.app.utils.ImageUtils
import java.text.SimpleDateFormat
import java.util.*

class TradeRecordAdapter(
    private val onItemClick: (TradeRecord) -> Unit
) : RecyclerView.Adapter<TradeRecordAdapter.ViewHolder>() {
    
    private var records: List<TradeRecord> = emptyList()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    
    fun updateRecords(newRecords: List<TradeRecord>) {
        records = newRecords
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTradeRecordBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(records[position])
    }
    
    override fun getItemCount(): Int = records.size
    
    inner class ViewHolder(private val binding: ItemTradeRecordBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(record: TradeRecord) {
            // 设置开仓理由
            binding.reasonText.text = record.openReason.displayName
            
            // 设置结果标签
            binding.resultChip.text = record.result.displayName
            
            // 根据结果设置标签颜色
            val chipBackgroundColor = when (record.result) {
                TradeResult.PROFIT -> ContextCompat.getColor(binding.root.context, R.color.success_color)
                TradeResult.LOSS -> ContextCompat.getColor(binding.root.context, R.color.error_color)
                TradeResult.BREAKEVEN -> ContextCompat.getColor(binding.root.context, R.color.warning_color)
            }
            binding.resultChip.setChipBackgroundColorResource(android.R.color.transparent)
            binding.resultChip.chipBackgroundColor = android.content.res.ColorStateList.valueOf(chipBackgroundColor)
            
            // 设置创建时间
            binding.timeText.text = dateFormat.format(record.createdTime)
            
            // 设置图片
            if (record.imagePath != null && ImageUtils.imageFileExists(record.imagePath)) {
                binding.thumbnailImage.visibility = View.VISIBLE
                Glide.with(binding.root.context)
                    .load(record.imagePath)
                    .centerCrop()
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_image_placeholder)
                    .into(binding.thumbnailImage)
            } else {
                binding.thumbnailImage.visibility = View.GONE
            }
            
            // 设置点击事件
            binding.root.setOnClickListener {
                onItemClick(record)
            }
        }
    }
}