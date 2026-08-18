package com.example.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseApiResponse<T>(
    @Json(name = "success") val success: Boolean,
    @Json(name = "message") val message: String? = null,
    @Json(name = "data") val data: T? = null,
    @Json(name = "error") val error: String? = null
)

@JsonClass(generateAdapter = true)
data class ExpenseListApiResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "count") val count: Int? = 0,
    @Json(name = "totalExpense") val totalExpense: Double? = 0.0,
    @Json(name = "categoryBreakdown") val categoryBreakdown: List<CategoryBreakdownDto>? = emptyList(),
    @Json(name = "data") val data: List<ExpenseDto>? = emptyList()
)

@JsonClass(generateAdapter = true)
data class CategoryBreakdownDto(
    @Json(name = "category") val category: String,
    @Json(name = "amount") val amount: Double,
    @Json(name = "percentage") val percentage: String? = "0"
)

@JsonClass(generateAdapter = true)
data class ExpenseDto(
    @Json(name = "_id") val id: String? = null,
    @Json(name = "title") val title: String,
    @Json(name = "category") val category: String,
    @Json(name = "amount") val amount: Double,
    @Json(name = "formattedDate") val formattedDate: String? = "",
    @Json(name = "iconName") val iconName: String? = "receipt",
    @Json(name = "colorHex") val colorHex: String? = "0xFF0284C7",
    @Json(name = "paymentMode") val paymentMode: String? = "UPI",
    @Json(name = "merchantName") val merchantName: String? = "",
    @Json(name = "note") val note: String? = "",
    @Json(name = "createdAt") val createdAt: String? = null
)

@JsonClass(generateAdapter = true)
data class CreateExpenseRequest(
    @Json(name = "title") val title: String,
    @Json(name = "category") val category: String,
    @Json(name = "amount") val amount: Double,
    @Json(name = "formattedDate") val formattedDate: String,
    @Json(name = "iconName") val iconName: String = "receipt",
    @Json(name = "colorHex") val colorHex: String = "0xFF0284C7",
    @Json(name = "paymentMode") val paymentMode: String = "UPI",
    @Json(name = "note") val note: String = ""
)

@JsonClass(generateAdapter = true)
data class InvestmentListApiResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "summary") val summary: PortfolioSummaryDto? = null,
    @Json(name = "assetAllocation") val assetAllocation: List<AssetAllocationDto>? = emptyList(),
    @Json(name = "data") val data: List<InvestmentDto>? = emptyList()
)

@JsonClass(generateAdapter = true)
data class PortfolioSummaryDto(
    @Json(name = "totalInvested") val totalInvested: Double? = 0.0,
    @Json(name = "totalCurrentValue") val totalCurrentValue: Double? = 0.0,
    @Json(name = "totalReturns") val totalReturns: Double? = 0.0,
    @Json(name = "overallReturnPercentage") val overallReturnPercentage: Double? = 0.0,
    @Json(name = "totalAssetsCount") val totalAssetsCount: Int? = 0
)

@JsonClass(generateAdapter = true)
data class AssetAllocationDto(
    @Json(name = "type") val type: String,
    @Json(name = "value") val value: Double,
    @Json(name = "percentage") val percentage: String? = "0"
)

@JsonClass(generateAdapter = true)
data class InvestmentDto(
    @Json(name = "_id") val id: String? = null,
    @Json(name = "symbol") val symbol: String,
    @Json(name = "name") val name: String,
    @Json(name = "type") val type: String,
    @Json(name = "investedAmount") val investedAmount: Double,
    @Json(name = "currentValue") val currentValue: Double,
    @Json(name = "pnl") val pnl: Double? = 0.0,
    @Json(name = "pnlPercentage") val pnlPercentage: Double? = 0.0,
    @Json(name = "holdingQty") val holdingQty: Double? = 0.0,
    @Json(name = "avgPrice") val avgPrice: Double? = 0.0,
    @Json(name = "ltp") val ltp: Double? = 0.0
)

@JsonClass(generateAdapter = true)
data class CreateInvestmentRequest(
    @Json(name = "symbol") val symbol: String,
    @Json(name = "name") val name: String,
    @Json(name = "type") val type: String,
    @Json(name = "investedAmount") val investedAmount: Double,
    @Json(name = "currentValue") val currentValue: Double,
    @Json(name = "holdingQty") val holdingQty: Double,
    @Json(name = "avgPrice") val avgPrice: Double,
    @Json(name = "ltp") val ltp: Double
)

@JsonClass(generateAdapter = true)
data class GoalListApiResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "summary") val summary: GoalSummaryDto? = null,
    @Json(name = "data") val data: List<GoalDto>? = emptyList()
)

@JsonClass(generateAdapter = true)
data class GoalSummaryDto(
    @Json(name = "totalGoals") val totalGoals: Int? = 0,
    @Json(name = "totalTarget") val totalTarget: Double? = 0.0,
    @Json(name = "totalSaved") val totalSaved: Double? = 0.0,
    @Json(name = "totalMonthlySip") val totalMonthlySip: Double? = 0.0,
    @Json(name = "overallProgress") val overallProgress: String? = "0"
)

@JsonClass(generateAdapter = true)
data class GoalDto(
    @Json(name = "_id") val id: String? = null,
    @Json(name = "title") val title: String,
    @Json(name = "targetAmount") val targetAmount: Double,
    @Json(name = "currentAmount") val currentAmount: Double = 0.0,
    @Json(name = "targetDate") val targetDate: String? = "",
    @Json(name = "category") val category: String? = "General",
    @Json(name = "colorHex") val colorHex: String? = "0xFF0284C7",
    @Json(name = "monthlySip") val monthlySip: Double? = 0.0,
    @Json(name = "priority") val priority: String? = "Medium",
    @Json(name = "status") val status: String? = "ACTIVE"
)

@JsonClass(generateAdapter = true)
data class CreateGoalRequest(
    @Json(name = "title") val title: String,
    @Json(name = "targetAmount") val targetAmount: Double,
    @Json(name = "currentAmount") val currentAmount: Double = 0.0,
    @Json(name = "targetDate") val targetDate: String,
    @Json(name = "category") val category: String = "General",
    @Json(name = "colorHex") val colorHex: String = "0xFF0284C7",
    @Json(name = "monthlySip") val monthlySip: Double = 0.0,
    @Json(name = "priority") val priority: String = "Medium"
)

@JsonClass(generateAdapter = true)
data class DashboardSummaryApiResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "data") val data: DashboardSummaryDataDto? = null
)

@JsonClass(generateAdapter = true)
data class DashboardSummaryDataDto(
    @Json(name = "totalNetWorth") val totalNetWorth: Double? = 0.0,
    @Json(name = "monthlyIncome") val monthlyIncome: Double? = 0.0,
    @Json(name = "monthlyExpense") val monthlyExpense: Double? = 0.0,
    @Json(name = "netSavings") val netSavings: Double? = 0.0,
    @Json(name = "savingsRate") val savingsRate: Double? = 0.0,
    @Json(name = "financialHealthScore") val financialHealthScore: Int? = 80
)
