package com.example.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SpendWiseApiService {

    // --- EXPENSES ---
    @GET("expenses")
    suspend fun getExpenses(
        @Query("category") category: String? = null,
        @Query("search") search: String? = null
    ): Response<ExpenseListApiResponse>

    @POST("expenses")
    suspend fun createExpense(
        @Body request: CreateExpenseRequest
    ): Response<BaseApiResponse<ExpenseDto>>

    @DELETE("expenses/{id}")
    suspend fun deleteExpense(
        @Path("id") id: String
    ): Response<BaseApiResponse<Map<String, String>>>

    // --- INVESTMENTS ---
    @GET("investments")
    suspend fun getInvestments(): Response<InvestmentListApiResponse>

    @POST("investments")
    suspend fun createInvestment(
        @Body request: CreateInvestmentRequest
    ): Response<BaseApiResponse<InvestmentDto>>

    // --- GOALS ---
    @GET("goals")
    suspend fun getGoals(): Response<GoalListApiResponse>

    @POST("goals")
    suspend fun createGoal(
        @Body request: CreateGoalRequest
    ): Response<BaseApiResponse<GoalDto>>

    // --- DASHBOARD ---
    @GET("dashboard/summary")
    suspend fun getDashboardSummary(): Response<DashboardSummaryApiResponse>
}
