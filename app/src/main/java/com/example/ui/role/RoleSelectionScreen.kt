package com.example.ui.role

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserRole
import com.example.ui.components.LoadGoBrandLogo
import com.example.ui.theme.*

@Composable
fun RoleSelectionScreen(
    onRoleSelected: (UserRole) -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Brand Header
            LoadGoBrandLogo(
                showTagline = true,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Welcome Card with Gradient Accent
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = LoadGoBlue),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(LoadGoBlue, LoadGoBlueDark)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Welcome to LoadGo",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "लोडगो में आपका स्वागत है",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = LoadGoAmber,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "On-demand mini trucks & tempos for house shifting, shop supplies, and business logistics across the city.",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Select Your Role / अपना रोल चुनें",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Choose how you want to use the LoadGo platform today:",
                fontSize = 13.sp,
                color = TextSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Role Option 1: Customer
            RoleOptionCard(
                title = "Customer",
                hindiTitle = "ग्राहक (सामान भेजने वाले)",
                description = "Book mini trucks, Tata Ace & pickups with transparent rates and live driver tracking.",
                icon = Icons.Default.ShoppingCart,
                iconColor = LoadGoBlue,
                iconBg = LoadGoBlueLight,
                testTag = "role_customer_btn",
                onClick = { onRoleSelected(UserRole.CUSTOMER) }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Role Option 2: Driver
            RoleOptionCard(
                title = "Driver / Partner",
                hindiTitle = "ड्राइवर पार्टनर (गाड़ी वाले भाई)",
                description = "Receive instant trip requests, go online/offline, view daily earnings, and accept deliveries.",
                icon = Icons.Default.LocalShipping,
                iconColor = Color(0xFFE65100),
                iconBg = LoadGoAmberLight,
                testTag = "role_driver_btn",
                onClick = { onRoleSelected(UserRole.DRIVER) }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Role Option 3: Admin
            RoleOptionCard(
                title = "Admin",
                hindiTitle = "प्रशासक (कंट्रोल पैनल)",
                description = "Live marketplace dashboard, manage driver approvals, test booking statuses & configure pricing.",
                icon = Icons.Default.AdminPanelSettings,
                iconColor = Color(0xFF6A1B9A),
                iconBg = Color(0xFFF3E5F5),
                testTag = "role_admin_btn",
                onClick = { onRoleSelected(UserRole.ADMIN) }
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Value Props footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FeatureBadge(icon = Icons.Default.FlashOn, text = "Instant Booking")
                FeatureBadge(icon = Icons.Default.Security, text = "Verified Drivers")
                FeatureBadge(icon = Icons.Default.CurrencyRupee, text = "Affordable Fares")
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun RoleOptionCard(
    title: String,
    hindiTitle: String,
    description: String,
    icon: ImageVector,
    iconColor: Color,
    iconBg: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, DividerColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag(testTag),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
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
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                Text(
                    text = hindiTitle,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = iconColor,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Select $title",
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun FeatureBadge(icon: ImageVector, text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(LoadGoBlueLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = LoadGoBlue,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}
