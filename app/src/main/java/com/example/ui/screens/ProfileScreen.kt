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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.OutlinedTextField
import com.example.data.SpendWiseRepository
import com.example.data.api.ApiClient
import com.example.navigation.Screen
import com.example.ui.components.BottomNavBar
import com.example.ui.theme.SpendWiseBackground
import com.example.ui.theme.SpendWiseError
import com.example.ui.theme.SpendWiseErrorContainer
import com.example.ui.theme.SpendWiseOnBackground
import com.example.ui.theme.SpendWiseOnErrorContainer
import com.example.ui.theme.SpendWisePrimary
import com.example.ui.theme.SpendWisePrimaryContainer
import com.example.ui.theme.SpendWiseSecondary
import com.example.ui.theme.SpendWiseSecondaryContainer
import com.example.ui.theme.SpendWiseSuccess
import com.example.ui.theme.SpendWiseSuccessContainer
import com.example.ui.theme.SpendWiseSurfaceContainerLow
import com.example.ui.theme.SpendWiseSurfaceVariant

@Composable
fun ProfileScreen(
    repository: SpendWiseRepository,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val portfolioSummary by repository.portfolioSummary.collectAsState()
    val goals by repository.goals.collectAsState()

    val isApiConnected by repository.isApiConnected.collectAsState()
    val isSyncing by repository.isSyncing.collectAsState()
    val apiStatusMessage by repository.apiStatusMessage.collectAsState()

    var notificationsEnabled by remember { mutableStateOf(true) }
    var biometricEnabled by remember { mutableStateOf(true) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showServerConfigDialog by remember { mutableStateOf(false) }
    var customServerUrl by remember { mutableStateOf(ApiClient.baseUrl) }

    if (showServerConfigDialog) {
        AlertDialog(
            onDismissRequest = { showServerConfigDialog = false },
            title = {
                Text(
                    text = "Backend API Endpoint",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = SpendWiseOnBackground
                )
            },
            text = {
                Column {
                    Text(
                        text = "Configure Express / Node.js backend server URL:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF475569)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = customServerUrl,
                        onValueChange = { customServerUrl = it },
                        label = { Text("API Base URL") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Emulator: http://10.0.2.2:5000/api/\n• Local Network: http://<YOUR_IP>:5000/api/\n• MongoDB Cluster: biofugitive.ah5xtvm.mongodb.net",
                        fontSize = 12.sp,
                        color = SpendWisePrimary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        ApiClient.updateBaseUrl(customServerUrl)
                        repository.refreshAllData()
                        showServerConfigDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SpendWisePrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Save & Sync", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showServerConfigDialog = false }) {
                    Text("Cancel", color = SpendWisePrimary)
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    text = "Sign Out",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = SpendWiseOnBackground
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to sign out of SpendWise?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF475569)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SpendWiseError,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("confirm_logout_button")
                ) {
                    Text("Sign Out", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false },
                    modifier = Modifier.testTag("cancel_logout_button")
                ) {
                    Text("Cancel", color = SpendWisePrimary)
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SpendWiseBackground)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onNavigate(Screen.Dashboard.route) },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, SpendWiseSurfaceVariant, CircleShape)
                        .testTag("profile_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = SpendWisePrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = "My Profile",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = SpendWiseOnBackground,
                    modifier = Modifier.testTag("profile_title")
                )

                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, SpendWiseSurfaceVariant, CircleShape)
                        .testTag("profile_edit_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit Profile",
                        tint = SpendWisePrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = Screen.Profile.route,
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
            Spacer(modifier = Modifier.height(6.dp))

            // 1. Profile Hero Card (Sky Blue Theme)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_hero_card"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(SpendWisePrimaryContainer)
                            .border(3.dp, SpendWisePrimary.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "AR",
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                            color = SpendWisePrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Alex Riviera",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = SpendWiseOnBackground
                    )

                    Text(
                        text = "devamanoj07@gmail.com",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF475569)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(SpendWisePrimaryContainer)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = SpendWisePrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "PRO WEALTH",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SpendWisePrimary
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(SpendWiseSuccessContainer)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    tint = SpendWiseSuccess,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "KYC VERIFIED",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SpendWiseSuccess
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                    HorizontalDivider(color = SpendWiseSurfaceVariant.copy(alpha = 0.6f))
                    Spacer(modifier = Modifier.height(16.dp))

                    // Bento Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ProfileStatItem(
                            value = SpendWiseRepository.formatCurrency(portfolioSummary.totalValue),
                            label = "Net Portfolio"
                        )
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(36.dp)
                                .background(SpendWiseSurfaceVariant)
                        )
                        ProfileStatItem(
                            value = "${goals.size}",
                            label = "Active Goals"
                        )
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(36.dp)
                                .background(SpendWiseSurfaceVariant)
                        )
                        ProfileStatItem(
                            value = "98%",
                            label = "Health Score"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Personal & Account Details Section
            Text(
                text = "Account Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SpendWiseOnBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_account_details_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ProfileDetailRow(label = "Phone Number", value = "+91 98765 43210")
                    HorizontalDivider(color = SpendWiseSurfaceVariant.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 10.dp))
                    ProfileDetailRow(label = "PAN Number", value = "ABCDE••••F")
                    HorizontalDivider(color = SpendWiseSurfaceVariant.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 10.dp))
                    ProfileDetailRow(label = "Risk Profile", value = "Moderate Growth")
                    HorizontalDivider(color = SpendWiseSurfaceVariant.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 10.dp))
                    ProfileDetailRow(label = "Member Since", value = "August 2024")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Linked Bank Accounts
            Text(
                text = "Linked Bank Accounts",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SpendWiseOnBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_linked_banks_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    LinkedBankItem(
                        bankName = "HDFC Bank",
                        accountNumber = "•••• 9012",
                        tag = "PRIMARY SALARY",
                        isPrimary = true
                    )
                    HorizontalDivider(color = SpendWiseSurfaceVariant.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 12.dp))
                    LinkedBankItem(
                        bankName = "State Bank of India",
                        accountNumber = "•••• 3341",
                        tag = "INVESTMENT DEMAT",
                        isPrimary = false
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. App Preferences & Security
            Text(
                text = "Preferences & Security",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SpendWiseOnBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_preferences_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(SpendWisePrimaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Notifications,
                                    contentDescription = null,
                                    tint = SpendWisePrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Push Notifications",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SpendWiseOnBackground
                                )
                                Text(
                                    text = "SIP alerts and budget limits",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF475569)
                                )
                            }
                        }

                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = SpendWisePrimary,
                                uncheckedThumbColor = Color(0xFF94A3B8),
                                uncheckedTrackColor = SpendWiseSurfaceContainerLow
                            ),
                            modifier = Modifier.testTag("switch_notifications")
                        )
                    }

                    HorizontalDivider(color = SpendWiseSurfaceVariant.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(SpendWisePrimaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Fingerprint,
                                    contentDescription = null,
                                    tint = SpendWisePrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Biometric Lock",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SpendWiseOnBackground
                                )
                                Text(
                                    text = "Fingerprint / Face ID login",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF475569)
                                )
                            }
                        }

                        Switch(
                            checked = biometricEnabled,
                            onCheckedChange = { biometricEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = SpendWisePrimary,
                                uncheckedThumbColor = Color(0xFF94A3B8),
                                uncheckedTrackColor = SpendWiseSurfaceContainerLow
                            ),
                            modifier = Modifier.testTag("switch_biometric")
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Database & Cloud Backend (MongoDB Atlas)
            Text(
                text = "Database & Cloud Backend",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SpendWiseOnBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_database_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ProfileMenuRow(
                        title = "MongoDB Atlas Cluster",
                        subtitle = if (isApiConnected) "Connected (biofugitive)" else "Connecting / Local Mode",
                        icon = Icons.Filled.Storage,
                        onClick = { showServerConfigDialog = true }
                    )
                    HorizontalDivider(color = SpendWiseSurfaceVariant.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 10.dp))
                    ProfileMenuRow(
                        title = "Backend API URL",
                        subtitle = ApiClient.baseUrl,
                        icon = Icons.Filled.Dns,
                        onClick = { showServerConfigDialog = true }
                    )
                    HorizontalDivider(color = SpendWiseSurfaceVariant.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 10.dp))
                    ProfileMenuRow(
                        title = if (isSyncing) "Syncing..." else "Sync Data Now",
                        subtitle = apiStatusMessage,
                        icon = Icons.Filled.Sync,
                        onClick = { repository.refreshAllData() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 6. Reports & Advisory
            Text(
                text = "Reports & Support",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SpendWiseOnBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_reports_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseSurfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ProfileMenuRow(
                        title = "Annual Tax & P&L Statement",
                        subtitle = "Download FY 2025-26 report",
                        icon = Icons.Filled.Description,
                        onClick = { }
                    )
                    HorizontalDivider(color = SpendWiseSurfaceVariant.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 10.dp))
                    ProfileMenuRow(
                        title = "Wealth Advisor Support",
                        subtitle = "Chat with a certified planner",
                        icon = Icons.Filled.HelpOutline,
                        onClick = { }
                    )
                    HorizontalDivider(color = SpendWiseSurfaceVariant.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 10.dp))
                    ProfileMenuRow(
                        title = "Security & Privacy Policy",
                        subtitle = "256-bit bank grade encryption",
                        icon = Icons.Filled.Security,
                        onClick = { }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 6. Sign Out Button
            OutlinedButton(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("profile_logout_button"),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SpendWiseError.copy(alpha = 0.5f)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = SpendWiseErrorContainer.copy(alpha = 0.3f),
                    contentColor = SpendWiseError
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Sign Out",
                    tint = SpendWiseError,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sign Out",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SpendWiseError
                )
            }

            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}

@Composable
fun ProfileStatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = SpendWiseOnBackground
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF475569)
        )
    }
}

@Composable
fun ProfileDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF475569)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = SpendWiseOnBackground
        )
    }
}

@Composable
fun LinkedBankItem(
    bankName: String,
    accountNumber: String,
    tag: String,
    isPrimary: Boolean
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
                    .background(SpendWisePrimaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountBalance,
                    contentDescription = null,
                    tint = SpendWisePrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = bankName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SpendWiseOnBackground
                )
                Text(
                    text = accountNumber,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF475569)
                )
            }
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(if (isPrimary) SpendWisePrimaryContainer else SpendWiseSurfaceContainerLow)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = tag,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isPrimary) SpendWisePrimary else Color(0xFF475569)
            )
        }
    }
}

@Composable
fun ProfileMenuRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
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
                    .background(SpendWiseSurfaceContainerLow),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = SpendWisePrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = SpendWiseOnBackground
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF475569)
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = Color(0xFF94A3B8),
            modifier = Modifier.size(14.dp)
        )
    }
}
