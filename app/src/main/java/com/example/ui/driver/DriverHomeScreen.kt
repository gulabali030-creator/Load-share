package com.example.ui.driver

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LoadGoRepository
import com.example.model.Booking
import com.example.model.BookingStatus
import com.example.ui.components.BookingStatusChip
import com.example.ui.components.DriverVerificationChip
import com.example.ui.components.LocationRouteDisplay
import com.example.ui.components.StatMetricCard
import com.example.ui.theme.*

@Composable
fun DriverHomeScreen(
    onNavigateToRequests: () -> Unit
) {
    val driver by LoadGoRepository.currentDriver.collectAsState()
    val allBookings by LoadGoRepository.bookings.collectAsState()

    // Active trip assigned to this driver
    val activeTrip = allBookings.firstOrNull {
        it.driverId == driver.id &&
                (it.status == BookingStatus.DRIVER_ASSIGNED ||
                 it.status == BookingStatus.DRIVER_ARRIVING ||
                 it.status == BookingStatus.TRIP_STARTED)
    }

    // Pending requests available for pickup
    val pendingRequestsCount = allBookings.count { it.status == BookingStatus.SEARCHING_DRIVER }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Online / Offline Status Toggle Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (driver.isOnline) Color(0xFFE8F5E9) else Color(0xFFECEFF1)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(if (driver.isOnline) StatusGreen else Color.Gray)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (driver.isOnline) "You are ONLINE" else "You are OFFLINE",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (driver.isOnline) StatusGreen else TextSecondary
                        )
                        Text(
                            text = if (driver.isOnline) "Ready to receive goods trips" else "Turn on to get booking requests",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                Switch(
                    checked = driver.isOnline,
                    onCheckedChange = { LoadGoRepository.toggleDriverOnline(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = StatusGreen,
                        checkedTrackColor = Color(0xFFA5D6A7)
                    ),
                    modifier = Modifier.testTag("driver_online_switch")
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Driver Info header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = driver.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "${driver.vehicleNumber} • ${driver.vehicleType}",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }
            DriverVerificationChip(status = driver.verificationStatus)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Active Trip Card (if active trip exists)
        if (activeTrip != null) {
            ActiveTripManagementCard(trip = activeTrip)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Metrics: Today's Earnings & Completed Trips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatMetricCard(
                title = "Today's Earnings",
                hindiTitle = "आज की कमाई",
                value = "₹${driver.todayEarnings}",
                icon = Icons.Default.CurrencyRupee,
                iconBg = LoadGoBlueLight,
                iconTint = LoadGoBlue,
                modifier = Modifier.weight(1f)
            )

            StatMetricCard(
                title = "Completed Trips",
                hindiTitle = "पूर्ण ट्रिप्स",
                value = "${driver.completedTrips}",
                icon = Icons.Default.CheckCircle,
                iconBg = StatusGreenLight,
                iconTint = StatusGreen,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pending Requests Notification Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(LoadGoAmberLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = Color(0xFFE65100),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "New Trip Requests",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = if (pendingRequestsCount > 0)
                                "$pendingRequestsCount customer orders waiting for driver"
                            else "No pending customer requests at the moment",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                Button(
                    onClick = onNavigateToRequests,
                    colors = ButtonDefaults.buttonColors(containerColor = LoadGoBlue),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    modifier = Modifier.testTag("driver_view_requests_btn")
                ) {
                    Text("View (${pendingRequestsCount})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ActiveTripManagementCard(trip: Booking) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("driver_active_trip_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF7B1FA2))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ONGOING TRIP • ${trip.bookingId}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        color = Color(0xFF7B1FA2)
                    )
                }
                BookingStatusChip(status = trip.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Customer details
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = SurfaceBg,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Customer: ${trip.customerName}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Goods: ${trip.goodsDescription}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                    Text(
                        text = "₹${trip.fare}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LoadGoBlueDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LocationRouteDisplay(pickup = trip.pickup, drop = trip.drop)

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = DividerColor)
            Spacer(modifier = Modifier.height(14.dp))

            // Actions: Arriving / Start Trip / Complete Trip
            when (trip.status) {
                BookingStatus.DRIVER_ASSIGNED -> {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = { LoadGoRepository.driverArriving(trip.bookingId) },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("driver_arriving_btn"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Driver Arriving")
                        }
                        Button(
                            onClick = { LoadGoRepository.startTripByDriver(trip.bookingId) },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("driver_start_trip_btn"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = LoadGoBlue)
                        ) {
                            Text("Start Trip / यात्रा शुरू", fontWeight = FontWeight.Bold)
                        }
                    }
                }
                BookingStatus.DRIVER_ARRIVING -> {
                    Button(
                        onClick = { LoadGoRepository.startTripByDriver(trip.bookingId) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("driver_start_trip_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LoadGoBlue)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Start Trip / लोड हो गया, यात्रा शुरू करें", fontWeight = FontWeight.Bold)
                    }
                }
                BookingStatus.TRIP_STARTED -> {
                    Button(
                        onClick = { LoadGoRepository.completeTripByDriver(trip.bookingId) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("driver_complete_trip_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = StatusGreen)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Complete Trip & Collect ₹${trip.fare} / यात्रा पूर्ण करें", fontWeight = FontWeight.Bold)
                    }
                }
                else -> {}
            }
        }
    }
}
