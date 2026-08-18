package com.example.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object Expenses : Screen("expenses")
    object AddExpense : Screen("add_expense")
    object Investments : Screen("investments")
    object InvestmentOverview : Screen("investment_overview")
    object InvestmentTransactions : Screen("investment_transactions")
    object AddInvestmentSelect : Screen("add_investment_select")
    object AddStockInvestment : Screen("add_stock_investment")
    object FinancialGoals : Screen("financial_goals")
    object Profile : Screen("profile")
}
