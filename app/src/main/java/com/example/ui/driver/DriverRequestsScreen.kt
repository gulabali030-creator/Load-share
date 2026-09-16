package com.example.ui.driver

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.ui.components.LocationRouteDisplay
import com.example.ui.theme.*

@Composable
fun DriverRequestsScreen(
    onNavigateToHome: () -> Unit
) {
    val allBookings by LoadGoRepository.bookings.collectAsState()
    val driver by LoadGoRepository.currentDriver.collectAsState()

    // Searching driver requests
    val pendingRequests = allBookings.filter { it.status == BookingStatus.SEARCHING_DRIVER }

    var acceptedNotice by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Booking Requests",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "ट्रिप अनुरोध • Accept orders near you",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = LoadGoAmberLight
            ) {
                Text(
                    text = "${pendingRequests.size} Available",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE65100),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        if (acceptedNotice != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = StatusGreenLight,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = acceptedNotice ?: "",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = StatusGreen
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (!driver.isOnline) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = StatusRed)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "You are currently Offline",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = StatusRed
                        )
                        Text(
                            text = "Switch Online from the Home tab to accept new bookings.",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        if (pendingRequests.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.DoneAll,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No Pending Requests",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Switch to Customer role to create a new delivery order!",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("driver_requests_list")
            ) {
                items(pendingRequests, key = { it.bookingId }) { request ->
                    DriverRequestCard(
                        booking = request,
                        onAccept = {
                            LoadGoRepository.acceptBookingByDriver(request.bookingId)
                            acceptedNotice = "Accepted ${request.bookingId}! Head to Home tab to manage trip."
                            onNavigateToHome()
                        },
                        onReject = {
                            // Demo reject: ignore request
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DriverRequestCard(
    booking: Booking,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("request_item_${booking.bookingId}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: ID + Vehicle + Fare
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
                            .background(LoadGoBlueLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalShipping,
                            contentDescription = null,
                            tint = LoadGoBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = booking.vehicleType,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "Customer: ${booking.customerName}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹${booking.fare}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = LoadGoBlueDark
                    )
                    Text(
                        text = "${booking.distanceKm} km trip",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LocationRouteDisplay(pickup = booking.pickup, drop = booking.drop)

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = SurfaceBg,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Goods: ${booking.goodsDescription}",
                        fontSize = 12.sp,
                        color = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons: Reject / Accept
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onReject,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("reject_request_btn_${booking.bookingId}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusRed)
                ) {
                    Text("Reject", fontSize = 13.sp)
                }

                Button(
                    onClick = onAccept,
                    modifier = Modifier
                        .weight(1.4f)
                        .height(44.dp)
                        .testTag("accept_request_btn_${booking.bookingId}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LoadGoBlue)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Accept / स्वीकार करें", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
