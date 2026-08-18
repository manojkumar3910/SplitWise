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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Goal
import com.example.data.SpendWiseRepository
import com.example.navigation.Screen
import com.example.ui.components.BottomNavBar
import com.example.ui.components.CircularProgressGauge
import com.example.ui.components.SpendWiseTopBar
import com.example.ui.theme.SpendWiseBackground
import com.example.ui.theme.SpendWiseError
import com.example.ui.theme.SpendWiseErrorContainer
import com.example.ui.theme.SpendWiseGold
import com.example.ui.theme.SpendWiseOnBackground
import com.example.ui.theme.SpendWiseOnErrorContainer
import com.example.ui.theme.SpendWisePrimary
import com.example.ui.theme.SpendWisePrimaryContainer
import com.example.ui.theme.SpendWiseSuccess
import com.example.ui.theme.SpendWiseSuccessContainer
import com.example.ui.theme.SpendWiseSurfaceContainerHigh
import com.example.ui.theme.SpendWiseSurfaceContainerLow
import com.example.ui.theme.SpendWiseSurfaceVariant

@Composable
fun FinancialGoalsScreen(
    repository: SpendWiseRepository,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val goals by repository.goals.collectAsState()
    var selectedFilter by remember { mutableStateOf("All") }
    var showCreateGoalDialog by remember { mutableStateOf(false) }

    val filterChips = listOf("All", "Active", "Completed", "Paused")

    Scaffold(
        topBar = {
            SpendWiseTopBar(
                title = "Alex Riviera",
                subtitle = "Goal planner",
                onAvatarClick = { onNavigate(Screen.Profile.route) }
            )
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = Screen.FinancialGoals.route,
                onNavigate = onNavigate
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateGoalDialog = true },
                containerColor = SpendWisePrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .testTag("create_goal_fab")
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Create Goal",
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Financial Goals",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            color = SpendWiseOnBackground
                        )
                        Text(
                            text = "Set targets and build your future.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF53433F)
                        )
                    }

                    Button(
                        onClick = { showCreateGoalDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SpendWisePrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("create_goal_header_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Goal", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Summary Bento Grid (High Density cards)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GoalSummaryMetricCard(
                        title = "Total Goals",
                        value = "6",
                        modifier = Modifier.weight(1f).testTag("metric_total_goals")
                    )
                    GoalSummaryMetricCard(
                        title = "Total Target",
                        value = "₹8,50,000",
                        modifier = Modifier.weight(1f).testTag("metric_total_target")
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GoalSummaryMetricCard(
                        title = "Total Saved",
                        value = "₹3,25,000",
                        modifier = Modifier.weight(1f).testTag("metric_total_saved")
                    )

                    // Overall Progress Gauge Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("metric_overall_progress"),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceVariant),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Overall",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF53433F)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "38.2%",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = SpendWiseOnBackground
                                )
                            }

                            CircularProgressGauge(
                                progressPercent = 38.2f,
                                size = 42.dp,
                                strokeWidth = 5.dp,
                                progressColor = SpendWisePrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Filters & Sort Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
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
                                    .testTag("goal_filter_$chip"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = chip,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else Color(0xFF53433F)
                                )
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .border(1.dp, SpendWiseSurfaceVariant, RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Sort by",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF53433F)
                        )
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = null,
                            tint = SpendWisePrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Goals Cards List
            items(goals) { goal ->
                GoalItemCard(goal = goal)
                Spacer(modifier = Modifier.height(14.dp))
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))

                // Goal Insights Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("goal_insights_card"),
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = null,
                                tint = SpendWisePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Goal Insights & Recommendations",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SpendWiseOnBackground
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        InsightBullet(
                            text = "Increase monthly Emergency Fund contribution by ₹5,000 to reach your ₹2,00,000 target 2 months earlier."
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        InsightBullet(
                            text = "Gold price dip detected - consider topping up 1g this week to get your Gold Goal back on track."
                        )
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Create Goal Dialog
        if (showCreateGoalDialog) {
            CreateGoalDialog(
                onDismiss = { showCreateGoalDialog = false },
                onConfirm = { title, cat, target, monthly, deadline ->
                    repository.addGoal(
                        title = title,
                        category = cat,
                        targetAmount = target,
                        monthlyContribution = monthly,
                        deadline = deadline
                    )
                    showCreateGoalDialog = false
                }
            )
        }
    }
}

@Composable
fun GoalSummaryMetricCard(
    title: String,
    value: String,
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
                color = Color(0xFF53433F)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = SpendWiseOnBackground
            )
        }
    }
}

@Composable
fun GoalItemCard(goal: Goal) {
    val (icon, tint, bg) = when (goal.category.lowercase()) {
        "emergency fund" -> Triple(Icons.Filled.Shield, SpendWisePrimary, SpendWisePrimaryContainer)
        "electronics" -> Triple(Icons.Filled.PhoneAndroid, SpendWisePrimary, SpendWiseSurfaceContainerLow)
        "gold" -> Triple(Icons.Filled.MonetizationOn, SpendWiseGold, SpendWisePrimaryContainer.copy(alpha = 0.5f))
        else -> Triple(Icons.Filled.Savings, SpendWisePrimary, SpendWisePrimaryContainer)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("goal_card_${goal.id}"),
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
                            .background(bg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = goal.title,
                            tint = tint,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = goal.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SpendWiseOnBackground
                        )
                        Text(
                            text = if (goal.isGram) "Target: ${goal.targetGrams}g" else "Target: ${SpendWiseRepository.formatCurrency(goal.targetAmount)}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF53433F)
                        )
                    }
                }

                // Status Badge
                if (goal.isAtRisk) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SpendWiseErrorContainer)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Warning,
                                contentDescription = null,
                                tint = SpendWiseOnErrorContainer,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "At Risk",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SpendWiseOnErrorContainer
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SpendWiseSuccessContainer)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "On Track",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SpendWiseSuccess
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progress text and bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (goal.isGram) "Current: ${goal.currentGrams}g" else "Saved: ${SpendWiseRepository.formatCurrency(goal.savedAmount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = SpendWiseOnBackground
                )
                Text(
                    text = "${goal.progressPercent.toInt()}%",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (goal.isAtRisk) SpendWiseError else SpendWisePrimary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { goal.progressPercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (goal.isAtRisk) SpendWiseError else SpendWisePrimary,
                trackColor = SpendWisePrimaryContainer.copy(alpha = 0.5f),
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Footer details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (goal.monthlyContribution > 0) {
                    Text(
                        text = "₹${SpendWiseRepository.formatCurrencyPlain(goal.monthlyContribution)}/mo",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF53433F)
                    )
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.CalendarToday,
                        contentDescription = null,
                        tint = Color(0xFF53433F),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = goal.deadline,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF53433F)
                    )
                }
            }
        }
    }
}

@Composable
fun InsightBullet(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .size(6.dp)
                .background(SpendWisePrimary, CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF53433F)
        )
    }
}

@Composable
fun CreateGoalDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, category: String, target: Double, monthly: Double, deadline: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Savings") }
    var targetText by remember { mutableStateOf("") }
    var monthlyText by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("Dec 2026") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = "Create New Goal",
                fontWeight = FontWeight.Bold,
                color = SpendWiseOnBackground
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Goal Title") },
                    placeholder = { Text("e.g. Vacation Fund") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SpendWisePrimary,
                        unfocusedBorderColor = SpendWiseSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = targetText,
                    onValueChange = { targetText = it },
                    label = { Text("Target Amount (₹)") },
                    placeholder = { Text("100000") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SpendWisePrimary,
                        unfocusedBorderColor = SpendWiseSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = monthlyText,
                    onValueChange = { monthlyText = it },
                    label = { Text("Monthly Contribution (₹)") },
                    placeholder = { Text("10000") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SpendWisePrimary,
                        unfocusedBorderColor = SpendWiseSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = deadline,
                    onValueChange = { deadline = it },
                    label = { Text("Target Deadline") },
                    placeholder = { Text("Dec 2026") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SpendWisePrimary,
                        unfocusedBorderColor = SpendWiseSurfaceVariant
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val target = targetText.toDoubleOrNull() ?: 50000.0
                    val monthly = monthlyText.toDoubleOrNull() ?: 5000.0
                    onConfirm(
                        if (title.isNotBlank()) title else "New Goal",
                        category,
                        target,
                        monthly,
                        deadline
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = SpendWisePrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Create Goal")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFF53433F))
            }
        }
    )
}
