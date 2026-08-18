package com.example.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.NumberFormat
import java.util.Locale
import java.util.UUID

class SpendWiseRepository {

    private val _expenses = MutableStateFlow<List<Expense>>(initialExpenses())
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    private val _goals = MutableStateFlow<List<Goal>>(initialGoals())
    val goals: StateFlow<List<Goal>> = _goals.asStateFlow()

    private val _investmentTransactions = MutableStateFlow<List<InvestmentTransaction>>(initialInvestmentTransactions())
    val investmentTransactions: StateFlow<List<InvestmentTransaction>> = _investmentTransactions.asStateFlow()

    private val _portfolioSummary = MutableStateFlow(PortfolioSummary())
    val portfolioSummary: StateFlow<PortfolioSummary> = _portfolioSummary.asStateFlow()

    private val _budgets = MutableStateFlow<List<Budget>>(initialBudgets())
    val budgets: StateFlow<List<Budget>> = _budgets.asStateFlow()

    private val _userEmail = MutableStateFlow("devamanoj07@gmail.com")
    val userEmail: StateFlow<String> = _userEmail.asStateFlow()

    private val _userBalance = MutableStateFlow(6000.0)
    val userBalance: StateFlow<Double> = _userBalance.asStateFlow()

    private val _userIncome = MutableStateFlow(40000.0)
    val userIncome: StateFlow<Double> = _userIncome.asStateFlow()

    val totalExpensesToday: Double
        get() = _expenses.value.filter { it.date == "Aug 17" || it.date == "17 Aug 2026" }.sumOf { it.amount }

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
        _expenses.value = listOf(newExpense) + _expenses.value
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
        
        // Update portfolio value
        val curSummary = _portfolioSummary.value
        _portfolioSummary.value = curSummary.copy(
            totalValue = curSummary.totalValue + total,
            totalInvested = curSummary.totalInvested + total,
            stocksValue = if (assetClass == AssetClass.STOCKS) curSummary.stocksValue + total else curSummary.stocksValue
        )
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

        private fun initialExpenses(): List<Expense> = listOf(
            Expense(
                id = "1",
                title = "Lunch",
                category = "Food",
                subcategory = "Restaurant",
                amount = 180.0,
                paymentMethod = "Credit Card",
                date = "Aug 17",
                time = "1:15 PM"
            ),
            Expense(
                id = "2",
                title = "Tea",
                category = "Food",
                subcategory = "Snacks",
                amount = 30.0,
                paymentMethod = "Cash",
                date = "Aug 17",
                time = "4:30 PM"
            ),
            Expense(
                id = "3",
                title = "Bus",
                category = "Transport",
                subcategory = "Transit",
                amount = 40.0,
                paymentMethod = "UPI",
                date = "Aug 17",
                time = "6:00 PM"
            ),
            Expense(
                id = "4",
                title = "Shopping",
                category = "Shopping",
                subcategory = "Retail",
                amount = 500.0,
                paymentMethod = "Debit Card",
                date = "Aug 17",
                time = "8:30 PM"
            ),
            Expense(
                id = "5",
                title = "Electricity Bill",
                category = "Bills",
                subcategory = "Utilities",
                amount = 1200.0,
                paymentMethod = "Auto-Pay",
                date = "Aug 16",
                time = "10:00 AM"
            )
        )

        private fun initialGoals(): List<Goal> = listOf(
            Goal(
                id = "g1",
                title = "Emergency Fund",
                category = "Emergency Fund",
                savedAmount = 85000.0,
                targetAmount = 200000.0,
                isAtRisk = false,
                monthlyContribution = 20000.0,
                deadline = "Dec 2026"
            ),
            Goal(
                id = "g2",
                title = "New Phone",
                category = "Electronics",
                savedAmount = 32000.0,
                targetAmount = 50000.0,
                isAtRisk = false,
                monthlyContribution = 8000.0,
                deadline = "Nov 2026"
            ),
            Goal(
                id = "g3",
                title = "Gold Goal",
                category = "Gold",
                savedAmount = 0.0,
                targetAmount = 0.0,
                isAtRisk = true,
                monthlyContribution = 2000.0,
                deadline = "Jan 2027",
                isGram = true,
                currentGrams = 4.5,
                targetGrams = 10.0
            )
        )

        private fun initialInvestmentTransactions(): List<InvestmentTransaction> = listOf(
            InvestmentTransaction(
                id = "t1",
                name = "Reliance Industries",
                symbol = "RELIANCE",
                type = TransactionType.BUY,
                quantity = 5.0,
                pricePerShare = 1450.0,
                totalAmount = 7250.0,
                date = "17 Aug",
                assetClass = AssetClass.STOCKS,
                exchangeOrVault = "NSE",
                details = "5 shares @ ₹1,450",
                monthYear = "AUGUST 2023"
            ),
            InvestmentTransaction(
                id = "t2",
                name = "Digital Gold",
                symbol = "GOLD",
                type = TransactionType.BUY,
                quantity = 0.5,
                pricePerShare = 7600.0,
                totalAmount = 3800.0,
                date = "15 Aug",
                assetClass = AssetClass.GOLD,
                exchangeOrVault = "Vault",
                details = "0.5 grams @ ₹7,600/g",
                monthYear = "AUGUST 2023"
            ),
            InvestmentTransaction(
                id = "t3",
                name = "SBI Flexicap Fund",
                symbol = "SBIFLEXI",
                type = TransactionType.SIP,
                quantity = 110.6,
                pricePerShare = 45.2,
                totalAmount = 5000.0,
                date = "10 Aug",
                assetClass = AssetClass.MUTUAL_FUNDS,
                exchangeOrVault = "Nav: 45.2",
                details = "Auto-deducted",
                monthYear = "AUGUST 2023"
            )
        )

        private fun initialBudgets(): List<Budget> = listOf(
            Budget(category = "Food", spentAmount = 8000.0, limitAmount = 10000.0, iconName = "restaurant"),
            Budget(category = "Transport", spentAmount = 2500.0, limitAmount = 5000.0, iconName = "directions_car")
        )
    }
}
