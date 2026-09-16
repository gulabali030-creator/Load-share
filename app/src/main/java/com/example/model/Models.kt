package com.example.model

enum class UserRole(val label: String, val hindiLabel: String) {
    CUSTOMER("Customer", "ग्राहक"),
    DRIVER("Driver", "ड्राइवर / पार्टनर"),
    ADMIN("Admin", "प्रशासक")
}

enum class DriverVerificationStatus(val label: String, val hindiLabel: String) {
    PENDING("Pending", "लंबित"),
    APPROVED("Approved", "सत्यापित"),
    REJECTED("Rejected", "अस्वीकृत")
}

enum class BookingStatus(val label: String, val hindiLabel: String) {
    SEARCHING_DRIVER("Searching Driver", "ड्राइवर की तलाश"),
    DRIVER_ASSIGNED("Driver Assigned", "ड्राइवर मिल गया"),
    DRIVER_ARRIVING("Driver Arriving", "ड्राइवर आ रहा है"),
    TRIP_STARTED("Trip Started", "यात्रा शुरू हुई"),
    TRIP_COMPLETED("Trip Completed", "यात्रा पूर्ण"),
    CANCELLED("Cancelled", "रद्द किया गया")
}

data class VehicleTypeInfo(
    val id: String,
    val name: String,
    val hindiName: String,
    val capacity: String,
    val baseFare: Int,
    val perKmRate: Int,
    val dimensions: String,
    val suitableFor: String
)

data class Customer(
    val id: String,
    val name: String,
    val mobile: String,
    val address: String,
    val bookingCount: Int = 0,
    val status: String = "Active"
)

data class Driver(
    val id: String,
    val name: String,
    val mobile: String,
    val vehicleNumber: String,
    val vehicleType: String,
    val licenseNumber: String,
    val isOnline: Boolean = true,
    val verificationStatus: DriverVerificationStatus = DriverVerificationStatus.APPROVED,
    val upiId: String = "driver@okaxis",
    val todayEarnings: Int = 1850,
    val weeklyEarnings: Int = 12400,
    val totalEarnings: Int = 48600,
    val completedTrips: Int = 34,
    val documents: Map<String, Boolean> = mapOf(
        "Driving Licence" to true,
        "Aadhaar" to true,
        "PAN" to true,
        "RC" to true,
        "Insurance" to true
    )
)

data class Booking(
    val bookingId: String,
    val customerId: String,
    val customerName: String,
    val customerPhone: String,
    val driverId: String? = null,
    val driverName: String? = null,
    val driverPhone: String? = null,
    val driverVehicleNumber: String? = null,
    val pickup: String,
    val drop: String,
    val vehicleType: String,
    val goodsDescription: String,
    val fare: Int,
    val status: BookingStatus = BookingStatus.SEARCHING_DRIVER,
    val date: String,
    val distanceKm: Int = 8
)
