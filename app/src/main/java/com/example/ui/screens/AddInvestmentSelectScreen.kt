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
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ui.theme.SpendWiseSurfaceContainerLow
import com.example.ui.theme.SpendWiseSurfaceVariant

@Composable
fun AddInvestmentSelectScreen(
    onClose: () -> Unit,
    onSelectStocks: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpendWiseBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Close Button in header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, SpendWiseSurfaceVariant, CircleShape)
                    .testTag("close_investment_select_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = SpendWiseOnBackground
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "What would you like to add?",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = SpendWiseOnBackground,
            modifier = Modifier.testTag("investment_select_title")
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Select an asset class to begin tracking your new investment.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF53433F)
        )

        Spacer(modifier = Modifier.height(28.dp))

        // 1. Stocks Card
        AssetSelectCard(
            title = "Stocks",
            subtitle = "Track shares, equities, and ETFs in your portfolio",
            icon = Icons.Filled.ShowChart,
            iconTint = SpendWisePrimary,
            iconBg = SpendWisePrimaryContainer,
            onClick = onSelectStocks,
            testTag = "select_stocks_card"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 2. Mutual Funds Card
        AssetSelectCard(
            title = "Mutual Funds",
            subtitle = "Track SIPs, ELSS, index funds, and active portfolios",
            icon = Icons.Filled.PieChart,
            iconTint = SpendWiseSecondary,
            iconBg = SpendWiseSecondaryContainer,
            onClick = onSelectStocks,
            testTag = "select_mutual_funds_card"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 3. Gold Card
        AssetSelectCard(
            title = "Gold",
            subtitle = "Track 24K digital gold, Sovereign Gold Bonds (SGB)",
            icon = Icons.Filled.MonetizationOn,
            iconTint = SpendWiseGold,
            iconBg = SpendWiseGoldContainer,
            onClick = onSelectStocks,
            testTag = "select_gold_card"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 4. Silver Card
        AssetSelectCard(
            title = "Silver",
            subtitle = "Track digital silver holdings and commodity investments",
            icon = Icons.Filled.Savings,
            iconTint = SpendWiseSilver,
            iconBg = SpendWiseSilverContainer,
            onClick = onSelectStocks,
            testTag = "select_silver_card"
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun AssetSelectCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color = SpendWiseSurfaceContainerLow,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconTint,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = SpendWiseOnBackground
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF53433F)
                    )
                }
            }

            Icon(
                imageVector = Icons.Filled.ArrowForwardIos,
                contentDescription = "Select $title",
                tint = SpendWisePrimary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
