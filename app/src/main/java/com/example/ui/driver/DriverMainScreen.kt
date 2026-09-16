package com.example.ui.driver

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

enum class DriverTab(val label: String, val hindiLabel: String, val icon: ImageVector) {
    HOME("Home", "होम", Icons.Default.Home),
    REQUESTS("Requests", "अनुरोध", Icons.Default.Notifications),
    EARNINGS("Earnings", "कमाई", Icons.Default.CurrencyRupee),
    PROFILE("Profile", "प्रोफ़ाइल", Icons.Default.Person)
}

@Composable
fun DriverMainScreen(
    onSwitchRole: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(DriverTab.HOME) }

    Scaffold(
        topBar = {
            LoadGoTopBar(
                title = "LoadGo Partner",
                subtitle = "Driver Console • डिलीवरी पार्टनर",
                currentRoleName = "Driver",
                onSwitchRole = onSwitchRole
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                DriverTab.values().forEach { tab ->
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
                        modifier = Modifier.testTag("driver_nav_${tab.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                DriverTab.HOME -> DriverHomeScreen(
                    onNavigateToRequests = { selectedTab = DriverTab.REQUESTS }
                )
                DriverTab.REQUESTS -> DriverRequestsScreen(
                    onNavigateToHome = { selectedTab = DriverTab.HOME }
                )
                DriverTab.EARNINGS -> DriverEarningsScreen()
                DriverTab.PROFILE -> DriverProfileScreen(
                    onSwitchRole = onSwitchRole
                )
            }
        }
    }
}
