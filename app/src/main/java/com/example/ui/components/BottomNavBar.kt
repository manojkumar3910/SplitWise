package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.navigation.Screen
import com.example.ui.theme.SpendWisePrimary
import com.example.ui.theme.SpendWisePrimaryContainer
import com.example.ui.theme.SpendWiseSurfaceVariant

enum class NavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String,
    val testTag: String
) {
    HOME("Home", Icons.Filled.Home, Icons.Outlined.Home, Screen.Dashboard.route, "nav_home"),
    EXPENSES("Expenses", Icons.Filled.Payments, Icons.Outlined.Payments, Screen.Expenses.route, "nav_expenses"),
    INVESTMENTS("Investments", Icons.Filled.TrendingUp, Icons.Outlined.TrendingUp, Screen.InvestmentOverview.route, "nav_investments"),
    GOALS("Goals", Icons.Filled.TrackChanges, Icons.Outlined.TrackChanges, Screen.FinancialGoals.route, "nav_goals"),
    PROFILE("Profile", Icons.Filled.Person, Icons.Outlined.Person, Screen.InvestmentOverview.route, "nav_profile")
}

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.White,
        shadowElevation = 4.dp,
        tonalElevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = SpendWiseSurfaceVariant
            )
            .testTag("bottom_nav_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 10.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem.values().forEach { item ->
                val isSelected = when (item) {
                    NavItem.HOME -> currentRoute == Screen.Dashboard.route
                    NavItem.EXPENSES -> currentRoute == Screen.Expenses.route || currentRoute == Screen.AddExpense.route
                    NavItem.INVESTMENTS -> currentRoute == Screen.Investments.route || currentRoute == Screen.InvestmentOverview.route || currentRoute == Screen.InvestmentTransactions.route || currentRoute == Screen.AddInvestmentSelect.route || currentRoute == Screen.AddStockInvestment.route
                    NavItem.GOALS -> currentRoute == Screen.FinancialGoals.route
                    NavItem.PROFILE -> false
                }

                val interactionSource = remember { MutableInteractionSource() }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .then(
                            if (isSelected) {
                                Modifier.background(SpendWisePrimaryContainer)
                            } else {
                                Modifier
                            }
                        )
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            if (currentRoute != item.route) {
                                onNavigate(item.route)
                            }
                        }
                        .padding(horizontal = if (isSelected) 14.dp else 10.dp, vertical = 6.dp)
                        .testTag(item.testTag)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label,
                            tint = if (isSelected) SpendWisePrimary else Color(0xFF53433F).copy(alpha = 0.65f),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) SpendWisePrimary else Color(0xFF53433F).copy(alpha = 0.65f)
                        )
                    }
                }
            }
        }
    }
}
