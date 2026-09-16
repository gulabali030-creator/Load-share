package com.example.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.model.VehicleTypeInfo
import com.example.ui.components.BookingStatusChip
import com.example.ui.components.LocationRouteDisplay
import com.example.ui.theme.*

@Composable
fun CustomerHomeScreen(
    onNavigateToBookings: () -> Unit
) {
    val currentCustomer by LoadGoRepository.currentCustomer.collectAsState()
    val vehicleTypes by LoadGoRepository.vehicleTypes.collectAsState()
    val allBookings by LoadGoRepository.bookings.collectAsState()

    // Find active booking for current customer
    val activeBooking = allBookings.firstOrNull {
        it.customerId == (currentCustomer?.id ?: "") &&
                it.status != BookingStatus.TRIP_COMPLETED &&
                it.status != BookingStatus.CANCELLED
    }

    var pickup by remember { mutableStateOf("Karol Bagh Market, New Delhi") }
    var drop by remember { mutableStateOf("Chandni Chowk Wholesale, Delhi") }
    var selectedVehicle by remember { mutableStateOf(vehicleTypes.find { it.id == "tata_ace" } ?: vehicleTypes.first()) }
    var goodsDesc by remember { mutableStateOf("Cartons & retail supplies") }
    var showBookingConfirmationDialog by remember { mutableStateOf(false) }
    var showSuccessSnackbar by remember { mutableStateOf(false) }
    var distanceKm by remember { mutableStateOf(10) }

    val estimatedFare = selectedVehicle.baseFare + (distanceKm * selectedVehicle.perKmRate)

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Welcome Banner
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = LoadGoBlue,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Namaste, ${currentCustomer?.name ?: "Customer"}! 🙏",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Book goods vehicle in 60 seconds • आसान ट्रांसपोर्ट",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }

        // Active Booking Tracker Card (if any)
        if (activeBooking != null) {
            Spacer(modifier = Modifier.height(16.dp))
            ActiveBookingCard(
                booking = activeBooking,
                onCancel = {
                    LoadGoRepository.cancelBooking(activeBooking.bookingId)
                },
                onViewAll = onNavigateToBookings
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Book a Vehicle Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "Book a Vehicle",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "गाड़ी बुक करें • Select route & mini truck",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = LoadGoAmberLight
                    ) {
                        Text(
                            text = "Est. Dist: ${distanceKm} km",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE65100),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Pickup Input
                OutlinedTextField(
                    value = pickup,
                    onValueChange = { pickup = it },
                    label = { Text("Pickup Location / पिकअप स्थान") },
                    placeholder = { Text("Enter shop/flat address") },
                    leadingIcon = {
                        Icon(Icons.Default.MyLocation, contentDescription = null, tint = StatusGreen)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("customer_pickup_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Drop Input
                OutlinedTextField(
                    value = drop,
                    onValueChange = { drop = it },
                    label = { Text("Drop Location / ड्राप स्थान") },
                    placeholder = { Text("Enter destination address") },
                    leadingIcon = {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = StatusRed)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("customer_drop_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Goods Description
                OutlinedTextField(
                    value = goodsDesc,
                    onValueChange = { goodsDesc = it },
                    label = { Text("Goods Description / सामान का विवरण") },
                    placeholder = { Text("e.g. Furniture, 15 boxes, Hardware") },
                    leadingIcon = {
                        Icon(Icons.Default.Inventory2, contentDescription = null, tint = LoadGoBlue)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("customer_goods_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Vehicle Selector
                Text(
                    text = "Select Vehicle / गाड़ी चुनें",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Pick the vehicle size that fits your load:",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                // Horizontal scroll of vehicle cards
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(vehicleTypes) { vehicle ->
                        val isSelected = vehicle.id == selectedVehicle.id
                        VehicleSelectionCard(
                            vehicle = vehicle,
                            isSelected = isSelected,
                            estimatedFare = vehicle.baseFare + (distanceKm * vehicle.perKmRate),
                            onClick = { selectedVehicle = vehicle }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Distance Slider for quick testing
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Trip Distance: $distanceKm km",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )
                    Text(
                        text = "Base ₹${selectedVehicle.baseFare} + ₹${selectedVehicle.perKmRate}/km",
                        fontSize = 11.sp,
                        color = LoadGoBlueDark,
                        fontWeight = FontWeight.Medium
                    )
                }
                Slider(
                    value = distanceKm.toFloat(),
                    onValueChange = { distanceKm = it.toInt() },
                    valueRange = 2f..40f,
                    steps = 18,
                    colors = SliderDefaults.colors(
                        thumbColor = LoadGoBlue,
                        activeTrackColor = LoadGoBlue
                    ),
                    modifier = Modifier.testTag("distance_slider")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Estimated Fare Card & Book Now Button
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = LoadGoBlueLight,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Estimated Fare / अनुमानित किराया",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                fontWeight = FontWeight.Medium
                            )
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "₹$estimatedFare",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = LoadGoBlueDark
                                )
                                Text(
                                    text = " (Incl. GST)",
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                                )
                            }
                        }

                        Button(
                            onClick = { showBookingConfirmationDialog = true },
                            modifier = Modifier
                                .height(46.dp)
                                .testTag("book_vehicle_btn"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = LoadGoBlue)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Book Now",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Booking Confirmation Dialog
    if (showBookingConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showBookingConfirmationDialog = false },
            title = {
                Text(
                    text = "Confirm Booking / बुकिंग की पुष्टि",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = "Please verify your trip details before confirming:",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    LocationRouteDisplay(pickup = pickup, drop = drop)
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = DividerColor)
                    Spacer(modifier = Modifier.height(10.dp))
                    DetailRow(label = "Vehicle / गाड़ी", value = "${selectedVehicle.name} (${selectedVehicle.capacity})")
                    DetailRow(label = "Goods / सामान", value = goodsDesc.ifBlank { "General Cargo" })
                    DetailRow(label = "Estimated Fare / किराया", value = "₹$estimatedFare", isHighlight = true)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        LoadGoRepository.createBooking(
                            pickup = pickup,
                            drop = drop,
                            vehicleType = selectedVehicle.name,
                            goodsDescription = goodsDesc,
                            fare = estimatedFare,
                            distanceKm = distanceKm
                        )
                        showBookingConfirmationDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LoadGoBlue),
                    modifier = Modifier.testTag("confirm_booking_dialog_btn")
                ) {
                    Text("Confirm Booking")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBookingConfirmationDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun VehicleSelectionCard(
    vehicle: VehicleTypeInfo,
    isSelected: Boolean,
    estimatedFare: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(130.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) LoadGoBlue else DividerColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .testTag("vehicle_${vehicle.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) LoadGoBlueLight else SurfaceCard
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) LoadGoBlue else SurfaceBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalShipping,
                    contentDescription = null,
                    tint = if (isSelected) Color.White else LoadGoBlue,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = vehicle.name,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = TextPrimary
            )
            Text(
                text = vehicle.capacity,
                fontSize = 10.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (isSelected) LoadGoBlue else Color(0xFFECEFF1)
            ) {
                Text(
                    text = "₹$estimatedFare",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else LoadGoBlueDark,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun ActiveBookingCard(
    booking: Booking,
    onCancel: () -> Unit,
    onViewAll: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("active_booking_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                            .background(
                                if (booking.status == BookingStatus.TRIP_STARTED) Color(0xFF7B1FA2)
                                else LoadGoAmber
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ACTIVE TRIP • ${booking.bookingId}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        color = TextPrimary
                    )
                }
                BookingStatusChip(status = booking.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Step Progress tracker
            BookingProgressStepper(status = booking.status)

            Spacer(modifier = Modifier.height(12.dp))

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
                        text = "Vehicle: ${booking.vehicleType}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    if (booking.driverName != null) {
                        Text(
                            text = "Driver: ${booking.driverName} (${booking.driverVehicleNumber ?: ""})",
                            fontSize = 12.sp,
                            color = StatusGreen,
                            fontWeight = FontWeight.Medium
                        )
                    } else {
                        Text(
                            text = "Matching nearest driver partner...",
                            fontSize = 11.sp,
                            color = Color(0xFFE65100),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Text(
                    text = "₹${booking.fare}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LoadGoBlueDark
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (booking.status == BookingStatus.SEARCHING_DRIVER || booking.status == BookingStatus.DRIVER_ASSIGNED) {
                    OutlinedButton(
                        onClick = onCancel,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusRed),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("cancel_active_booking_btn")
                    ) {
                        Text("Cancel Trip", fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onViewAll,
                    colors = ButtonDefaults.buttonColors(containerColor = LoadGoBlue),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("View Trips", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun BookingProgressStepper(status: BookingStatus) {
    val steps = listOf(
        "Searching" to (status.ordinal >= BookingStatus.SEARCHING_DRIVER.ordinal),
        "Assigned" to (status.ordinal >= BookingStatus.DRIVER_ASSIGNED.ordinal),
        "Arriving" to (status.ordinal >= BookingStatus.DRIVER_ARRIVING.ordinal),
        "Started" to (status.ordinal >= BookingStatus.TRIP_STARTED.ordinal),
        "Completed" to (status.ordinal >= BookingStatus.TRIP_COMPLETED.ordinal)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, (label, isReached) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(if (isReached) LoadGoBlue else DividerColor),
                    contentAlignment = Alignment.Center
                ) {
                    if (isReached) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
                Text(
                    text = label,
                    fontSize = 9.sp,
                    color = if (isReached) LoadGoBlueDark else TextSecondary,
                    fontWeight = if (isReached) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            if (index < steps.lastIndex) {
                Box(
                    modifier = Modifier
                        .weight(0.6f)
                        .height(2.dp)
                        .background(if (steps[index + 1].second) LoadGoBlue else DividerColor)
                )
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, isHighlight: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = TextSecondary)
        Text(
            text = value,
            fontSize = if (isHighlight) 15.sp else 12.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.SemiBold,
            color = if (isHighlight) LoadGoBlueDark else TextPrimary
        )
    }
}
