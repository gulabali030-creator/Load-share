package com.example.ui.admin

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
import com.example.model.Driver
import com.example.model.DriverVerificationStatus
import com.example.ui.components.DriverVerificationChip
import com.example.ui.theme.*

@Composable
fun AdminDriversScreen() {
    val drivers by LoadGoRepository.driversList.collectAsState()
    var selectedDriverForDetails by remember { mutableStateOf<Driver?>(null) }

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
                    text = "Manage Drivers",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "ड्राइवर प्रबंधन • Verify partner KYC & fleet approvals",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = LoadGoBlueLight
            ) {
                Text(
                    text = "${drivers.size} Drivers",
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
                .testTag("admin_drivers_list")
        ) {
            items(drivers, key = { it.id }) { driver ->
                AdminDriverCard(
                    driver = driver,
                    onApprove = {
                        LoadGoRepository.adminUpdateDriverStatus(driver.id, DriverVerificationStatus.APPROVED)
                    },
                    onReject = {
                        LoadGoRepository.adminUpdateDriverStatus(driver.id, DriverVerificationStatus.REJECTED)
                    },
                    onViewDetails = {
                        selectedDriverForDetails = driver
                    }
                )
            }
        }
    }

    // View Details Dialog
    if (selectedDriverForDetails != null) {
        val d = selectedDriverForDetails!!
        AlertDialog(
            onDismissRequest = { selectedDriverForDetails = null },
            title = {
                Text("Driver Details: ${d.name}", fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text(text = "Partner ID: ${d.id}", fontSize = 12.sp, color = TextSecondary)
                    Text(text = "Mobile: +91 ${d.mobile}", fontSize = 13.sp)
                    Text(text = "Vehicle No: ${d.vehicleNumber}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = "Vehicle Model: ${d.vehicleType}", fontSize = 13.sp)
                    Text(text = "Licence No: ${d.licenseNumber}", fontSize = 13.sp)
                    Text(text = "Payout UPI: ${d.upiId}", fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "KYC Documents Status:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    d.documents.forEach { (doc, verified) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = doc, fontSize = 12.sp)
                            Text(
                                text = if (verified) "✓ Uploaded" else "Pending",
                                fontSize = 12.sp,
                                color = if (verified) StatusGreen else StatusRed
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { selectedDriverForDetails = null }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun AdminDriverCard(
    driver: Driver,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onViewDetails: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_driver_item_${driver.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(LoadGoBlueLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = LoadGoBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = driver.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "+91 ${driver.mobile} • ${driver.id}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
                DriverVerificationChip(status = driver.verificationStatus)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = SurfaceBg,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Vehicle: ${driver.vehicleType} (${driver.vehicleNumber})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                    Text(
                        text = if (driver.isOnline) "🟢 Online" else "⚪ Offline",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (driver.isOnline) StatusGreen else TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onViewDetails,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("admin_driver_view_${driver.id}"),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                ) {
                    Text("View Details", fontSize = 11.sp)
                }

                if (driver.verificationStatus != DriverVerificationStatus.APPROVED) {
                    Button(
                        onClick = onApprove,
                        colors = ButtonDefaults.buttonColors(containerColor = StatusGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("admin_driver_approve_${driver.id}"),
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                    ) {
                        Text("Approve", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                if (driver.verificationStatus != DriverVerificationStatus.REJECTED) {
                    Button(
                        onClick = onReject,
                        colors = ButtonDefaults.buttonColors(containerColor = StatusRed),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("admin_driver_reject_${driver.id}"),
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                    ) {
                        Text("Reject", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
