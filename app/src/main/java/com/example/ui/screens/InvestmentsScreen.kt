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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.ui.theme.SpendWisePrimary
import com.example.ui.theme.SpendWisePrimaryContainer
import com.example.ui.theme.SpendWiseSecondary
import com.example.ui.theme.SpendWiseSecondaryContainer
import com.example.ui.theme.SpendWiseSilver
import com.example.ui.theme.SpendWiseSilverContainer
import com.example.ui.theme.SpendWiseSuccess
import com.example.ui.theme.SpendWiseSuccessContainer
import com.example.ui.theme.SpendWiseSurfaceContainerHigh
import com.example.ui.theme.SpendWiseSurfaceContainerLow
import com.example.ui.theme.SpendWiseSurfaceVariant

@Composable
fun InvestmentsScreen(
    repository: SpendWiseRepository,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val portfolioSummary by repository.portfolioSummary.collectAsState()
    var selectedTimeframe by remember { mutableStateOf("6M") }

    Scaffold(
        topBar = {
            SpendWiseTopBar(
                title = "Alex Riviera",
                subtitle = "Portfolio summary",
                onAvatarClick = { onNavigate(Screen.InvestmentOverview.route) }
            )
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = Screen.Investments.route,
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

            Text(
                text = "Investments",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = SpendWiseOnBackground
            )

            Text(
                text = "Track and manage your wealth portfolio.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF53433F)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Total Portfolio Value Hero Card (High Density style)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("investments_hero_card"),
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
                        text = "TOTAL PORTFOLIO VALUE",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = Color(0xFF53433F)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = SpendWiseRepository.formatCurrency(portfolioSummary.totalValue),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp,
                            color = SpendWiseOnBackground
                        )

                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(SpendWiseSuccessContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.TrendingUp,
                                contentDescription = null,
                                tint = SpendWiseSuccess,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "+11.4%",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SpendWiseSuccess
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Total Invested: ${SpendWiseRepository.formatCurrency(portfolioSummary.totalInvested)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF53433F)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Performance Line Chart
                    PortfolioPerformanceChart(
                        selectedTimeframe = selectedTimeframe,
                        onTimeframeSelected = { selectedTimeframe = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Asset Allocation Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("asset_allocation_card"),
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
                        text = "Asset Allocation",
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
                            stocksPercent = 45f,
                            mutualFundsPercent = 30f,
                            goldPercent = 15f,
                            silverPercent = 10f,
                            sizeDp = 130.dp
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            AllocationLegendItem(label = "Stocks", percent = "45%", color = SpendWisePrimary)
                            AllocationLegendItem(label = "Mutual Funds", percent = "30%", color = SpendWiseSecondary)
                            AllocationLegendItem(label = "Gold", percent = "15%", color = SpendWiseGold)
                            AllocationLegendItem(label = "Silver", percent = "10%", color = SpendWiseSilver)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Category Cards Grid (Stocks -> goes to InvestmentTransactions)
            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = SpendWiseOnBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Stocks Card
                InvestmentCategoryCard(
                    title = "Stocks",
                    amount = "₹1,25,325",
                    gain = "+15.2%",
                    isPositive = true,
                    icon = Icons.Filled.ShowChart,
                    iconTint = SpendWisePrimary,
                    iconBg = SpendWisePrimaryContainer,
                    onClick = { onNavigate(Screen.InvestmentTransactions.route) },
                    modifier = Modifier.weight(1f).testTag("stocks_category_card")
                )

                // Mutual Funds Card
                InvestmentCategoryCard(
                    title = "Mutual Funds",
                    amount = "₹83,550",
                    gain = "+8.5%",
                    isPositive = true,
                    icon = Icons.Filled.PieChart,
                    iconTint = SpendWiseSecondary,
                    iconBg = SpendWiseSecondaryContainer,
                    onClick = { onNavigate(Screen.InvestmentOverview.route) },
                    modifier = Modifier.weight(1f).testTag("mf_category_card")
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Gold Card
                InvestmentCategoryCard(
                    title = "Gold",
                    amount = "₹41,775",
                    gain = "+5.1%",
                    isPositive = true,
                    icon = Icons.Filled.MonetizationOn,
                    iconTint = SpendWiseGold,
                    iconBg = SpendWiseGoldContainer,
                    onClick = { onNavigate(Screen.InvestmentOverview.route) },
                    modifier = Modifier.weight(1f).testTag("gold_category_card")
                )

                // Silver Card
                InvestmentCategoryCard(
                    title = "Silver",
                    amount = "₹27,850",
                    gain = "-1.2%",
                    isPositive = false,
                    icon = Icons.Filled.Savings,
                    iconTint = SpendWiseSilver,
                    iconBg = SpendWiseSilverContainer,
                    onClick = { onNavigate(Screen.InvestmentOverview.route) },
                    modifier = Modifier.weight(1f).testTag("silver_category_card")
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun InvestmentCategoryCard(
    title: String,
    amount: String,
    gain: String,
    isPositive: Boolean,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color = SpendWiseSurfaceContainerLow,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
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
                        contentDescription = title,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Icon(
                    imageVector = Icons.Filled.ArrowForwardIos,
                    contentDescription = "Open $title",
                    tint = SpendWisePrimary,
                    modifier = Modifier.size(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SpendWiseOnBackground
            )

            Spacer(modifier = Modifier.height(2.dp))

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
                    imageVector = if (isPositive) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
                    contentDescription = null,
                    tint = if (isPositive) SpendWiseSuccess else Color(0xFFBA1A1A),
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = gain,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isPositive) SpendWiseSuccess else Color(0xFFBA1A1A)
                )
            }
        }
    }
}
