package com.example.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LoadGoRepository
import com.example.model.VehicleTypeInfo
import com.example.ui.theme.*

@Composable
fun AdminPricingScreen(
    onBack: () -> Unit
) {
    val vehicleTypes by LoadGoRepository.vehicleTypes.collectAsState()
    var editingVehicle by remember { mutableStateOf<VehicleTypeInfo?>(null) }
    var editBaseFare by remember { mutableStateOf("") }
    var editPerKm by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("pricing_back_btn")
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = "Fleet Pricing Management",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "किराया दरें • Configure base fares & per-km charges",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .testTag("admin_pricing_list")
        ) {
            items(vehicleTypes, key = { it.id }) { vehicle ->
                AdminVehiclePricingCard(
                    vehicle = vehicle,
                    onEdit = {
                        editingVehicle = vehicle
                        editBaseFare = vehicle.baseFare.toString()
                        editPerKm = vehicle.perKmRate.toString()
                    }
                )
            }
        }
    }

    // Edit Pricing Dialog
    if (editingVehicle != null) {
        val v = editingVehicle!!
        AlertDialog(
            onDismissRequest = { editingVehicle = null },
            title = {
                Text("Edit Pricing: ${v.name}", fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text(
                        text = "Update base fare and per-kilometer rate for ${v.name} (${v.capacity}):",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = editBaseFare,
                        onValueChange = { if (it.all { c -> c.isDigit() }) editBaseFare = it },
                        label = { Text("Base Fare (₹) / शुरुआती किराया") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_base_fare_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = editPerKm,
                        onValueChange = { if (it.all { c -> c.isDigit() }) editPerKm = it },
                        label = { Text("Rate Per Km (₹/km) / प्रति किमी") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_per_km_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val base = editBaseFare.toIntOrNull() ?: v.baseFare
                        val perKm = editPerKm.toIntOrNull() ?: v.perKmRate
                        LoadGoRepository.adminUpdateBaseFare(v.id, base, perKm)
                        editingVehicle = null
                    },
                    modifier = Modifier.testTag("save_pricing_btn")
                ) {
                    Text("Save Rates")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingVehicle = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun AdminVehiclePricingCard(
    vehicle: VehicleTypeInfo,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("pricing_card_${vehicle.id}"),
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
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = null,
                        tint = LoadGoBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = vehicle.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${vehicle.hindiName} • Cap: ${vehicle.capacity}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.testTag("edit_btn_${vehicle.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Pricing",
                        tint = LoadGoBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = SurfaceBg,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Base Fare", fontSize = 11.sp, color = TextSecondary)
                        Text(
                            "₹${vehicle.baseFare}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = LoadGoBlueDark
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(30.dp)
                            .background(DividerColor)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Per Km Rate", fontSize = 11.sp, color = TextSecondary)
                        Text(
                            "₹${vehicle.perKmRate} / km",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE65100)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(30.dp)
                            .background(DividerColor)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Sample 10 km", fontSize = 11.sp, color = TextSecondary)
                        Text(
                            "₹${vehicle.baseFare + 10 * vehicle.perKmRate}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = StatusGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Ideal for: ${vehicle.suitableFor}",
                fontSize = 11.sp,
                color = TextSecondary
            )
        }
    }
}
