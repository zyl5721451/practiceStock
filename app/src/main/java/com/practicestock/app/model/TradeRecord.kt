package com.practicestock.app.model

import java.util.Date

data class TradeRecord(
    val id: String,
    val openReason: OpenReason?,
    val result: TradeResult?,
    val imagePath: String?,
    val createdTime: Date
)

enum class OpenReason(val displayName: String) {
    VOLUME_BREAKTHROUGH("两次成交量突破"),
    TREND_BREAKTHROUGH("趋势中横盘突破"),
    CALLBACK_BREAKTHROUGH("回调后突破"),
    TREND_SUPPORT("趋势中遇到了支撑");
    
    companion object {
        fun fromDisplayName(displayName: String): OpenReason? {
            return values().find { it.displayName == displayName }
        }
    }
}

enum class TradeResult(val displayName: String) {
    PROFIT("盈利"),
    LOSS("亏损");
    
    companion object {
        fun fromDisplayName(displayName: String): TradeResult? {
            return values().find { it.displayName == displayName }
        }
    }
}

data class StatisticsData(
    val openReason: OpenReason,
    val count: Int,
    val percentage: Float
)