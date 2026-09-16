package com.example.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LoadGoRepository
import com.example.model.Customer
import com.example.ui.theme.*

@Composable
fun AdminCustomersScreen() {
    val customers by LoadGoRepository.customersList.collectAsState()

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
                    text = "Manage Customers",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "ग्राहक प्रबंधन • Registered platform users & order frequency",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = LoadGoBlueLight
            ) {
                Text(
                    text = "${customers.size} Registered",
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
                .testTag("admin_customers_list")
        ) {
            items(customers, key = { it.id }) { customer ->
                AdminCustomerCard(customer = customer)
            }
        }
    }
}

@Composable
private fun AdminCustomerCard(customer: Customer) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_customer_item_${customer.id}"),
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
                        .size(44.dp)
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
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = customer.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "+91 ${customer.mobile} • ${customer.id}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = customer.address.ifBlank { "Address not specified" },
                        fontSize = 11.sp,
                        color = TextSecondary,
                        maxLines = 1
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = StatusGreenLight
                ) {
                    Text(
                        text = customer.status,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = StatusGreen,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${customer.bookingCount} Bookings",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = LoadGoBlueDark
                )
            }
        }
    }
}
