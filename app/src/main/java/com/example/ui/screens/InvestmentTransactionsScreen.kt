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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AssetClass
import com.example.data.InvestmentTransaction
import com.example.data.SpendWiseRepository
import com.example.data.TransactionType
import com.example.navigation.Screen
import com.example.ui.components.BottomNavBar
import com.example.ui.components.SpendWiseTopBar
import com.example.ui.theme.SpendWiseBackground
import com.example.ui.theme.SpendWiseError
import com.example.ui.theme.SpendWiseErrorContainer
import com.example.ui.theme.SpendWiseGold
import com.example.ui.theme.SpendWiseOnBackground
import com.example.ui.theme.SpendWiseOnErrorContainer
import com.example.ui.theme.SpendWisePrimary
import com.example.ui.theme.SpendWisePrimaryContainer
import com.example.ui.theme.SpendWiseSecondary
import com.example.ui.theme.SpendWiseSecondaryContainer
import com.example.ui.theme.SpendWiseSilver
import com.example.ui.theme.SpendWiseSuccess
import com.example.ui.theme.SpendWiseSuccessContainer
import com.example.ui.theme.SpendWiseSurfaceContainerHigh
import com.example.ui.theme.SpendWiseSurfaceContainerLow
import com.example.ui.theme.SpendWiseSurfaceVariant

@Composable
fun InvestmentTransactionsScreen(
    repository: SpendWiseRepository,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val transactions by repository.investmentTransactions.collectAsState()
    var selectedFilter by remember { mutableStateOf("All") }

    val tabs = listOf("Overview", "Stocks", "Mutual Funds", "Gold", "Silver", "Transactions")
    val filterChips = listOf("All", "Buy", "Sell", "SIP", "More Filters")

    val filteredTransactions = transactions.filter {
        when (selectedFilter.lowercase()) {
            "all" -> true
            "buy" -> it.type == TransactionType.BUY
            "sell" -> it.type == TransactionType.SELL
            "sip" -> it.type == TransactionType.SIP
            else -> true
        }
    }

    val groupedTransactions = filteredTransactions.groupBy { it.monthYear }

    Scaffold(
        topBar = {
            SpendWiseTopBar(
                title = "Alex Riviera",
                subtitle = "Transactions ledger",
                onAvatarClick = { onNavigate(Screen.InvestmentOverview.route) }
            )
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = Screen.Investments.route,
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
                    .testTag("add_investment_tx_fab")
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Investments",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = SpendWiseOnBackground
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Tabs (Overview -> goes to InvestmentOverview, Transactions is active)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(tabs) { tab ->
                        val isSelected = tab == "Transactions"
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) SpendWisePrimary else Color.White)
                                .border(1.dp, if (isSelected) SpendWisePrimary else SpendWiseSurfaceVariant, RoundedCornerShape(20.dp))
                                .clickable {
                                    if (tab == "Overview") {
                                        onNavigate(Screen.InvestmentOverview.route)
                                    }
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .testTag("tx_tab_$tab"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                color = if (isSelected) Color.White else Color(0xFF53433F)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(filterChips) { chip ->
                        val isSelected = chip == selectedFilter
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) SpendWisePrimary else Color.White)
                                .border(1.dp, if (isSelected) SpendWisePrimary else SpendWiseSurfaceVariant, RoundedCornerShape(20.dp))
                                .clickable { selectedFilter = chip }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                                .testTag("filter_chip_$chip"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (chip == "More Filters") {
                                    Icon(
                                        imageVector = Icons.Filled.Tune,
                                        contentDescription = null,
                                        tint = if (isSelected) Color.White else SpendWisePrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(
                                    text = chip,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isSelected) Color.White else Color(0xFF53433F)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Grouped Transaction Cards by Month
            groupedTransactions.forEach { (month, list) ->
                item {
                    Text(
                        text = month.uppercase(),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = Color(0xFF53433F),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .testTag("tx_card_group_$month"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceVariant),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            list.forEachIndexed { index, tx ->
                                TransactionLedgerRow(tx = tx)
                                if (index < list.size - 1) {
                                    androidx.compose.material3.HorizontalDivider(
                                        color = SpendWiseSurfaceVariant.copy(alpha = 0.6f),
                                        thickness = 1.dp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))

                // View Older Transactions Button
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("view_older_transactions_button"),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceVariant),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = SpendWiseOnBackground
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.History,
                        contentDescription = null,
                        tint = SpendWisePrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "View Older Transactions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun TransactionLedgerRow(tx: InvestmentTransaction) {
    val (icon, tint, bg) = when (tx.assetClass) {
        AssetClass.STOCKS -> Triple(Icons.Filled.ShowChart, SpendWisePrimary, SpendWisePrimaryContainer)
        AssetClass.MUTUAL_FUNDS -> Triple(Icons.Filled.PieChart, SpendWiseSecondary, SpendWiseSecondaryContainer)
        AssetClass.GOLD -> Triple(Icons.Filled.MonetizationOn, SpendWiseGold, SpendWisePrimaryContainer.copy(alpha = 0.5f))
        AssetClass.SILVER -> Triple(Icons.Filled.Savings, SpendWiseSilver, SpendWiseSurfaceContainerLow)
    }

    val (badgeBg, badgeTextColor, badgeText) = when (tx.type) {
        TransactionType.BUY -> Triple(SpendWiseSuccessContainer, SpendWiseSuccess, "BUY")
        TransactionType.SELL -> Triple(SpendWiseErrorContainer, SpendWiseOnErrorContainer, "SELL")
        TransactionType.SIP -> Triple(SpendWisePrimaryContainer, SpendWisePrimary, "SIP")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("tx_row_${tx.id}"),
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
                    .background(bg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = tx.name,
                    tint = tint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = tx.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SpendWiseOnBackground
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(badgeBg)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = badgeText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeTextColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${tx.date} • ${tx.details}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF53433F)
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "₹${SpendWiseRepository.formatCurrencyPlain(tx.totalAmount)}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = SpendWiseOnBackground
            )
            Text(
                text = tx.exchangeOrVault,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF53433F)
            )
        }
    }
}
