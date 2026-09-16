package com.example.ui.admin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.components.LoadGoTopBar
import com.example.ui.theme.LoadGoBlue

enum class AdminTab(val label: String, val hindiLabel: String, val icon: ImageVector) {
    DASHBOARD("Dashboard", "डैशबोर्ड", Icons.Default.Dashboard),
    DRIVERS("Drivers", "ड्राइवर", Icons.Default.LocalShipping),
    CUSTOMERS("Customers", "ग्राहक", Icons.Default.People),
    BOOKINGS("Bookings", "बुकिंग्स", Icons.Default.ReceiptLong)
}

@Composable
fun AdminMainScreen(
    onSwitchRole: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(AdminTab.DASHBOARD) }
    var showPricingScreen by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            LoadGoTopBar(
                title = "LoadGo Admin",
                subtitle = "Platform Control & Fleet Operations",
                currentRoleName = "Admin",
                onSwitchRole = onSwitchRole,
                actions = {
                    IconButton(
                        onClick = { showPricingScreen = !showPricingScreen },
                        modifier = Modifier.testTag("admin_topbar_pricing_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PriceChange,
                            contentDescription = "Pricing Settings"
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (!showPricingScreen) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp
                ) {
                    AdminTab.values().forEach { tab ->
                        val isSelected = selectedTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { selectedTab = tab },
                            icon = { Icon(imageVector = tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = LoadGoBlue,
                                selectedTextColor = LoadGoBlue,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier.testTag("admin_nav_${tab.name.lowercase()}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (showPricingScreen) {
                AdminPricingScreen(
                    onBack = { showPricingScreen = false }
                )
            } else {
                when (selectedTab) {
                    AdminTab.DASHBOARD -> AdminDashboardScreen(
                        onNavigateToPricing = { showPricingScreen = true },
                        onNavigateToDrivers = { selectedTab = AdminTab.DRIVERS },
                        onNavigateToBookings = { selectedTab = AdminTab.BOOKINGS }
                    )
                    AdminTab.DRIVERS -> AdminDriversScreen()
                    AdminTab.CUSTOMERS -> AdminCustomersScreen()
                    AdminTab.BOOKINGS -> AdminBookingsScreen()
                }
            }
        }
    }
}
