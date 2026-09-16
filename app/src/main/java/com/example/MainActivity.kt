package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.data.LoadGoRepository
import com.example.model.UserRole
import com.example.ui.admin.AdminLoginScreen
import com.example.ui.admin.AdminMainScreen
import com.example.ui.customer.CustomerLoginScreen
import com.example.ui.customer.CustomerMainScreen
import com.example.ui.driver.DriverLoginScreen
import com.example.ui.driver.DriverMainScreen
import com.example.ui.role.RoleSelectionScreen
import com.example.ui.theme.LoadGoTheme
import com.example.ui.theme.SurfaceBg

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoadGoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = SurfaceBg
                ) {
                    LoadGoApp()
                }
            }
        }
    }
}

@Composable
fun LoadGoApp() {
    val navController = rememberNavController()
    val currentRole by LoadGoRepository.currentRole.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "role_selection"
    ) {
        composable("role_selection") {
            RoleSelectionScreen(
                onRoleSelected = { role ->
                    LoadGoRepository.selectRole(role)
                    when (role) {
                        UserRole.CUSTOMER -> {
                            // If customer is already logged in for this session, go directly to customer_main
                            if (LoadGoRepository.isCustomerLoggedIn.value) {
                                navController.navigate("customer_main") {
                                    launchSingleTop = true
                                }
                            } else {
                                navController.navigate("customer_login") {
                                    launchSingleTop = true
                                }
                            }
                        }
                        UserRole.DRIVER -> {
                            if (LoadGoRepository.isDriverLoggedIn.value) {
                                navController.navigate("driver_main") {
                                    launchSingleTop = true
                                }
                            } else {
                                navController.navigate("driver_login") {
                                    launchSingleTop = true
                                }
                            }
                        }
                        UserRole.ADMIN -> {
                            if (LoadGoRepository.isAdminLoggedIn.value) {
                                navController.navigate("admin_main") {
                                    launchSingleTop = true
                                }
                            } else {
                                navController.navigate("admin_login") {
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                }
            )
        }

        // Customer Screens
        composable("customer_login") {
            CustomerLoginScreen(
                onLoginSuccess = {
                    navController.navigate("customer_main") {
                        popUpTo("role_selection") { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onBackToRoleSelect = {
                    navController.navigate("role_selection") {
                        popUpTo("role_selection") { inclusive = true }
                    }
                }
            )
        }

        composable("customer_main") {
            CustomerMainScreen(
                onSwitchRole = {
                    navController.navigate("role_selection") {
                        popUpTo("role_selection") { inclusive = true }
                    }
                }
            )
        }

        // Driver Screens
        composable("driver_login") {
            DriverLoginScreen(
                onLoginSuccess = {
                    navController.navigate("driver_main") {
                        popUpTo("role_selection") { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onBackToRoleSelect = {
                    navController.navigate("role_selection") {
                        popUpTo("role_selection") { inclusive = true }
                    }
                }
            )
        }

        composable("driver_main") {
            DriverMainScreen(
                onSwitchRole = {
                    navController.navigate("role_selection") {
                        popUpTo("role_selection") { inclusive = true }
                    }
                }
            )
        }

        // Admin Screens
        composable("admin_login") {
            AdminLoginScreen(
                onLoginSuccess = {
                    navController.navigate("admin_main") {
                        popUpTo("role_selection") { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onBackToRoleSelect = {
                    navController.navigate("role_selection") {
                        popUpTo("role_selection") { inclusive = true }
                    }
                }
            )
        }

        composable("admin_main") {
            AdminMainScreen(
                onSwitchRole = {
                    navController.navigate("role_selection") {
                        popUpTo("role_selection") { inclusive = true }
                    }
                }
            )
        }
    }
}

