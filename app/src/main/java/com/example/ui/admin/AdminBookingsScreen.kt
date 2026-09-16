package com.example.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun AdminBookingsScreen() {
    val bookings by LoadGoRepository.bookings.collectAsState()
    var selectedBookingForStatusChange by remember { mutableStateOf<Booking?>(null) }

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
                    text = "Manage Bookings",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "बुकिंग्स प्रबंधन • Live order lifecycle & status simulator",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = LoadGoBlueLight
            ) {
                Text(
                    text = "${bookings.size} Orders",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = LoadGoBlueDark,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .testTag("admin_bookings_list")
        ) {
            items(bookings, key = { it.bookingId }) { booking ->
                AdminBookingCard(
                    booking = booking,
                    onChangeStatus = { selectedBookingForStatusChange = booking }
                )
            }
        }
    }

    // Status Change Dialog for Demo / Testing
    if (selectedBookingForStatusChange != null) {
        val b = selectedBookingForStatusChange!!
        AlertDialog(
            onDismissRequest = { selectedBookingForStatusChange = null },
            title = {
                Text("Change Status for ${b.bookingId}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Column {
                    Text(
                        text = "Select new booking status to test live synchronization across roles:",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    BookingStatus.values().forEach { status ->
                        val isCurrent = b.status == status
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isCurrent) LoadGoBlueLight else SurfaceBg,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    LoadGoRepository.adminUpdateBookingStatus(b.bookingId, status)
                                    selectedBookingForStatusChange = null
                                }
                                .testTag("set_status_${status.name}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${status.label} (${status.hindiLabel})",
                                    fontSize = 13.sp,
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isCurrent) LoadGoBlueDark else TextPrimary
                                )
                                if (isCurrent) {
                                    Text("✓ Current", fontSize = 11.sp, color = LoadGoBlueDark, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedBookingForStatusChange = null }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun AdminBookingCard(
    booking: Booking,
    onChangeStatus: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_booking_item_${booking.bookingId}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: ID + Status + Fare
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = booking.bookingId,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = LoadGoBlueDark
                    )
                    Text(
                        text = booking.date,
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
                BookingStatusChip(status = booking.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            LocationRouteDisplay(pickup = booking.pickup, drop = booking.drop)

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = DividerColor)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Customer: ${booking.customerName} (${booking.customerPhone})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Driver: ${booking.driverName ?: "Unassigned"} ${booking.driverVehicleNumber?.let { "($it)" } ?: ""}",
                        fontSize = 12.sp,
                        color = if (booking.driverName != null) StatusGreen else Color(0xFFE65100),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Vehicle: ${booking.vehicleType} • Goods: ${booking.goodsDescription}",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹${booking.fare}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LoadGoBlueDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onChangeStatus,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_change_status_${booking.bookingId}"),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Change Status (Demo / Test)", fontSize = 12.sp)
            }
        }
    }
}
