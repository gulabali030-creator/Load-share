package com.example.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LoadGoRepository
import com.example.ui.components.LoadGoBrandLogo
import com.example.ui.theme.*

@Composable
fun CustomerLoginScreen(
    onLoginSuccess: () -> Unit,
    onBackToRoleSelect: () -> Unit
) {
    var step by remember { mutableStateOf(1) } // 1: Mobile & Name, 2: OTP
    var mobile by remember { mutableStateOf("9876543210") }
    var name by remember { mutableStateOf("Ramesh Sharma") }
    var otp by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackToRoleSelect,
                    modifier = Modifier.testTag("back_to_role_select_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back to Roles",
                        tint = TextPrimary
                    )
                }
                TextButton(onClick = onBackToRoleSelect) {
                    Text("Change Role", fontSize = 13.sp, color = LoadGoBlue)
                }
            }
        },
        containerColor = SurfaceBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            LoadGoBrandLogo(showTagline = true)
            Spacer(modifier = Modifier.height(28.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (step == 1) {
                        Text(
                            text = "Customer Login",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "ग्राहक लॉगिन • Enter details to book vehicles",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                        )

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it; errorMessage = null },
                            label = { Text("Customer Name / नाम") },
                            placeholder = { Text("e.g. Ramesh Sharma") },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = LoadGoBlue)
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("customer_name_input"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = mobile,
                            onValueChange = {
                                if (it.length <= 10 && it.all { char -> char.isDigit() }) {
                                    mobile = it
                                    errorMessage = null
                                }
                            },
                            label = { Text("Mobile Number / मोबाइल नंबर") },
                            placeholder = { Text("10 digit mobile") },
                            prefix = { Text("+91 ", fontWeight = FontWeight.Bold) },
                            leadingIcon = {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = LoadGoBlue)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("customer_mobile_input"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        if (errorMessage != null) {
                            Text(
                                text = errorMessage ?: "",
                                color = StatusRed,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                if (mobile.length < 10) {
                                    errorMessage = "Please enter a valid 10-digit mobile number"
                                } else {
                                    errorMessage = null
                                    step = 2
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("customer_continue_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = LoadGoBlue)
                        ) {
                            Text(
                                text = "Continue / आगे बढ़ें",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        // Step 2: OTP Verification
                        Text(
                            text = "Verify OTP",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "OTP sent to +91 $mobile",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        // Demo OTP hint banner
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = LoadGoAmberLight,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color(0xFFE65100),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Demo Mode OTP: 123456",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color(0xFFE65100)
                                    )
                                    Text(
                                        text = "डेमो ओटीपी 123456 दर्ज करें",
                                        fontSize = 11.sp,
                                        color = Color(0xFFE65100).copy(alpha = 0.9f)
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = otp,
                            onValueChange = {
                                if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                                    otp = it
                                    errorMessage = null
                                }
                            },
                            label = { Text("Enter 6-digit OTP") },
                            placeholder = { Text("123456") },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = LoadGoBlue)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("customer_otp_input"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Quick auto-fill button
                        TextButton(
                            onClick = { otp = "123456"; errorMessage = null },
                            modifier = Modifier
                                .align(Alignment.End)
                                .testTag("auto_fill_customer_otp_btn")
                        ) {
                            Text("Fill Demo OTP (123456)", fontSize = 12.sp, color = LoadGoBlue)
                        }

                        if (errorMessage != null) {
                            Text(
                                text = errorMessage ?: "",
                                color = StatusRed,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (otp == "123456") {
                                    LoadGoRepository.loginCustomer(mobile, name)
                                    onLoginSuccess()
                                } else {
                                    errorMessage = "Invalid OTP. Please use demo OTP: 123456"
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("customer_verify_otp_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = LoadGoBlue)
                        ) {
                            Text(
                                text = "Verify & Login / लॉगिन करें",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        TextButton(onClick = { step = 1; errorMessage = null }) {
                            Text("Change Mobile Number", fontSize = 13.sp, color = TextSecondary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
