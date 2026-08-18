package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.theme.SpendWiseBackground
import com.example.ui.theme.SpendWiseOnBackground
import com.example.ui.theme.SpendWisePrimary
import com.example.ui.theme.SpendWisePrimaryContainer
import com.example.ui.theme.SpendWiseSurfaceContainerHigh
import com.example.ui.theme.SpendWiseSurfaceVariant

const val DEFAULT_AVATAR_URL = "https://lh3.googleusercontent.com/aida-public/AB6AXuDN9mQJyjTiKqjCE70MSYQTKIRXEJyoenSXBphUvT2coxR600nsNJ8YeIyUO-d1Y7dZIxDqkE1XBnTXrlSoWsA34l4P0oRmieTyHg_m6Yj7Dm_hfmaiMsfZzMe8JTxCAFAfJjjMIdftlPaon1qXLWps4h1XfqE8UG7iTjKGIIkJyC-RjK5mQVfZSTarDhymOCrv4JUTT_mkIHL6WMRSLEmsbErhQE7l3idCFuRMaEdend73SONi1PzK0A"

@Composable
fun SpendWiseTopBar(
    title: String = "Alex Riviera",
    subtitle: String = "GOOD MORNING",
    showAvatar: Boolean = true,
    avatarUrl: String = DEFAULT_AVATAR_URL,
    onAvatarClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(SpendWiseBackground)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.testTag("top_bar_title_column")) {
            Text(
                text = subtitle.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                fontSize = 11.sp,
                color = SpendWisePrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                color = SpendWiseOnBackground
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, SpendWiseSurfaceVariant, CircleShape)
                    .testTag("sync_refresh_button")
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    tint = SpendWisePrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            if (showAvatar) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(SpendWisePrimaryContainer)
                        .border(2.dp, Color.White, CircleShape)
                        .clickable { onAvatarClick() }
                        .testTag("user_profile_avatar"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "AR",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = SpendWisePrimary
                    )
                }
            }
        }
    }
}
