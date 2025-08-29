package com.practicestock.app.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.practicestock.app.R
import com.practicestock.app.data.DataManager
import com.practicestock.app.databinding.ActivityTradeDetailBinding
import com.practicestock.app.model.TradeRecord
import com.practicestock.app.model.TradeResult
import com.practicestock.app.utils.ImageUtils
import java.text.SimpleDateFormat
import java.util.*

class TradeDetailActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityTradeDetailBinding
    private lateinit var dataManager: DataManager
    private var tradeRecord: TradeRecord? = null
    
    private val dateFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault())
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTradeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        dataManager = DataManager.getInstance(this)
        
        setupToolbar()
        loadTradeRecord()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getString(R.string.trade_detail)
        }
        
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    
    private fun loadTradeRecord() {
        val recordId = intent.getStringExtra("record_id")
        if (recordId == null) {
            finish()
            return
        }
        
        tradeRecord = dataManager.getAllRecords().find { it.id == recordId }
        if (tradeRecord == null) {
            finish()
            return
        }
        
        displayTradeRecord(tradeRecord!!)
    }
    
    private fun displayTradeRecord(record: TradeRecord) {
        // 显示图片
        if (record.imagePath != null && ImageUtils.imageFileExists(record.imagePath)) {
            binding.tradeImage.visibility = View.VISIBLE
            binding.noImageText.visibility = View.GONE
            
            Glide.with(this)
                .load(record.imagePath)
                .centerCrop()
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_placeholder)
                .into(binding.tradeImage)
                
            // 点击图片可以全屏查看
            binding.tradeImage.setOnClickListener {
                // TODO: 实现全屏图片查看功能
            }
        } else {
            binding.tradeImage.visibility = View.GONE
            binding.noImageText.visibility = View.VISIBLE
        }
        
        // 显示开仓理由
        binding.reasonText.text = record.openReason.displayName
        
        // 显示结果标签
        binding.resultChip.text = record.result.displayName
        
        // 根据结果设置标签颜色
        val chipBackgroundColor = when (record.result) {
            TradeResult.PROFIT -> ContextCompat.getColor(this, R.color.success_color)
            TradeResult.LOSS -> ContextCompat.getColor(this, R.color.error_color)
            TradeResult.BREAKEVEN -> ContextCompat.getColor(this, R.color.warning_color)
        }
        binding.resultChip.setChipBackgroundColorResource(android.R.color.transparent)
        binding.resultChip.chipBackgroundColor = android.content.res.ColorStateList.valueOf(chipBackgroundColor)
        
        // 显示创建时间
        binding.timeText.text = dateFormat.format(record.createdTime)
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}