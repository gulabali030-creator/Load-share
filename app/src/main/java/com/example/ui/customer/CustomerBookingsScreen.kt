package com.example.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.model.Booking
import com.example.model.BookingStatus
import com.example.ui.components.BookingStatusChip
import com.example.ui.components.LocationRouteDisplay
import com.example.ui.theme.*

@Composable
fun CustomerBookingsScreen() {
    val currentCustomer by LoadGoRepository.currentCustomer.collectAsState()
    val allBookings by LoadGoRepository.bookings.collectAsState()

    // Filter bookings belonging to this customer
    val customerBookings = allBookings.filter { it.customerId == (currentCustomer?.id ?: "") }

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
                    text = "My Bookings",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "मेरी बुकिंग्स • History of all trips",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = LoadGoBlueLight
            ) {
                Text(
                    text = "${customerBookings.size} Trips",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = LoadGoBlueDark,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (customerBookings.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Inbox,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No Bookings Yet",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Book a mini truck from the Home screen!",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("customer_bookings_list")
            ) {
                items(customerBookings, key = { it.bookingId }) { booking ->
                    CustomerBookingItemCard(booking = booking)
                }
            }
        }
    }
}

@Composable
private fun CustomerBookingItemCard(booking: Booking) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("booking_item_${booking.bookingId}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: ID + Status + Date
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

            Spacer(modifier = Modifier.height(12.dp))

            LocationRouteDisplay(pickup = booking.pickup, drop = booking.drop)

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = DividerColor)
            Spacer(modifier = Modifier.height(10.dp))

            // Vehicle and Goods info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocalShipping,
                            contentDescription = null,
                            tint = LoadGoBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = booking.vehicleType,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                    Text(
                        text = booking.goodsDescription,
                        fontSize = 11.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    if (booking.driverName != null) {
                        Text(
                            text = "Driver: ${booking.driverName} (${booking.driverVehicleNumber ?: ""})",
                            fontSize = 11.sp,
                            color = StatusGreen,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Total Fare",
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = "₹${booking.fare}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LoadGoBlueDark
                    )
                }
            }

            // If active and searching/assigned, offer cancel
            if (booking.status == BookingStatus.SEARCHING_DRIVER || booking.status == BookingStatus.DRIVER_ASSIGNED) {
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = { LoadGoRepository.cancelBooking(booking.bookingId) },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusRed),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("cancel_booking_btn_${booking.bookingId}"),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    Text("Cancel Booking / बुकिंग रद्द करें", fontSize = 12.sp)
                }
            }
        }
    }
}
