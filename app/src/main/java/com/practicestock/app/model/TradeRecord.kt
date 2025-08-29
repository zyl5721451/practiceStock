package com.practicestock.app.model

import java.util.Date

data class TradeRecord(
    val id: String,
    val openReason: OpenReason,
    val result: TradeResult,
    val imagePath: String?,
    val createdTime: Date
)

enum class OpenReason(val displayName: String) {
    TECHNICAL_ANALYSIS("技术分析"),
    FUNDAMENTAL_ANALYSIS("基本面分析"),
    NEWS_EVENT("消息面"),
    MARKET_SENTIMENT("市场情绪"),
    FOLLOW_TREND("跟随趋势"),
    OTHER("其他");
    
    companion object {
        fun fromDisplayName(displayName: String): OpenReason? {
            return values().find { it.displayName == displayName }
        }
    }
}

enum class TradeResult(val displayName: String) {
    PROFIT("盈利"),
    LOSS("亏损"),
    BREAKEVEN("保本");
    
    companion object {
        fun fromDisplayName(displayName: String): TradeResult? {
            return values().find { it.displayName == displayName }
        }
    }
}

data class StatisticsData(
    val reason: OpenReason,
    val count: Int,
    val percentage: Float
)