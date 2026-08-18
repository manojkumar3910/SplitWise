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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ui.components.AllocationLegendItem
import com.example.ui.components.AssetAllocationDonut
import com.example.ui.components.BottomNavBar
import com.example.ui.components.PortfolioPerformanceChart
import com.example.ui.components.SpendWiseTopBar
import com.example.ui.theme.SpendWiseBackground
import com.example.ui.theme.SpendWiseGold
import com.example.ui.theme.SpendWiseGoldContainer
import com.example.ui.theme.SpendWiseOnBackground
import com.example.ui.theme.SpendWiseOnSurfaceVariant
import com.example.ui.theme.SpendWisePrimary
import com.example.ui.theme.SpendWisePrimaryContainer
import com.example.ui.theme.SpendWiseSecondary
import com.example.ui.theme.SpendWiseSecondaryContainer
import com.example.ui.theme.SpendWiseSilver
import com.example.ui.theme.SpendWiseSilverContainer
import com.example.ui.theme.SpendWiseSuccess
import com.example.ui.theme.SpendWiseSurfaceContainerHigh
import com.example.ui.theme.SpendWiseSurfaceContainerLow
import com.example.ui.theme.SpendWiseSurfaceVariant

@Composable
fun InvestmentOverviewScreen(
    repository: SpendWiseRepository,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val portfolioSummary by repository.portfolioSummary.collectAsState()
    var selectedTab by remember { mutableStateOf("Overview") }
    var selectedTimeframe by remember { mutableStateOf("6M") }

    val tabs = listOf("Overview", "Stocks", "Mutual Funds", "Gold", "Silver", "Transactions")

    Scaffold(
        topBar = {
            SpendWiseTopBar(
                title = "Alex Riviera",
                subtitle = "Wealth manager",
                onAvatarClick = { onNavigate(Screen.InvestmentOverview.route) }
            )
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = Screen.InvestmentOverview.route,
                onNavigate = onNavigate
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigate(Screen.AddInvestmentSelect.route) },
                containerColor = SpendWisePrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .testTag("add_investment_fab")
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Investment",
                    modifier = Modifier.size(28.dp)
                )
            }
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Investments",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = SpendWiseOnBackground
                    )
                    Text(
                        text = "Track and optimize your portfolio",
                        style = MaterialTheme.typography.bodyLarge,
                        color = SpendWiseOnSurfaceVariant
                    )
                }

                Button(
                    onClick = { onNavigate(Screen.AddInvestmentSelect.route) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SpendWisePrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("add_investment_header_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Add", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Scrollable Category Tabs
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(tabs) { tab ->
                    val isSelected = tab == selectedTab
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) SpendWisePrimary else Color.White)
                            .border(1.dp, if (isSelected) SpendWisePrimary else SpendWiseSurfaceVariant, RoundedCornerShape(20.dp))
                            .clickable {
                                if (tab == "Transactions") {
                                    onNavigate(Screen.InvestmentTransactions.route)
                                } else {
                                    selectedTab = tab
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .testTag("tab_$tab"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                            color = if (isSelected) Color.White else Color(0xFF4B5563)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4 Bento Metric Cards (High Density)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OverviewMetricCard(
                    title = "Current Value",
                    value = SpendWiseRepository.formatCurrency(portfolioSummary.totalValue),
                    subtext = "+₹1,250 Today",
                    isPositive = true,
                    modifier = Modifier.weight(1f).testTag("overview_metric_current_value")
                )
                OverviewMetricCard(
                    title = "Total Invested",
                    value = SpendWiseRepository.formatCurrency(portfolioSummary.totalInvested),
                    subtext = "Principal",
                    isPositive = null,
                    modifier = Modifier.weight(1f).testTag("overview_metric_invested")
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OverviewMetricCard(
                    title = "Total Profit",
                    value = "+₹28,500",
                    subtext = "+11.40%",
                    isPositive = true,
                    modifier = Modifier.weight(1f).testTag("overview_metric_profit")
                )
                OverviewMetricCard(
                    title = "Today's Change",
                    value = "+₹1,250",
                    subtext = "+0.45%",
                    isPositive = true,
                    modifier = Modifier.weight(1f).testTag("overview_metric_today_change")
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Portfolio Performance Chart Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("portfolio_performance_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    PortfolioPerformanceChart(
                        selectedTimeframe = selectedTimeframe,
                        onTimeframeSelected = { selectedTimeframe = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Portfolio Allocation Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("portfolio_allocation_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Portfolio Allocation",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = SpendWiseOnBackground
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AssetAllocationDonut(
                            stocksPercent = 48f,
                            mutualFundsPercent = 34f,
                            goldPercent = 10f,
                            silverPercent = 8f,
                            sizeDp = 130.dp
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AllocationLegendItem(label = "Stocks", percent = "48%", color = SpendWisePrimary)
                            AllocationLegendItem(label = "Mutual Funds", percent = "34%", color = SpendWiseSecondary)
                            AllocationLegendItem(label = "Gold", percent = "10%", color = SpendWiseGold)
                            AllocationLegendItem(label = "Silver", percent = "8%", color = SpendWiseSilver)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Performance by Asset Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("performance_by_asset_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Performance by Asset",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = SpendWiseOnBackground
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AssetPerformanceRow(
                        name = "Stocks",
                        currentVal = "₹1,33,680",
                        returns = "+₹15,200",
                        percent = "+12.8%",
                        icon = Icons.Filled.ShowChart,
                        iconBg = SpendWisePrimaryContainer,
                        iconTint = SpendWisePrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AssetPerformanceRow(
                        name = "Mutual Funds",
                        currentVal = "₹94,690",
                        returns = "+₹11,450",
                        percent = "+13.7%",
                        icon = Icons.Filled.PieChart,
                        iconBg = SpendWiseSecondaryContainer,
                        iconTint = SpendWiseSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AssetPerformanceRow(
                        name = "Gold",
                        currentVal = "₹27,850",
                        returns = "+₹1,850",
                        percent = "+7.1%",
                        icon = Icons.Filled.MonetizationOn,
                        iconBg = SpendWiseGoldContainer,
                        iconTint = SpendWiseGold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AssetPerformanceRow(
                        name = "Silver",
                        currentVal = "₹22,280",
                        returns = "₹0",
                        percent = "0.0%",
                        icon = Icons.Filled.Savings,
                        iconBg = SpendWiseSilverContainer,
                        iconTint = SpendWiseSilver
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Active SIPs Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("active_sips_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SpendWiseSurfaceContainerLow),
                border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceContainerHigh),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Active SIPs",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = SpendWiseOnBackground
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(SpendWiseSecondaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.PieChart,
                                    contentDescription = null,
                                    tint = SpendWiseSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "SBI Flexicap Fund",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SpendWiseOnBackground
                                )
                                Text(
                                    text = "Next date: 25 Aug 2026",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF4B5563)
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "₹5,000",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = SpendWiseOnBackground
                            )
                            Text(
                                text = "Monthly",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF4B5563)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // My Investments Cards
            Text(
                text = "My Investments",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = SpendWiseOnBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            HoldingAssetCard(
                name = "Reliance Industries",
                tag = "Stocks • 5 shares",
                currentVal = "₹7,250",
                returns = "+₹650",
                percent = "+9.8%",
                isPositive = true,
                icon = Icons.Filled.ShowChart,
                iconTint = SpendWisePrimary,
                onClick = { onNavigate(Screen.InvestmentTransactions.route) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            HoldingAssetCard(
                name = "SBI Flexicap Fund",
                tag = "Mutual Fund • 110.6 units",
                currentVal = "₹5,000",
                returns = "+₹350",
                percent = "+7.5%",
                isPositive = true,
                icon = Icons.Filled.PieChart,
                iconTint = SpendWiseSecondary,
                onClick = { onNavigate(Screen.InvestmentTransactions.route) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            HoldingAssetCard(
                name = "Digital Gold",
                tag = "Gold • 0.5g",
                currentVal = "₹3,800",
                returns = "+₹180",
                percent = "+5.0%",
                isPositive = true,
                icon = Icons.Filled.MonetizationOn,
                iconTint = SpendWiseGold,
                onClick = { onNavigate(Screen.InvestmentTransactions.route) }
            )

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun OverviewMetricCard(
    title: String,
    value: String,
    subtext: String,
    isPositive: Boolean?,
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
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF4B5563)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = SpendWiseOnBackground
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isPositive != null) {
                    Icon(
                        imageVector = if (isPositive) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
                        contentDescription = null,
                        tint = if (isPositive) SpendWiseSuccess else Color(0xFF4B5563),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                }
                Text(
                    text = subtext,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isPositive == true) SpendWiseSuccess else Color(0xFF4B5563)
                )
            }
        }
    }
}

@Composable
fun AssetPerformanceRow(
    name: String,
    currentVal: String,
    returns: String,
    percent: String,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = name,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = SpendWiseOnBackground
                )
                Text(
                    text = currentVal,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4B5563)
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = returns,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SpendWiseSuccess
            )
            Text(
                text = percent,
                style = MaterialTheme.typography.labelSmall,
                color = SpendWiseSuccess
            )
        }
    }
}

@Composable
fun HoldingAssetCard(
    name: String,
    tag: String,
    currentVal: String,
    returns: String,
    percent: String,
    isPositive: Boolean,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("holding_${name.lowercase().replace(" ", "_")}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(SpendWiseSurfaceContainerLow),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = name,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SpendWiseOnBackground
                    )
                    Text(
                        text = tag,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF4B5563)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = currentVal,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = SpendWiseOnBackground
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$returns ($percent)",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isPositive) SpendWiseSuccess else Color(0xFF4B5563)
                    )
                }
            }
        }
    }
}
