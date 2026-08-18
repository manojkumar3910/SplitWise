package com.example.data

data class Expense(
    val id: String,
    val title: String,
    val category: String,
    val subcategory: String = "",
    val amount: Double,
    val paymentMethod: String,
    val date: String,
    val time: String = "10:30 PM",
    val description: String = "",
    val needOrWant: String = "Need",
    val isRecurring: Boolean = false
)

data class Goal(
    val id: String,
    val title: String,
    val category: String,
    val savedAmount: Double,
    val targetAmount: Double,
    val isAtRisk: Boolean = false,
    val monthlyContribution: Double = 0.0,
    val deadline: String = "Dec 2026",
    val unit: String = "₹",
    val isGram: Boolean = false,
    val currentGrams: Double = 0.0,
    val targetGrams: Double = 0.0,
    val status: String = "Active" // Active, Completed, Paused
) {
    val progressPercent: Float
        get() = if (isGram) {
            if (targetGrams > 0) ((currentGrams / targetGrams) * 100).toFloat().coerceIn(0f, 100f) else 0f
        } else {
            if (targetAmount > 0) ((savedAmount / targetAmount) * 100).toFloat().coerceIn(0f, 100f) else 0f
        }
}

enum class TransactionType {
    BUY, SELL, SIP
}

enum class AssetClass(val displayName: String) {
    STOCKS("Stocks"),
    MUTUAL_FUNDS("Mutual Funds"),
    GOLD("Gold"),
    SILVER("Silver")
}

data class InvestmentTransaction(
    val id: String,
    val name: String,
    val symbol: String,
    val type: TransactionType,
    val quantity: Double,
    val pricePerShare: Double,
    val totalAmount: Double,
    val date: String,
    val assetClass: AssetClass,
    val exchangeOrVault: String = "NSE",
    val details: String = "",
    val notes: String = "",
    val monthYear: String = "August 2023"
)

data class Budget(
    val category: String,
    val spentAmount: Double,
    val limitAmount: Double,
    val iconName: String = "restaurant"
) {
    val progress: Float
        get() = if (limitAmount > 0) (spentAmount / limitAmount).toFloat().coerceIn(0f, 1f) else 0f
}

data class InvestmentAssetHolding(
    val assetClass: AssetClass,
    val name: String,
    val tag: String,
    val totalValue: Double,
    val gainPercent: Double,
    val isPositive: Boolean = true,
    val iconType: String = "trending_up"
)

data class PortfolioSummary(
    val totalValue: Double = 278500.0,
    val totalInvested: Double = 250000.0,
    val profitAmount: Double = 28500.0,
    val profitPercent: Double = 11.4,
    val todayChangeAmount: Double = 1250.0,
    val todayChangePercent: Double = 0.45,
    val stocksValue: Double = 133680.0,
    val stocksPercent: Int = 48,
    val mutualFundsValue: Double = 94690.0,
    val mutualFundsPercent: Int = 34,
    val goldValue: Double = 27850.0,
    val goldPercent: Int = 10,
    val silverValue: Double = 22280.0,
    val silverPercent: Int = 8
)
