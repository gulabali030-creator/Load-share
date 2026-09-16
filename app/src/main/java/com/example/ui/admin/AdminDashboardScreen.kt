package com.example.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LoadGoRepository
import com.example.model.BookingStatus
import com.example.model.DriverVerificationStatus
import com.example.ui.components.StatMetricCard
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
    onNavigateToPricing: () -> Unit,
    onNavigateToDrivers: () -> Unit,
    onNavigateToBookings: () -> Unit
) {
    val customers by LoadGoRepository.customersList.collectAsState()
    val drivers by LoadGoRepository.driversList.collectAsState()
    val bookings by LoadGoRepository.bookings.collectAsState()

    val pendingDrivers = drivers.count { it.verificationStatus == DriverVerificationStatus.PENDING }
    val activeBookings = bookings.count {
        it.status == BookingStatus.SEARCHING_DRIVER ||
        it.status == BookingStatus.DRIVER_ASSIGNED ||
        it.status == BookingStatus.DRIVER_ARRIVING ||
        it.status == BookingStatus.TRIP_STARTED
    }
    val completedBookings = bookings.count { it.status == BookingStatus.TRIP_COMPLETED }
    val totalRevenue = bookings.filter { it.status == BookingStatus.TRIP_COMPLETED }.sumOf { it.fare }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Welcome Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Operations Dashboard",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "प्रशासन डैशबोर्ड • Platform metrics & operational control",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
            Button(
                onClick = onNavigateToPricing,
                colors = ButtonDefaults.buttonColors(containerColor = LoadGoBlue),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.testTag("admin_pricing_shortcut_btn")
            ) {
                Icon(Icons.Default.PriceChange, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Pricing", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Total Revenue Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Gross Platform Revenue / कुल राजस्व",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = "₹$totalRevenue",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFFFD54F),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    text = "Based on $completedBookings successfully delivered goods trips",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Metrics Grid (2 columns)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatMetricCard(
                title = "Total Customers",
                hindiTitle = "कुल ग्राहक",
                value = "${customers.size}",
                icon = Icons.Default.People,
                iconBg = LoadGoBlueLight,
                iconTint = LoadGoBlue,
                modifier = Modifier.weight(1f)
            )

            StatMetricCard(
                title = "Total Drivers",
                hindiTitle = "कुल ड्राइवर",
                value = "${drivers.size}",
                icon = Icons.Default.LocalShipping,
                iconBg = LoadGoAmberLight,
                iconTint = Color(0xFFE65100),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatMetricCard(
                title = "Pending Drivers",
                hindiTitle = "सत्यापन लंबित",
                value = "$pendingDrivers",
                icon = Icons.Default.PendingActions,
                iconBg = if (pendingDrivers > 0) Color(0xFFFFEBEE) else SurfaceBg,
                iconTint = if (pendingDrivers > 0) StatusRed else TextSecondary,
                modifier = Modifier.weight(1f)
            )

            StatMetricCard(
                title = "Active Bookings",
                hindiTitle = "सक्रिय ऑर्डर्स",
                value = "$activeBookings",
                icon = Icons.Default.ElectricBolt,
                iconBg = Color(0xFFE1F5FE),
                iconTint = Color(0xFF0288D1),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatMetricCard(
                title = "Completed Trips",
                hindiTitle = "सफल डिलीवरी",
                value = "$completedBookings",
                icon = Icons.Default.CheckCircle,
                iconBg = StatusGreenLight,
                iconTint = StatusGreen,
                modifier = Modifier.weight(1f)
            )

            StatMetricCard(
                title = "Total Fleet Types",
                hindiTitle = "गाड़ियों के प्रकार",
                value = "5 Categories",
                icon = Icons.Default.DirectionsTransit,
                iconBg = Color(0xFFEDE7F6),
                iconTint = Color(0xFF6A1B9A),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Operation Shortcuts
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Quick Operational Actions / त्वरित कार्य",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(
                        onClick = onNavigateToDrivers,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Manage Drivers", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = onNavigateToBookings,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Simulate Bookings", fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
