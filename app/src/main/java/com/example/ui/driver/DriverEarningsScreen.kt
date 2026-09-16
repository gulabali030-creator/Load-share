package com.example.ui.driver

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LoadGoRepository
import com.example.model.BookingStatus
import com.example.ui.components.StatMetricCard
import com.example.ui.theme.*

@Composable
fun DriverEarningsScreen() {
    val driver by LoadGoRepository.currentDriver.collectAsState()
    val allBookings by LoadGoRepository.bookings.collectAsState()

    // Completed trips by this driver
    val completedTripsList = allBookings.filter {
        it.driverId == driver.id && it.status == BookingStatus.TRIP_COMPLETED
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "Earnings & Payouts",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = "ड्राइवर कमाई • Track daily payouts and completed trips",
            fontSize = 12.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Large Total Balance Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = LoadGoBlue),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Total Lifetime Earnings / कुल कमाई",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "₹${driver.totalEarnings}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Linked UPI: ${driver.upiId}",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "Instant Payout",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Breakdown Cards: Today, Weekly, Completed Trips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatMetricCard(
                title = "Today",
                hindiTitle = "आज की कमाई",
                value = "₹${driver.todayEarnings}",
                icon = Icons.Default.Today,
                iconBg = LoadGoBlueLight,
                iconTint = LoadGoBlue,
                modifier = Modifier.weight(1f)
            )

            StatMetricCard(
                title = "This Week",
                hindiTitle = "इस हफ्ते की कमाई",
                value = "₹${driver.weeklyEarnings}",
                icon = Icons.Default.DateRange,
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
                title = "Trips Finished",
                hindiTitle = "कुल ट्रिप्स",
                value = "${driver.completedTrips}",
                icon = Icons.Default.DoneAll,
                iconBg = StatusGreenLight,
                iconTint = StatusGreen,
                modifier = Modifier.weight(1f)
            )

            StatMetricCard(
                title = "Average / Trip",
                hindiTitle = "औसत किराया",
                value = if (driver.completedTrips > 0) "₹${driver.totalEarnings / driver.completedTrips}" else "₹550",
                icon = Icons.Default.TrendingUp,
                iconBg = Color(0xFFEDE7F6),
                iconTint = Color(0xFF512DA8),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Completed trips log
        Text(
            text = "Completed Trips / हाल की ट्रिप्स",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (completedTripsList.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No completed trips yet in this session.",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                completedTripsList.forEach { trip ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "${trip.bookingId} • ${trip.vehicleType}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = TextPrimary
                                )
                                Text(
                                    text = trip.date,
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                            Text(
                                text = "+₹${trip.fare}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = StatusGreen
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
