package com.example.ui.driver

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.ui.components.DriverVerificationChip
import com.example.ui.theme.*

@Composable
fun DriverProfileScreen(
    onSwitchRole: () -> Unit
) {
    val driver by LoadGoRepository.currentDriver.collectAsState()
    var isEditing by remember { mutableStateOf(false) }

    var name by remember(driver) { mutableStateOf(driver.name) }
    var mobile by remember(driver) { mutableStateOf(driver.mobile) }
    var vehicleNumber by remember(driver) { mutableStateOf(driver.vehicleNumber) }
    var vehicleType by remember(driver) { mutableStateOf(driver.vehicleType) }
    var licenseNumber by remember(driver) { mutableStateOf(driver.licenseNumber) }
    var upiId by remember(driver) { mutableStateOf(driver.upiId) }
    var saveSuccess by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "Driver Partner Profile",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = "ड्राइवर प्रोफ़ाइल • Vehicle details & document verification",
            fontSize = 12.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(LoadGoAmberLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = null,
                        tint = Color(0xFFE65100),
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = driver.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "+91 ${driver.mobile} • ${driver.vehicleNumber}",
                    fontSize = 13.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))
                DriverVerificationChip(status = driver.verificationStatus)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Editable Details Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Vehicle & Driver Info",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    IconButton(
                        onClick = { isEditing = !isEditing },
                        modifier = Modifier.testTag("edit_driver_profile_btn")
                    ) {
                        Icon(
                            imageVector = if (isEditing) Icons.Default.Close else Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            tint = LoadGoBlue
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (isEditing) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Driver Name / नाम") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("driver_edit_name"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = mobile,
                        onValueChange = { if (it.length <= 10 && it.all { c -> c.isDigit() }) mobile = it },
                        label = { Text("Mobile Number / मोबाइल") },
                        prefix = { Text("+91 ") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("driver_edit_mobile"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = vehicleNumber,
                        onValueChange = { vehicleNumber = it.uppercase() },
                        label = { Text("Vehicle Registration / गाड़ी नंबर") },
                        placeholder = { Text("e.g. DL 1L AA 4589") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("driver_edit_veh_num"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = vehicleType,
                        onValueChange = { vehicleType = it },
                        label = { Text("Vehicle Type / गाड़ी का प्रकार") },
                        placeholder = { Text("Tata Ace, Pickup, Mini Truck, 8ft Truck, 14ft Truck") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("driver_edit_veh_type"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = licenseNumber,
                        onValueChange = { licenseNumber = it },
                        label = { Text("Driving Licence Number / ड्राइविंग लाइसेंस") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("driver_edit_license"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = upiId,
                        onValueChange = { upiId = it },
                        label = { Text("Bank / UPI ID for Payouts / यूपीआई आईडी") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("driver_edit_upi"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            LoadGoRepository.updateDriverProfile(
                                name = name,
                                mobile = mobile,
                                vehicleNumber = vehicleNumber,
                                vehicleType = vehicleType,
                                licenseNumber = licenseNumber,
                                upiId = upiId
                            )
                            isEditing = false
                            saveSuccess = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("save_driver_profile_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LoadGoBlue)
                    ) {
                        Text("Save Details / सुरक्षित करें", fontWeight = FontWeight.Bold)
                    }
                } else {
                    DriverDetailRow(label = "Driver Name / नाम", value = driver.name)
                    DriverDetailRow(label = "Mobile / मोबाइल", value = "+91 ${driver.mobile}")
                    DriverDetailRow(label = "Vehicle Number / गाड़ी नंबर", value = driver.vehicleNumber)
                    DriverDetailRow(label = "Vehicle Type / प्रकार", value = driver.vehicleType)
                    DriverDetailRow(label = "Driving Licence / लाइसेंस", value = driver.licenseNumber)
                    DriverDetailRow(label = "Bank UPI / भुगतान", value = driver.upiId)
                }

                if (saveSuccess) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "✓ Driver profile updated successfully",
                        color = StatusGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Document Upload Placeholders Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "KYC Documents / दस्तावेज़ सत्यापन",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Tap any document to toggle uploaded state (Demo Mode)",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                val docList = listOf(
                    "Driving Licence",
                    "Aadhaar",
                    "PAN",
                    "RC",
                    "Insurance"
                )

                docList.forEach { doc ->
                    val isUploaded = driver.documents[doc] ?: false
                    DocumentItemRow(
                        docName = doc,
                        isUploaded = isUploaded,
                        onToggle = { LoadGoRepository.toggleDriverDocument(doc) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Role switch card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Role & Session / रोल और सत्र",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onSwitchRole,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("driver_switch_role_btn"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.SwapHoriz, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Switch Role (Customer / Admin)")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun DriverDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = TextSecondary)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
    }
}

@Composable
private fun DocumentItemRow(
    docName: String,
    isUploaded: Boolean,
    onToggle: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = SurfaceBg,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onToggle)
            .testTag("doc_item_$docName")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isUploaded) Icons.Default.CheckCircle else Icons.Default.CloudUpload,
                    contentDescription = null,
                    tint = if (isUploaded) StatusGreen else TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = docName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = if (isUploaded) "Uploaded & Verified" else "Tap to upload (Demo)",
                        fontSize = 11.sp,
                        color = if (isUploaded) StatusGreen else TextSecondary
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (isUploaded) StatusGreenLight else LoadGoAmberLight
            ) {
                Text(
                    text = if (isUploaded) "Verified" else "Upload",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isUploaded) StatusGreen else Color(0xFFE65100),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }
    }
}
