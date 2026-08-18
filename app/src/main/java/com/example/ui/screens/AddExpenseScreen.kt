package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SpendWiseRepository
import com.example.ui.theme.SpendWiseBackground
import com.example.ui.theme.SpendWiseOnBackground
import com.example.ui.theme.SpendWisePrimary
import com.example.ui.theme.SpendWisePrimaryContainer
import com.example.ui.theme.SpendWiseSurfaceContainerHigh
import com.example.ui.theme.SpendWiseSurfaceContainerLow
import com.example.ui.theme.SpendWiseSurfaceVariant

data class ExpenseCategoryItem(val name: String, val icon: ImageVector)

@Composable
fun AddExpenseScreen(
    repository: SpendWiseRepository,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var amountText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Food") }
    var selectedSubcategory by remember { mutableStateOf("Restaurant") }
    var selectedPaymentMethod by remember { mutableStateOf("UPI") }
    var dateText by remember { mutableStateOf("17 Aug 2026") }
    var timeText by remember { mutableStateOf("10:30 PM") }
    var isMoreOptionsExpanded by remember { mutableStateOf(true) }
    var descriptionText by remember { mutableStateOf("") }
    var needOrWant by remember { mutableStateOf("Need") }
    var isRecurring by remember { mutableStateOf(false) }

    val categories = listOf(
        ExpenseCategoryItem("Food", Icons.Filled.Restaurant),
        ExpenseCategoryItem("Groceries", Icons.Filled.ShoppingCart),
        ExpenseCategoryItem("Transport", Icons.Filled.DirectionsCar),
        ExpenseCategoryItem("Housing", Icons.Filled.Home),
        ExpenseCategoryItem("Bills", Icons.Filled.Lightbulb),
        ExpenseCategoryItem("Shopping", Icons.Filled.LocalMall),
        ExpenseCategoryItem("Entertainment", Icons.Filled.Movie),
        ExpenseCategoryItem("More", Icons.Filled.MoreHoriz)
    )

    val subcategories = listOf("Restaurant", "Fast Food", "Snacks", "Tea/Coffee", "Food Delivery")
    val paymentMethods = listOf("UPI", "Cash", "Credit Card", "Debit Card", "Bank Transfer")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SpendWiseBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(1.dp, SpendWiseSurfaceVariant)
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("close_add_expense_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = SpendWiseOnBackground
                    )
                }

                Text(
                    text = "Add Expense",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = SpendWiseOnBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 48.dp)
                )
            }

            // Scrollable Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // 1. Amount Section with Soft Peach Container
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = SpendWisePrimaryContainer),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceContainerHigh)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "AMOUNT",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = SpendWisePrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "₹",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                fontSize = 38.sp,
                                color = Color(0xFF201A18),
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            androidx.compose.foundation.text.BasicTextField(
                                value = amountText,
                                onValueChange = { amountText = it },
                                textStyle = TextStyle(
                                    fontSize = 38.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF201A18),
                                    textAlign = TextAlign.Start
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier
                                    .width(140.dp)
                                    .testTag("expense_amount_input"),
                                decorationBox = { innerTextField ->
                                    if (amountText.isEmpty()) {
                                        Text(
                                            text = "0",
                                            style = TextStyle(
                                                fontSize = 38.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF53433F).copy(alpha = 0.4f)
                                            )
                                        )
                                    }
                                    innerTextField()
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Category Selection Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("category_selection_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Category",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SpendWiseOnBackground
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Category Grid 4x2
                        val rows = categories.chunked(4)
                        rows.forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowItems.forEach { cat ->
                                    val isSelected = cat.name == selectedCategory
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(14.dp))
                                            .border(
                                                width = 1.dp,
                                                color = if (isSelected) SpendWisePrimary else SpendWiseSurfaceVariant,
                                                shape = RoundedCornerShape(14.dp)
                                            )
                                            .background(
                                                if (isSelected) SpendWisePrimaryContainer else SpendWiseSurfaceContainerLow
                                            )
                                            .clickable { selectedCategory = cat.name }
                                            .padding(vertical = 10.dp, horizontal = 4.dp)
                                            .testTag("category_button_${cat.name}"),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            imageVector = cat.icon,
                                            contentDescription = cat.name,
                                            tint = if (isSelected) SpendWisePrimary else Color(0xFF53433F),
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = cat.name,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) SpendWisePrimary else Color(0xFF53433F),
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        androidx.compose.material3.HorizontalDivider(color = SpendWiseSurfaceVariant.copy(alpha = 0.6f))
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Subcategory",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF53433F)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(subcategories) { subcat ->
                                val isSelected = subcat == selectedSubcategory
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(if (isSelected) SpendWisePrimary else SpendWiseSurfaceContainerLow)
                                        .border(1.dp, if (isSelected) SpendWisePrimary else SpendWiseSurfaceVariant, RoundedCornerShape(20.dp))
                                        .clickable { selectedSubcategory = subcat }
                                        .padding(horizontal = 14.dp, vertical = 6.dp)
                                        .testTag("subcategory_chip_$subcat"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = subcat,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else Color(0xFF53433F)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 3. Date & Time Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("expense_date_card"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CalendarToday,
                                contentDescription = "Date",
                                tint = SpendWisePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "DATE",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF53433F)
                                )
                                Text(
                                    text = dateText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SpendWiseOnBackground
                                )
                            }
                        }
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("expense_time_card"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Schedule,
                                contentDescription = "Time",
                                tint = SpendWisePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "TIME",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF53433F)
                                )
                                Text(
                                    text = timeText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SpendWiseOnBackground
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 4. Payment Method Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("payment_method_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Payment Method",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SpendWiseOnBackground
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(paymentMethods) { method ->
                                val isSelected = method == selectedPaymentMethod
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(
                                            width = 1.dp,
                                            color = if (isSelected) SpendWisePrimary else SpendWiseSurfaceVariant,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .background(
                                            if (isSelected) SpendWisePrimaryContainer else SpendWiseSurfaceContainerLow
                                        )
                                        .clickable { selectedPaymentMethod = method }
                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                        .testTag("payment_method_$method"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = method,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) SpendWisePrimary else Color(0xFF53433F)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 5. More Options (Expandable)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("more_options_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isMoreOptionsExpanded = !isMoreOptionsExpanded }
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "More Options",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SpendWiseOnBackground
                            )
                            Icon(
                                imageVector = if (isMoreOptionsExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                contentDescription = "Toggle More Options",
                                tint = SpendWisePrimary
                            )
                        }

                        AnimatedVisibility(visible = isMoreOptionsExpanded) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                            ) {
                                androidx.compose.material3.HorizontalDivider(color = SpendWiseSurfaceVariant.copy(alpha = 0.6f))
                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "DESCRIPTION",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF53433F)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                OutlinedTextField(
                                    value = descriptionText,
                                    onValueChange = { descriptionText = it },
                                    placeholder = { Text("What was this expense for?", color = Color(0xFF53433F).copy(alpha = 0.7f)) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("expense_description_input"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = SpendWisePrimary,
                                        unfocusedBorderColor = SpendWiseSurfaceVariant,
                                        focusedContainerColor = SpendWiseSurfaceContainerLow,
                                        unfocusedContainerColor = SpendWiseSurfaceContainerLow
                                    )
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                // Add Receipt Button
                                Row(
                                    modifier = Modifier
                                        .clickable { }
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.AddPhotoAlternate,
                                        contentDescription = "Add Receipt",
                                        tint = SpendWisePrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Add Receipt",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = SpendWisePrimary
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Need / Want Toggle
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(SpendWiseSurfaceContainerLow)
                                        .border(1.dp, SpendWiseSurfaceVariant, RoundedCornerShape(12.dp))
                                        .padding(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (needOrWant == "Need") SpendWisePrimaryContainer else Color.Transparent)
                                            .clickable { needOrWant = "Need" }
                                            .padding(vertical = 8.dp)
                                            .testTag("need_toggle"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Need",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = if (needOrWant == "Need") FontWeight.Bold else FontWeight.Normal,
                                            color = if (needOrWant == "Need") SpendWisePrimary else Color(0xFF53433F)
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (needOrWant == "Want") SpendWisePrimaryContainer else Color.Transparent)
                                            .clickable { needOrWant = "Want" }
                                            .padding(vertical = 8.dp)
                                            .testTag("want_toggle"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Want",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = if (needOrWant == "Want") FontWeight.Bold else FontWeight.Normal,
                                            color = if (needOrWant == "Want") SpendWisePrimary else Color(0xFF53433F)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Recurring Expense Switch
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Recurring Expense",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = SpendWiseOnBackground
                                        )
                                        Text(
                                            text = "Make this a repeating expense",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color(0xFF53433F)
                                        )
                                    }

                                    Switch(
                                        checked = isRecurring,
                                        onCheckedChange = { isRecurring = it },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = SpendWisePrimary
                                        ),
                                        modifier = Modifier.testTag("recurring_switch")
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Fixed Bottom Save Expense Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.White)
                .border(1.dp, SpendWiseSurfaceVariant)
                .padding(16.dp)
        ) {
            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull() ?: 180.0
                    repository.addExpense(
                        title = if (descriptionText.isNotBlank()) descriptionText else selectedCategory,
                        category = selectedCategory,
                        subcategory = selectedSubcategory,
                        amount = amount,
                        paymentMethod = selectedPaymentMethod,
                        date = "Aug 17",
                        time = timeText,
                        description = descriptionText,
                        needOrWant = needOrWant,
                        isRecurring = isRecurring
                    )
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_expense_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SpendWisePrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "SAVE EXPENSE",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
