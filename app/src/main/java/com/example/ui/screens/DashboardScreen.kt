package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SpendWiseRepository
import com.example.navigation.Screen
import com.example.ui.components.BottomNavBar
import com.example.ui.components.SimpleSparklineChart
import com.example.ui.components.SpendWiseTopBar
import com.example.ui.theme.SpendWiseBackground
import com.example.ui.theme.SpendWiseError
import com.example.ui.theme.SpendWiseOnBackground
import com.example.ui.theme.SpendWiseOnSurfaceVariant
import com.example.ui.theme.SpendWisePrimary
import com.example.ui.theme.SpendWisePrimaryContainer
import com.example.ui.theme.SpendWiseSecondaryContainer
import com.example.ui.theme.SpendWiseSuccess
import com.example.ui.theme.SpendWiseSurfaceContainerHigh
import com.example.ui.theme.SpendWiseSurfaceContainerLow
import com.example.ui.theme.SpendWiseSurfaceVariant

@Composable
fun DashboardScreen(
    repository: SpendWiseRepository,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val portfolioSummary by repository.portfolioSummary.collectAsState()
    val expenses by repository.expenses.collectAsState()
    val goals by repository.goals.collectAsState()
    val budgets by repository.budgets.collectAsState()
    val userBalance by repository.userBalance.collectAsState()
    val userIncome by repository.userIncome.collectAsState()
    val isApiConnected by repository.isApiConnected.collectAsState()
    val isSyncing by repository.isSyncing.collectAsState()
    val apiStatusMessage by repository.apiStatusMessage.collectAsState()

    val totalExpenses = expenses.sumOf { it.amount }
    val totalGoalTarget = goals.sumOf { it.targetAmount }.let { if (it == 0.0) 400000.0 else it }
    val totalGoalSaved = goals.sumOf { it.savedAmount }.let { if (it == 0.0) 207000.0 else it }
    val goalProgressPercent = if (totalGoalTarget > 0) ((totalGoalSaved / totalGoalTarget) * 100).toFloat() else 0f

    Scaffold(
        topBar = {
            SpendWiseTopBar(
                title = "Alex Riviera",
                subtitle = if (isSyncing) "Syncing..." else "Good morning",
                onNotificationClick = { repository.refreshAllData() },
                onAvatarClick = { onNavigate(Screen.Profile.route) }
            )
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = Screen.Dashboard.route,
                onNavigate = onNavigate
            )
        },
        containerColor = SpendWiseBackground,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // 1. Highlight: Financial Goals Sky Bento Card (Clickable -> Financial Goals)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(Screen.FinancialGoals.route) }
                    .testTag("dashboard_financial_goals_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SpendWisePrimaryContainer),
                border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceContainerHigh),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.8f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.TrackChanges,
                                    contentDescription = null,
                                    tint = SpendWisePrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "FINANCIAL GOALS",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp,
                                    color = SpendWisePrimary
                                )
                                Text(
                                    text = "Emergency Fund & Vacation",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SpendWiseOnBackground
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = "Go to Goals",
                            tint = SpendWisePrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "Current Goals Progress",
                                fontSize = 12.sp,
                                color = SpendWiseOnSurfaceVariant
                            )
                            Text(
                                text = "${SpendWiseRepository.formatCurrency(totalGoalSaved)} / ${SpendWiseRepository.formatCurrency(totalGoalTarget)}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = SpendWiseOnBackground
                            )
                        }
                        Text(
                            text = String.format(java.util.Locale.US, "%.1f%%", goalProgressPercent),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = SpendWisePrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { (goalProgressPercent / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = SpendWisePrimary,
                        trackColor = SpendWisePrimary.copy(alpha = 0.2f),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Bento Grid: 2x2 Metric Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    title = "Balance",
                    amount = SpendWiseRepository.formatCurrency(userBalance),
                    change = if (isApiConnected) "● MongoDB" else "● Local",
                    isPositive = isApiConnected,
                    icon = Icons.Filled.AccountBalanceWallet,
                    iconTint = SpendWisePrimary,
                    modifier = Modifier.weight(1f).testTag("metric_balance")
                )
                MetricCard(
                    title = "Income",
                    amount = SpendWiseRepository.formatCurrency(userIncome),
                    change = "+5.1%",
                    isPositive = true,
                    icon = Icons.Filled.ArrowDownward,
                    iconTint = SpendWiseSuccess,
                    modifier = Modifier.weight(1f).testTag("metric_income")
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    title = "Expenses",
                    amount = SpendWiseRepository.formatCurrency(totalExpenses),
                    change = "${expenses.size} items",
                    isPositive = false,
                    icon = Icons.Filled.ArrowUpward,
                    iconTint = SpendWiseError,
                    modifier = Modifier.weight(1f).testTag("metric_expenses")
                )
                MetricCard(
                    title = "Investments",
                    amount = SpendWiseRepository.formatCurrency(portfolioSummary.totalValue),
                    change = "+${portfolioSummary.profitPercent}%",
                    isPositive = true,
                    icon = Icons.Filled.ShowChart,
                    iconTint = SpendWisePrimary,
                    modifier = Modifier.weight(1f).testTag("metric_investments")
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Investment Portfolio Bento Card (Clickable -> Investment Overview)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(Screen.InvestmentOverview.route) }
                    .testTag("dashboard_investment_portfolio_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "Investment Portfolio",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = SpendWiseOnBackground
                            )
                            Text(
                                text = "Total Holdings",
                                style = MaterialTheme.typography.bodyMedium,
                                color = SpendWiseOnSurfaceVariant
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = SpendWiseRepository.formatCurrency(portfolioSummary.totalValue),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = SpendWiseOnBackground
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFE8F5E9))
                                    .padding(horizontal = 8.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowUpward,
                                    contentDescription = null,
                                    tint = SpendWiseSuccess,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "+₹28,500 (+14.8%)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SpendWiseSuccess
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Sparkline area chart with terracotta styling
                    SimpleSparklineChart(
                        modifier = Modifier.fillMaxWidth(),
                        height = 80.dp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3 mini asset breakdown pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MiniAssetPill(
                            label = "Stocks",
                            amount = "₹74.7K",
                            modifier = Modifier.weight(1f)
                        )
                        MiniAssetPill(
                            label = "Mutual Funds",
                            amount = "₹31.1K",
                            modifier = Modifier.weight(1f)
                        )
                        MiniAssetPill(
                            label = "Gold",
                            amount = "₹12.5K",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Recent Expenses Card (Clean White Container)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dashboard_recent_transactions_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Recent Expenses",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = SpendWiseOnBackground
                        )

                        Text(
                            text = "View All",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = SpendWisePrimary,
                            modifier = Modifier
                                .clickable { onNavigate(Screen.Expenses.route) }
                                .padding(4.dp)
                                .testTag("view_all_expenses_button")
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Transaction Rows
                    RecentTransactionRow(
                        title = "Whole Foods Market",
                        category = "Groceries",
                        amount = "-₹4,500",
                        isIncome = false,
                        icon = Icons.Filled.ShoppingCart
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    RecentTransactionRow(
                        title = "Uber Ride",
                        category = "Transport",
                        amount = "-₹850",
                        isIncome = false,
                        icon = Icons.Filled.DirectionsCar
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    RecentTransactionRow(
                        title = "Monthly Salary",
                        category = "Income",
                        amount = "+₹40,000",
                        isIncome = true,
                        icon = Icons.Filled.ArrowDownward
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    RecentTransactionRow(
                        title = "Blue Tokai Coffee",
                        category = "Dining",
                        amount = "-₹350",
                        isIncome = false,
                        icon = Icons.Filled.Restaurant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun MiniAssetPill(
    label: String,
    amount: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SpendWiseSurfaceContainerLow)
            .border(1.dp, SpendWiseSurfaceVariant, RoundedCornerShape(12.dp))
            .padding(vertical = 8.dp, horizontal = 10.dp)
    ) {
        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color(0xFF4B5563)
            )
            Text(
                text = amount,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = SpendWiseOnBackground
            )
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    amount: String,
    change: String,
    isPositive: Boolean,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4B5563)
                )
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(SpendWisePrimaryContainer.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = amount,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = SpendWiseOnBackground
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isPositive) Icons.Filled.TrendingUp else Icons.Filled.TrendingDown,
                    contentDescription = null,
                    tint = if (isPositive) SpendWiseSuccess else SpendWiseError,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = change,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isPositive) SpendWiseSuccess else SpendWiseError
                )
            }
        }
    }
}

@Composable
fun RecentTransactionRow(
    title: String,
    category: String,
    amount: String,
    isIncome: Boolean,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.7f))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = category,
                    tint = if (isIncome) SpendWiseSuccess else SpendWisePrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = SpendWiseOnBackground
                )
                Text(
                    text = category,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF4B5563)
                )
            }
        }

        Text(
            text = amount,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = if (isIncome) SpendWiseSuccess else SpendWiseOnBackground
        )
    }
}
