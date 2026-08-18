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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.data.Expense
import com.example.data.SpendWiseRepository
import com.example.navigation.Screen
import com.example.ui.components.BottomNavBar
import com.example.ui.components.SpendWiseTopBar
import com.example.ui.theme.SpendWiseBackground
import com.example.ui.theme.SpendWiseError
import com.example.ui.theme.SpendWiseOnBackground
import com.example.ui.theme.SpendWisePrimary
import com.example.ui.theme.SpendWisePrimaryContainer
import com.example.ui.theme.SpendWiseSurfaceContainerHigh
import com.example.ui.theme.SpendWiseSurfaceContainerLow
import com.example.ui.theme.SpendWiseSurfaceVariant

@Composable
fun ExpensesScreen(
    repository: SpendWiseRepository,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val expenses by repository.expenses.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Today") }

    val filterChips = listOf("Today", "This Week", "This Month", "Custom")

    val filteredExpenses = expenses.filter {
        searchQuery.isBlank() || it.title.contains(searchQuery, ignoreCase = true) || it.category.contains(searchQuery, ignoreCase = true)
    }

    val groupedExpenses = filteredExpenses.groupBy { it.date }

    Scaffold(
        topBar = {
            SpendWiseTopBar(
                title = "Alex Riviera",
                subtitle = "Expenses tracker",
                onAvatarClick = { onNavigate(Screen.InvestmentOverview.route) }
            )
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = Screen.Expenses.route,
                onNavigate = onNavigate
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigate(Screen.AddExpense.route) },
                containerColor = SpendWisePrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .testTag("add_expense_fab")
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Expense",
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
                    text = "Expenses",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = SpendWiseOnBackground
                )
                Text(
                    text = "Track and categorize your recent spending.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF53433F)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Summary Peach Bento Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("expenses_summary_card"),
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
                        Text(
                            text = "TODAY'S SPENDING",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp,
                            color = SpendWisePrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "₹750",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                fontSize = 36.sp,
                                color = Color(0xFF201A18)
                            )
                            Text(
                                text = ".00",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF53433F),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search transactions...", color = Color(0xFF53433F).copy(alpha = 0.7f)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = SpendWisePrimary
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_expenses_input"),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SpendWisePrimary,
                        unfocusedBorderColor = SpendWiseSurfaceVariant,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

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
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .testTag("filter_chip_$chip"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (chip == "Custom") {
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

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Grouped Transaction List
            groupedExpenses.forEach { (dateHeader, list) ->
                item {
                    Text(
                        text = dateHeader,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF53433F),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .testTag("expenses_group_$dateHeader"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceVariant),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            list.forEachIndexed { index, expense ->
                                ExpenseRowItem(expense = expense)
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
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun ExpenseRowItem(expense: Expense) {
    val (icon, bg, tint) = when (expense.category.lowercase()) {
        "food" -> if (expense.title.contains("Tea", ignoreCase = true)) {
            Triple(Icons.Filled.LocalCafe, SpendWisePrimaryContainer, SpendWisePrimary)
        } else {
            Triple(Icons.Filled.Restaurant, SpendWisePrimaryContainer, SpendWisePrimary)
        }
        "transport" -> Triple(Icons.Filled.DirectionsBus, SpendWiseSurfaceContainerLow, SpendWisePrimary)
        "shopping" -> Triple(Icons.Filled.ShoppingBag, Color(0xFFFFDAD6), SpendWiseError)
        "bills" -> Triple(Icons.Filled.Bolt, SpendWiseSurfaceVariant, Color(0xFF201A18))
        else -> Triple(Icons.Filled.Payments, SpendWisePrimaryContainer, SpendWisePrimary)
    }

    val methodIcon = when (expense.paymentMethod.lowercase()) {
        "credit card", "debit card" -> Icons.Filled.CreditCard
        "upi" -> Icons.Filled.AccountBalanceWallet
        "cash" -> Icons.Filled.Payments
        "auto-pay", "bank transfer" -> Icons.Filled.AccountBalance
        else -> Icons.Filled.Payments
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("expense_item_${expense.id}"),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(bg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = expense.category,
                    tint = tint,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = expense.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = SpendWiseOnBackground
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = methodIcon,
                        contentDescription = null,
                        tint = Color(0xFF53433F),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = expense.paymentMethod,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF53433F)
                    )
                }
            }
        }

        Text(
            text = "₹${expense.amount.toInt()}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = SpendWiseOnBackground
        )
    }
}
