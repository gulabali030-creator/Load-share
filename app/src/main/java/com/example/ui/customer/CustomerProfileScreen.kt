package com.example.ui.customer

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
import com.example.ui.theme.*

@Composable
fun CustomerProfileScreen(
    onSwitchRole: () -> Unit
) {
    val currentCustomer by LoadGoRepository.currentCustomer.collectAsState()
    var isEditing by remember { mutableStateOf(false) }

    var name by remember(currentCustomer) { mutableStateOf(currentCustomer?.name ?: "") }
    var mobile by remember(currentCustomer) { mutableStateOf(currentCustomer?.mobile ?: "") }
    var address by remember(currentCustomer) { mutableStateOf(currentCustomer?.address ?: "") }
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
            text = "Customer Profile",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = "ग्राहक प्रोफ़ाइल • Manage personal and address details",
            fontSize = 12.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Avatar Card
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
                        .background(LoadGoBlueLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = LoadGoBlue,
                        modifier = Modifier.size(42.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = currentCustomer?.name ?: "Customer",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "+91 ${currentCustomer?.mobile ?: ""}",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = LoadGoBlueLight
                ) {
                    Text(
                        text = "ID: ${currentCustomer?.id ?: ""} • ${currentCustomer?.bookingCount ?: 0} Trips Completed",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LoadGoBlueDark,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Details / Edit Card
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
                        text = "Profile Information",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    IconButton(
                        onClick = { isEditing = !isEditing },
                        modifier = Modifier.testTag("edit_customer_profile_btn")
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
                        label = { Text("Customer Name / नाम") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = LoadGoBlue) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_name_input"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = mobile,
                        onValueChange = {
                            if (it.length <= 10 && it.all { c -> c.isDigit() }) mobile = it
                        },
                        label = { Text("Mobile Number / मोबाइल") },
                        prefix = { Text("+91 ") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = LoadGoBlue) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_mobile_input"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Default Address / पता") },
                        leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, tint = LoadGoBlue) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_address_input"),
                        shape = RoundedCornerShape(12.dp),
                        minLines = 2
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            LoadGoRepository.updateCustomerProfile(name, mobile, address)
                            isEditing = false
                            saveSuccess = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("save_customer_profile_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LoadGoBlue)
                    ) {
                        Text("Save Profile / सुरक्षित करें", fontWeight = FontWeight.Bold)
                    }
                } else {
                    ProfileFieldItem(
                        label = "Full Name / पूरा नाम",
                        value = currentCustomer?.name ?: "-",
                        icon = Icons.Default.Person
                    )
                    ProfileFieldItem(
                        label = "Mobile Number / मोबाइल",
                        value = "+91 ${currentCustomer?.mobile ?: "-"}",
                        icon = Icons.Default.Phone
                    )
                    ProfileFieldItem(
                        label = "Default Address / पता",
                        value = currentCustomer?.address.takeIf { !it.isNullOrBlank() } ?: "Not specified",
                        icon = Icons.Default.LocationOn
                    )
                }

                if (saveSuccess) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "✓ Profile updated successfully",
                        color = StatusGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Role Switch & Session Actions
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
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onSwitchRole,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("customer_switch_role_btn"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.SwapHoriz, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Switch Role (Driver / Admin)")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ProfileFieldItem(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(LoadGoBlueLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = LoadGoBlue, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = label, fontSize = 11.sp, color = TextSecondary)
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
        }
    }
}
