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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AssetClass
import com.example.data.SpendWiseRepository
import com.example.data.TransactionType
import com.example.ui.theme.SpendWiseBackground
import com.example.ui.theme.SpendWiseError
import com.example.ui.theme.SpendWiseOnBackground
import com.example.ui.theme.SpendWisePrimary
import com.example.ui.theme.SpendWisePrimaryContainer
import com.example.ui.theme.SpendWiseSecondary
import com.example.ui.theme.SpendWiseSurfaceContainerLow
import com.example.ui.theme.SpendWiseSurfaceVariant

@Composable
fun AddStockInvestmentScreen(
    repository: SpendWiseRepository,
    onBack: () -> Unit,
    onSubmitSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var stockName by remember { mutableStateOf("Reliance Industries") }
    var stockSymbol by remember { mutableStateOf("RELIANCE") }
    var transactionType by remember { mutableStateOf(TransactionType.BUY) }
    var quantityText by remember { mutableStateOf("10") }
    var priceText by remember { mutableStateOf("1450") }
    var dateText by remember { mutableStateOf("17 Aug 2026") }
    var chargesText by remember { mutableStateOf("0.00") }
    var notesText by remember { mutableStateOf("") }

    val quantity = quantityText.toDoubleOrNull() ?: 0.0
    val price = priceText.toDoubleOrNull() ?: 0.0
    val charges = chargesText.toDoubleOrNull() ?: 0.0
    val totalAmount = (quantity * price) + charges

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SpendWiseBackground)
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            // Header with Back Arrow
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
                    modifier = Modifier.testTag("back_stock_investment_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = SpendWiseOnBackground
                    )
                }

                Text(
                    text = "Add Stock Investment",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = SpendWiseOnBackground,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Form Fields
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Stock Search / Name Input
                Text(
                    text = "Stock / Asset Name",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = SpendWiseOnBackground
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = stockName,
                    onValueChange = { stockName = it },
                    placeholder = { Text("e.g. Reliance Industries", color = Color(0xFF53433F)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = SpendWisePrimary
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("stock_name_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SpendWisePrimary,
                        unfocusedBorderColor = SpendWiseSurfaceVariant,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Symbol Field
                Text(
                    text = "Symbol / Ticker",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = SpendWiseOnBackground
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = stockSymbol,
                    onValueChange = { stockSymbol = it.uppercase() },
                    placeholder = { Text("e.g. RELIANCE", color = Color(0xFF53433F)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("stock_symbol_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SpendWisePrimary,
                        unfocusedBorderColor = SpendWiseSurfaceVariant,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // BUY / SELL Toggle
                Text(
                    text = "Transaction Type",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = SpendWiseOnBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(SpendWiseSurfaceContainerLow)
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (transactionType == TransactionType.BUY) SpendWisePrimary else Color.Transparent)
                            .clickable { transactionType = TransactionType.BUY }
                            .padding(vertical = 10.dp)
                            .testTag("type_buy_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "BUY",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (transactionType == TransactionType.BUY) Color.White else Color(0xFF53433F)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (transactionType == TransactionType.SELL) SpendWiseError else Color.Transparent)
                            .clickable { transactionType = TransactionType.SELL }
                            .padding(vertical = 10.dp)
                            .testTag("type_sell_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SELL",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (transactionType == TransactionType.SELL) Color.White else Color(0xFF53433F)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Quantity & Price per Share Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Quantity",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = SpendWiseOnBackground
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = quantityText,
                            onValueChange = { quantityText = it },
                            placeholder = { Text("10", color = Color(0xFF53433F)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("stock_quantity_input"),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SpendWisePrimary,
                                unfocusedBorderColor = SpendWiseSurfaceVariant,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            singleLine = true
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Price per Share (₹)",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = SpendWiseOnBackground
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = priceText,
                            onValueChange = { priceText = it },
                            placeholder = { Text("1450", color = Color(0xFF53433F)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("stock_price_input"),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SpendWisePrimary,
                                unfocusedBorderColor = SpendWiseSurfaceVariant,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Date & Charges Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Date",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = SpendWiseOnBackground
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = dateText,
                            onValueChange = { dateText = it },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.CalendarToday,
                                    contentDescription = "Date",
                                    tint = SpendWisePrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("stock_date_input"),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SpendWisePrimary,
                                unfocusedBorderColor = SpendWiseSurfaceVariant,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            singleLine = true
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Charges (Optional ₹)",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = SpendWiseOnBackground
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = chargesText,
                            onValueChange = { chargesText = it },
                            placeholder = { Text("0.00", color = Color(0xFF53433F)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("stock_charges_input"),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SpendWisePrimary,
                                unfocusedBorderColor = SpendWiseSurfaceVariant,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Notes Field
                Text(
                    text = "Notes",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = SpendWiseOnBackground
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = notesText,
                    onValueChange = { notesText = it },
                    placeholder = { Text("Add any details about this transaction...", color = Color(0xFF53433F)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .testTag("stock_notes_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SpendWisePrimary,
                        unfocusedBorderColor = SpendWiseSurfaceVariant,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Calculation Summary Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("stock_calculation_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = SpendWiseSurfaceContainerLow),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Calculation:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF53433F)
                            )
                            Text(
                                text = "${quantity.toInt()} Shares @ ₹${SpendWiseRepository.formatCurrencyPlain(price)}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = SpendWiseOnBackground
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        androidx.compose.material3.HorizontalDivider(color = SpendWiseSurfaceVariant)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total Amount",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SpendWiseOnBackground
                            )
                            Text(
                                text = SpendWiseRepository.formatCurrency(totalAmount),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = SpendWisePrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Fixed Submit Button at bottom
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
                    repository.addStockInvestment(
                        symbol = if (stockSymbol.isNotBlank()) stockSymbol else "STOCK",
                        name = if (stockName.isNotBlank()) stockName else "Stock Investment",
                        type = transactionType,
                        quantity = quantity,
                        pricePerShare = price,
                        date = dateText,
                        charges = charges,
                        notes = notesText,
                        assetClass = AssetClass.STOCKS
                    )
                    onSubmitSuccess()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_stock_investment_button"),
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
                    text = "Add Investment",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
