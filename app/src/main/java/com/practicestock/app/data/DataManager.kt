package com.practicestock.app.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicestock.app.model.OpenReason
import com.practicestock.app.model.StatisticsData
import com.practicestock.app.model.TradeRecord
import com.practicestock.app.model.TradeResult
import com.practicestock.app.utils.ImageUtils
import java.util.*

class DataManager private constructor(context: Context) {
    
    companion object {
        private const val PREFS_NAME = "trade_records_prefs"
        private const val KEY_RECORDS = "records"
        
        @Volatile
        private var INSTANCE: DataManager? = null
        
        fun getInstance(context: Context): DataManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DataManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()
    
    fun saveRecord(record: TradeRecord) {
        val records = getAllRecords().toMutableList()
        records.add(record)
        saveRecords(records)
    }
    
    fun getAllRecords(): List<TradeRecord> {
        val json = sharedPreferences.getString(KEY_RECORDS, null) ?: return emptyList()
        val type = object : TypeToken<List<TradeRecord>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    fun getFilteredRecords(reason: OpenReason? = null, result: TradeResult? = null): List<TradeRecord> {
        return getAllRecords().filter { record ->
            (reason == null || record.openReason == reason) &&
            (result == null || record.result == result)
        }
    }
    
    fun getStatistics(): List<StatisticsData> {
        val records = getAllRecords().filter { it.openReason != null }
        if (records.isEmpty()) return emptyList()
        
        val reasonCounts = records.groupBy { it.openReason!! }
            .mapValues { it.value.size }
        
        val totalCount = records.size
        
        return reasonCounts.map { (reason, count) ->
            val percentage = (count.toFloat() / totalCount) * 100f
            StatisticsData(
                openReason = reason,
                count = count,
                percentage = percentage
            )
        }.sortedByDescending { it.count }
    }
    
    fun getStatisticsData(): List<StatisticsData> {
        return getStatistics()
    }
    
    fun deleteRecord(recordId: String) {
        val records = getAllRecords().toMutableList()
        // 找到要删除的记录，如果有图片则先删除图片文件
        val recordToDelete = records.find { it.id == recordId }
        recordToDelete?.imagePath?.let { imagePath ->
            ImageUtils.deleteImageFile(imagePath)
        }
        
        records.removeAll { it.id == recordId }
        saveRecords(records)
    }
    
    fun clearAllRecords() {
        sharedPreferences.edit().remove(KEY_RECORDS).apply()
    }
    
    private fun saveRecords(records: List<TradeRecord>) {
        val json = gson.toJson(records)
        sharedPreferences.edit().putString(KEY_RECORDS, json).apply()
    }
    
    fun generateId(): String {
        return UUID.randomUUID().toString()
    }
}