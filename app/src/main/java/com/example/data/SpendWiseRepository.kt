package com.example.data

import android.util.Log
import com.example.data.api.ApiClient
import com.example.data.api.CreateExpenseRequest
import com.example.data.api.CreateGoalRequest
import com.example.data.api.CreateInvestmentRequest
import com.example.data.api.ExpenseDto
import com.example.data.api.GoalDto
import com.example.data.api.InvestmentDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale
import java.util.UUID

class SpendWiseRepository(
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
) {

    private val TAG = "SpendWiseRepository"

    private val _expenses = MutableStateFlow<List<Expense>>(seedFallbackExpenses())
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    private val _goals = MutableStateFlow<List<Goal>>(seedFallbackGoals())
    val goals: StateFlow<List<Goal>> = _goals.asStateFlow()

    private val _investmentTransactions = MutableStateFlow<List<InvestmentTransaction>>(seedFallbackInvestments())
    val investmentTransactions: StateFlow<List<InvestmentTransaction>> = _investmentTransactions.asStateFlow()

    private val _portfolioSummary = MutableStateFlow(PortfolioSummary())
    val portfolioSummary: StateFlow<PortfolioSummary> = _portfolioSummary.asStateFlow()

    private val _budgets = MutableStateFlow<List<Budget>>(defaultBudgets())
    val budgets: StateFlow<List<Budget>> = _budgets.asStateFlow()

    private val _userEmail = MutableStateFlow("devamanoj07@gmail.com")
    val userEmail: StateFlow<String> = _userEmail.asStateFlow()

    private val _userBalance = MutableStateFlow(50000.0)
    val userBalance: StateFlow<Double> = _userBalance.asStateFlow()

    private val _userIncome = MutableStateFlow(95000.0)
    val userIncome: StateFlow<Double> = _userIncome.asStateFlow()

    // Real-time backend connection status
    private val _isApiConnected = MutableStateFlow(false)
    val isApiConnected: StateFlow<Boolean> = _isApiConnected.asStateFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    private val _apiStatusMessage = MutableStateFlow("Ready (Auto-sync with MongoDB on start)")
    val apiStatusMessage: StateFlow<String> = _apiStatusMessage.asStateFlow()

    init {
        // Initial fetch from live MongoDB Atlas backend
        refreshAllData()
    }

    val totalExpensesToday: Double
        get() = _expenses.value.sumOf { it.amount }

    fun refreshAllData() {
        scope.launch {
            _isSyncing.value = true
            _apiStatusMessage.value = "Fetching live data from MongoDB Atlas..."

            val expSuccess = fetchExpensesInternal()
            val invSuccess = fetchInvestmentsInternal()
            val goalSuccess = fetchGoalsInternal()
            fetchDashboardSummaryInternal()

            val connected = expSuccess || invSuccess || goalSuccess
            _isApiConnected.value = connected
            _isSyncing.value = false
            _apiStatusMessage.value = if (connected) {
                "Synced with MongoDB Atlas (${_expenses.value.size} expenses, ${_goals.value.size} goals)"
            } else {
                "MongoDB Atlas Offline/Local Mode"
            }
        }
    }

    private suspend fun fetchExpensesInternal(): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.service.getExpenses()
            if (response.isSuccessful && response.body() != null) {
                val dtoList = response.body()?.data ?: emptyList()
                if (dtoList.isNotEmpty()) {
                    _expenses.value = dtoList.map { it.toDomain() }
                } else if (_expenses.value.isEmpty()) {
                    _expenses.value = seedFallbackExpenses()
                }
                return@withContext true
            } else {
                Log.w(TAG, "Failed to fetch expenses: ${response.code()}")
                if (_expenses.value.isEmpty()) _expenses.value = seedFallbackExpenses()
                return@withContext false
            }
        } catch (e: Exception) {
            Log.d(TAG, "Expense network fetch status: ${e.localizedMessage}")
            if (_expenses.value.isEmpty()) _expenses.value = seedFallbackExpenses()
            return@withContext false
        }
    }

    private suspend fun fetchInvestmentsInternal(): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.service.getInvestments()
            if (response.isSuccessful && response.body() != null) {
                val res = response.body()!!
                val dtoList = res.data ?: emptyList()
                if (dtoList.isNotEmpty()) {
                    _investmentTransactions.value = dtoList.map { it.toTransactionDomain() }
                    res.summary?.let { summaryDto ->
                        val totalVal = summaryDto.totalCurrentValue ?: 0.0
                        val totalInv = summaryDto.totalInvested ?: 0.0
                        val profit = summaryDto.totalReturns ?: (totalVal - totalInv)
                        val profitPct = summaryDto.overallReturnPercentage ?: 0.0

                        _portfolioSummary.value = _portfolioSummary.value.copy(
                            totalValue = totalVal,
                            totalInvested = totalInv,
                            profitAmount = profit,
                            profitPercent = profitPct
                        )
                    }
                } else if (_investmentTransactions.value.isEmpty()) {
                    _investmentTransactions.value = seedFallbackInvestments()
                }
                return@withContext true
            } else {
                if (_investmentTransactions.value.isEmpty()) _investmentTransactions.value = seedFallbackInvestments()
                return@withContext false
            }
        } catch (e: Exception) {
            Log.d(TAG, "Investment network fetch status: ${e.localizedMessage}")
            if (_investmentTransactions.value.isEmpty()) _investmentTransactions.value = seedFallbackInvestments()
            return@withContext false
        }
    }

    private suspend fun fetchGoalsInternal(): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.service.getGoals()
            if (response.isSuccessful && response.body() != null) {
                val dtoList = response.body()?.data ?: emptyList()
                if (dtoList.isNotEmpty()) {
                    _goals.value = dtoList.map { it.toDomain() }
                } else if (_goals.value.isEmpty()) {
                    _goals.value = seedFallbackGoals()
                }
                return@withContext true
            } else {
                if (_goals.value.isEmpty()) _goals.value = seedFallbackGoals()
                return@withContext false
            }
        } catch (e: Exception) {
            Log.d(TAG, "Goals network fetch status: ${e.localizedMessage}")
            if (_goals.value.isEmpty()) _goals.value = seedFallbackGoals()
            return@withContext false
        }
    }

    private suspend fun fetchDashboardSummaryInternal() = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.service.getDashboardSummary()
            if (response.isSuccessful && response.body()?.data != null) {
                val data = response.body()!!.data!!
                data.monthlyIncome?.let { _userIncome.value = it }
                data.totalNetWorth?.let { _userBalance.value = it }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Dashboard summary fetch status: ${e.localizedMessage}")
        }
    }

    fun addExpense(
        title: String,
        category: String,
        subcategory: String,
        amount: Double,
        paymentMethod: String,
        date: String,
        time: String,
        description: String,
        needOrWant: String,
        isRecurring: Boolean
    ) {
        val newExpense = Expense(
            id = UUID.randomUUID().toString(),
            title = if (title.isBlank()) category else title,
            category = category,
            subcategory = subcategory,
            amount = amount,
            paymentMethod = paymentMethod,
            date = date,
            time = time,
            description = description,
            needOrWant = needOrWant,
            isRecurring = isRecurring
        )
        // Immediate optimistic UI update
        _expenses.value = listOf(newExpense) + _expenses.value

        // Post to backend API
        scope.launch {
            try {
                val request = CreateExpenseRequest(
                    title = newExpense.title,
                    category = newExpense.category,
                    amount = newExpense.amount,
                    formattedDate = "$date, $time",
                    paymentMode = newExpense.paymentMethod,
                    note = description
                )
                val response = ApiClient.service.createExpense(request)
                if (response.isSuccessful && response.body()?.data != null) {
                    val serverExpense = response.body()!!.data!!.toDomain()
                    // Replace temporary ID with server ID
                    _expenses.value = _expenses.value.map { if (it.id == newExpense.id) serverExpense else it }
                    _isApiConnected.value = true
                    _apiStatusMessage.value = "Saved expense to MongoDB Atlas"
                }
            } catch (e: Exception) {
                Log.w(TAG, "Failed to post expense to API: ${e.localizedMessage}")
            }
        }
    }

    fun deleteExpense(expenseId: String) {
        _expenses.value = _expenses.value.filterNot { it.id == expenseId }
        scope.launch {
            try {
                ApiClient.service.deleteExpense(expenseId)
            } catch (e: Exception) {
                Log.w(TAG, "Failed to delete expense from API: ${e.localizedMessage}")
            }
        }
    }

    fun addStockInvestment(
        symbol: String,
        name: String,
        type: TransactionType,
        quantity: Double,
        pricePerShare: Double,
        date: String,
        charges: Double = 0.0,
        notes: String = "",
        assetClass: AssetClass = AssetClass.STOCKS
    ) {
        val total = (quantity * pricePerShare) + charges
        val newTx = InvestmentTransaction(
            id = UUID.randomUUID().toString(),
            name = if (name.isNotBlank()) name else symbol,
            symbol = symbol,
            type = type,
            quantity = quantity,
            pricePerShare = pricePerShare,
            totalAmount = total,
            date = date,
            assetClass = assetClass,
            exchangeOrVault = if (assetClass == AssetClass.STOCKS) "NSE" else if (assetClass == AssetClass.GOLD) "Vault" else "Direct",
            details = "${quantity.toInt()} shares @ ₹${formatCurrencyPlain(pricePerShare)}",
            notes = notes,
            monthYear = "August 2026"
        )
        _investmentTransactions.value = listOf(newTx) + _investmentTransactions.value

        val curSummary = _portfolioSummary.value
        _portfolioSummary.value = curSummary.copy(
            totalValue = curSummary.totalValue + total,
            totalInvested = curSummary.totalInvested + total,
            stocksValue = if (assetClass == AssetClass.STOCKS) curSummary.stocksValue + total else curSummary.stocksValue
        )

        // Post to backend API
        scope.launch {
            try {
                val req = CreateInvestmentRequest(
                    symbol = symbol,
                    name = name.ifBlank { symbol },
                    type = if (assetClass == AssetClass.STOCKS) "STOCK" else if (assetClass == AssetClass.GOLD) "GOLD" else "MUTUAL_FUND",
                    investedAmount = total,
                    currentValue = total,
                    holdingQty = quantity,
                    avgPrice = pricePerShare,
                    ltp = pricePerShare
                )
                ApiClient.service.createInvestment(req)
                _isApiConnected.value = true
            } catch (e: Exception) {
                Log.w(TAG, "Failed to post investment to API: ${e.localizedMessage}")
            }
        }
    }

    fun addGoal(
        title: String,
        category: String,
        targetAmount: Double,
        monthlyContribution: Double,
        deadline: String
    ) {
        val newGoal = Goal(
            id = UUID.randomUUID().toString(),
            title = title,
            category = category,
            savedAmount = 0.0,
            targetAmount = targetAmount,
            isAtRisk = false,
            monthlyContribution = monthlyContribution,
            deadline = deadline
        )
        _goals.value = _goals.value + newGoal

        // Post to backend API
        scope.launch {
            try {
                val req = CreateGoalRequest(
                    title = title,
                    targetAmount = targetAmount,
                    currentAmount = 0.0,
                    targetDate = deadline,
                    category = category,
                    monthlySip = monthlyContribution
                )
                ApiClient.service.createGoal(req)
                _isApiConnected.value = true
            } catch (e: Exception) {
                Log.w(TAG, "Failed to post goal to API: ${e.localizedMessage}")
            }
        }
    }

    // Mapping DTO helpers
    private fun ExpenseDto.toDomain(): Expense {
        return Expense(
            id = this.id ?: UUID.randomUUID().toString(),
            title = this.title,
            category = this.category,
            subcategory = this.merchantName ?: "",
            amount = this.amount,
            paymentMethod = this.paymentMode ?: "UPI",
            date = this.formattedDate ?: "Today",
            time = "Auto",
            description = this.note ?: "",
            needOrWant = "Need",
            isRecurring = false
        )
    }

    private fun InvestmentDto.toTransactionDomain(): InvestmentTransaction {
        val assetCls = when (this.type.uppercase()) {
            "STOCK", "EQUITY" -> AssetClass.STOCKS
            "MUTUAL_FUND", "MF" -> AssetClass.MUTUAL_FUNDS
            "GOLD" -> AssetClass.GOLD
            "SILVER" -> AssetClass.SILVER
            else -> AssetClass.STOCKS
        }
        return InvestmentTransaction(
            id = this.id ?: UUID.randomUUID().toString(),
            name = this.name,
            symbol = this.symbol,
            type = TransactionType.BUY,
            quantity = this.holdingQty ?: 1.0,
            pricePerShare = this.avgPrice ?: 0.0,
            totalAmount = this.currentValue,
            date = "Live Holdings",
            assetClass = assetCls,
            exchangeOrVault = if (assetCls == AssetClass.STOCKS) "NSE" else "Direct",
            details = "${(this.holdingQty ?: 1.0).toInt()} units @ ₹${formatCurrencyPlain(this.avgPrice ?: 0.0)}",
            notes = "PnL: ₹${formatCurrencyPlain(this.pnl ?: 0.0)} (${this.pnlPercentage ?: 0.0}%)",
            monthYear = "August 2026"
        )
    }

    private fun GoalDto.toDomain(): Goal {
        return Goal(
            id = this.id ?: UUID.randomUUID().toString(),
            title = this.title,
            category = this.category ?: "General",
            savedAmount = this.currentAmount,
            targetAmount = this.targetAmount,
            isAtRisk = false,
            monthlyContribution = this.monthlySip ?: 0.0,
            deadline = this.targetDate ?: "2026",
            status = this.status ?: "Active"
        )
    }

    companion object {
        val instance by lazy { SpendWiseRepository() }

        fun formatCurrency(amount: Double): String {
            val format = NumberFormat.getNumberInstance(Locale("en", "IN"))
            return "₹" + format.format(amount.toLong())
        }

        fun formatCurrencyPlain(amount: Double): String {
            val format = NumberFormat.getNumberInstance(Locale("en", "IN"))
            return format.format(amount.toLong())
        }

        private fun defaultBudgets(): List<Budget> = listOf(
            Budget(category = "Food & Dining", spentAmount = 8450.0, limitAmount = 15000.0, iconName = "restaurant"),
            Budget(category = "Shopping", spentAmount = 8210.0, limitAmount = 12000.0, iconName = "shopping_bag"),
            Budget(category = "Transport", spentAmount = 4500.0, limitAmount = 6000.0, iconName = "directions_car"),
            Budget(category = "Bills & Utilities", spentAmount = 2199.0, limitAmount = 5000.0, iconName = "receipt_long")
        )

        private fun seedFallbackExpenses(): List<Expense> = listOf(
            Expense("e1", "Bistro Gourmet & Drinks", "Food & Dining", "Restaurant", 3450.0, "Credit Card", "Aug 18", "2:30 PM", "Weekend lunch"),
            Expense("e2", "Highland Supermarket", "Shopping", "Groceries", 8210.0, "UPI", "Aug 17", "6:15 PM", "Monthly groceries"),
            Expense("e3", "Shell Fuel Station", "Transport", "Fuel", 4500.0, "Debit Card", "Aug 15", "9:00 AM", "Full tank petrol"),
            Expense("e4", "Fiber Internet & OTT Bundle", "Bills & Utilities", "Utilities", 2199.0, "Auto-Pay", "Aug 12", "11:20 AM", "High-speed broadband"),
            Expense("e5", "Apple One Subscription", "Entertainment", "Cloud", 365.0, "Credit Card", "Aug 05", "4:00 PM", "Music & cloud")
        )

        private fun seedFallbackGoals(): List<Goal> = listOf(
            Goal("g1", "Emergency Rainy Day Fund", "Emergency", 95000.0, 150000.0, false, 10000.0, "Dec 2025"),
            Goal("g2", "Tokyo Vacation 2026", "Travel", 112000.0, 250000.0, false, 15000.0, "May 2026"),
            Goal("g3", "Electric Vehicle Down Payment", "Automobile", 180000.0, 400000.0, false, 20000.0, "Oct 2026")
        )

        private fun seedFallbackInvestments(): List<InvestmentTransaction> = listOf(
            InvestmentTransaction("i1", "Reliance Industries Ltd.", "RELIANCE", TransactionType.BUY, 22.0, 2363.63, 64800.0, "Live Holdings", AssetClass.STOCKS, "NSE", "22 shares @ ₹2,363"),
            InvestmentTransaction("i2", "Tata Consultancy Services", "TCS", TransactionType.BUY, 14.0, 3428.57, 56400.0, "Live Holdings", AssetClass.STOCKS, "NSE", "14 shares @ ₹3,428"),
            InvestmentTransaction("i3", "Mirae Asset Nifty 50 Index Fund", "NIFTY50", TransactionType.SIP, 450.0, 166.67, 92300.0, "Live Holdings", AssetClass.MUTUAL_FUNDS, "Direct", "450 units @ ₹166.67"),
            InvestmentTransaction("i4", "Sovereign Gold Bond Series VI", "SGB2028", TransactionType.BUY, 7.0, 5000.0, 44200.0, "Live Holdings", AssetClass.GOLD, "Vault", "7 grams @ ₹5,000/g")
        )
    }
}
