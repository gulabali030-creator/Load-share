package com.example.data

import com.example.model.Booking
import com.example.model.BookingStatus
import com.example.model.Customer
import com.example.model.Driver
import com.example.model.DriverVerificationStatus
import com.example.model.UserRole
import com.example.model.VehicleTypeInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object LoadGoRepository {

    // Current Session & Role
    private val _currentRole = MutableStateFlow<UserRole?>(null)
    val currentRole: StateFlow<UserRole?> = _currentRole.asStateFlow()

    // Customer Session
    private val _currentCustomer = MutableStateFlow<Customer?>(
        Customer(
            id = "CUST-101",
            name = "Ramesh Sharma",
            mobile = "9876543210",
            address = "Shop 14, Karol Bagh Market, New Delhi",
            bookingCount = 3
        )
    )
    val currentCustomer: StateFlow<Customer?> = _currentCustomer.asStateFlow()

    private val _isCustomerLoggedIn = MutableStateFlow(false)
    val isCustomerLoggedIn: StateFlow<Boolean> = _isCustomerLoggedIn.asStateFlow()

    // Driver Session
    private val _currentDriver = MutableStateFlow<Driver>(
        Driver(
            id = "DRV-201",
            name = "Rajesh Kumar",
            mobile = "9812345678",
            vehicleNumber = "DL 1L AA 4589",
            vehicleType = "Tata Ace",
            licenseNumber = "DL-0420190012345",
            isOnline = true,
            verificationStatus = DriverVerificationStatus.APPROVED,
            upiId = "rajesh.loadgo@oksbi",
            todayEarnings = 1850,
            weeklyEarnings = 12400,
            totalEarnings = 48600,
            completedTrips = 28
        )
    )
    val currentDriver: StateFlow<Driver> = _currentDriver.asStateFlow()

    private val _isDriverLoggedIn = MutableStateFlow(false)
    val isDriverLoggedIn: StateFlow<Boolean> = _isDriverLoggedIn.asStateFlow()

    // Admin Session
    private val _isAdminLoggedIn = MutableStateFlow(false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    // Vehicle Types with Pricing (Admin can modify base fare)
    private val _vehicleTypes = MutableStateFlow<List<VehicleTypeInfo>>(
        listOf(
            VehicleTypeInfo(
                id = "mini_truck",
                name = "Mini Truck",
                hindiName = "छोटा ट्रक",
                capacity = "500 kg",
                baseFare = 250,
                perKmRate = 18,
                dimensions = "5.5ft x 4ft",
                suitableFor = "Small boxes, electronics, appliances"
            ),
            VehicleTypeInfo(
                id = "pickup",
                name = "Pickup",
                hindiName = "पिकअप",
                capacity = "1000 kg",
                baseFare = 450,
                perKmRate = 24,
                dimensions = "8ft x 4.8ft",
                suitableFor = "Hardware, plywood, medium furniture"
            ),
            VehicleTypeInfo(
                id = "tata_ace",
                name = "Tata Ace",
                hindiName = "छोटा हाथी (Tata Ace)",
                capacity = "750 kg",
                baseFare = 350,
                perKmRate = 20,
                dimensions = "7ft x 4.5ft",
                suitableFor = "Most popular for shifting, retail goods"
            ),
            VehicleTypeInfo(
                id = "truck_8ft",
                name = "8ft Truck",
                hindiName = "८ फीट ट्रक",
                capacity = "1500 kg",
                baseFare = 650,
                perKmRate = 30,
                dimensions = "8.5ft x 5.5ft",
                suitableFor = "Office relocation, machinery, heavy crates"
            ),
            VehicleTypeInfo(
                id = "truck_14ft",
                name = "14ft Truck",
                hindiName = "१४ फीट ट्रक",
                capacity = "3500 kg",
                baseFare = 1100,
                perKmRate = 45,
                dimensions = "14ft x 6ft",
                suitableFor = "Full home shifting, industrial warehouse haul"
            )
        )
    )
    val vehicleTypes: StateFlow<List<VehicleTypeInfo>> = _vehicleTypes.asStateFlow()

    // Customers List (Admin view)
    private val _customersList = MutableStateFlow<List<Customer>>(
        listOf(
            Customer("CUST-101", "Ramesh Sharma", "9876543210", "Karol Bagh, New Delhi", 3, "Active"),
            Customer("CUST-102", "Priya Verma", "9811223344", "Indiranagar, Bengaluru", 5, "Active"),
            Customer("CUST-103", "Sunil Gupta", "9988776655", "Andheri East, Mumbai", 1, "Active"),
            Customer("CUST-104", "Amit Patel", "9722334455", "GIDC, Ahmedabad", 8, "Active")
        )
    )
    val customersList: StateFlow<List<Customer>> = _customersList.asStateFlow()

    // Drivers List (Admin view)
    private val _driversList = MutableStateFlow<List<Driver>>(
        listOf(
            Driver(
                id = "DRV-201",
                name = "Rajesh Kumar",
                mobile = "9812345678",
                vehicleNumber = "DL 1L AA 4589",
                vehicleType = "Tata Ace",
                licenseNumber = "DL-0420190012345",
                isOnline = true,
                verificationStatus = DriverVerificationStatus.APPROVED
            ),
            Driver(
                id = "DRV-202",
                name = "Gurpreet Singh",
                mobile = "9872112233",
                vehicleNumber = "PB 10 CT 7821",
                vehicleType = "Pickup",
                licenseNumber = "PB-1020180098765",
                isOnline = true,
                verificationStatus = DriverVerificationStatus.APPROVED
            ),
            Driver(
                id = "DRV-203",
                name = "Mohammad Irfan",
                mobile = "9955443322",
                vehicleNumber = "MH 02 BB 1290",
                vehicleType = "8ft Truck",
                licenseNumber = "MH-0220200054321",
                isOnline = false,
                verificationStatus = DriverVerificationStatus.PENDING
            ),
            Driver(
                id = "DRV-204",
                name = "Satish Reddy",
                mobile = "9440123456",
                vehicleNumber = "TS 07 EA 9901",
                vehicleType = "Mini Truck",
                licenseNumber = "TS-0720210067890",
                isOnline = true,
                verificationStatus = DriverVerificationStatus.APPROVED
            )
        )
    )
    val driversList: StateFlow<List<Driver>> = _driversList.asStateFlow()

    // Bookings List (Shared live state)
    private val _bookings = MutableStateFlow<List<Booking>>(
        listOf(
            Booking(
                bookingId = "LG-9021",
                customerId = "CUST-101",
                customerName = "Ramesh Sharma",
                customerPhone = "9876543210",
                driverId = "DRV-201",
                driverName = "Rajesh Kumar",
                driverPhone = "9812345678",
                driverVehicleNumber = "DL 1L AA 4589",
                pickup = "Karol Bagh Market, New Delhi",
                drop = "Chandni Chowk Wholesale, Delhi",
                vehicleType = "Tata Ace",
                goodsDescription = "Garment cartons (12 boxes)",
                fare = 550,
                status = BookingStatus.TRIP_COMPLETED,
                date = "15 Sep 2026, 02:30 PM",
                distanceKm = 10
            ),
            Booking(
                bookingId = "LG-9018",
                customerId = "CUST-101",
                customerName = "Ramesh Sharma",
                customerPhone = "9876543210",
                driverId = "DRV-202",
                driverName = "Gurpreet Singh",
                driverPhone = "9872112233",
                driverVehicleNumber = "PB 10 CT 7821",
                pickup = "Okhla Industrial Area Phase 3",
                drop = "Noida Sector 63 Logistics Hub",
                vehicleType = "Pickup",
                goodsDescription = "Electrical spare parts",
                fare = 850,
                status = BookingStatus.TRIP_COMPLETED,
                date = "14 Sep 2026, 11:15 AM",
                distanceKm = 16
            ),
            Booking(
                bookingId = "LG-9034",
                customerId = "CUST-102",
                customerName = "Priya Verma",
                customerPhone = "9811223344",
                driverId = null,
                driverName = null,
                driverPhone = null,
                driverVehicleNumber = null,
                pickup = "Indiranagar 100ft Road, Bengaluru",
                drop = "Whitefield Tech Park, Bengaluru",
                vehicleType = "Mini Truck",
                goodsDescription = "Home furniture & small tables",
                fare = 420,
                status = BookingStatus.SEARCHING_DRIVER,
                date = "Today, 10:00 AM",
                distanceKm = 9
            )
        )
    )
    val bookings: StateFlow<List<Booking>> = _bookings.asStateFlow()

    // --- Role and Session Actions ---
    fun selectRole(role: UserRole) {
        _currentRole.value = role
    }

    fun logout() {
        _currentRole.value = null
    }

    fun loginCustomer(mobile: String, name: String) {
        val existing = _customersList.value.find { it.mobile == mobile }
        val cust = existing?.copy(name = name.ifBlank { existing.name })
            ?: Customer(
                id = "CUST-${(100..999).random()}",
                name = name.ifBlank { "Valued Customer" },
                mobile = mobile,
                address = "Connaught Place, New Delhi",
                bookingCount = 0
            )
        _currentCustomer.value = cust
        if (existing == null) {
            _customersList.value = listOf(cust) + _customersList.value
        }
        _isCustomerLoggedIn.value = true
    }

    fun updateCustomerProfile(name: String, mobile: String, address: String) {
        val updated = _currentCustomer.value?.copy(name = name, mobile = mobile, address = address)
        _currentCustomer.value = updated
        if (updated != null) {
            _customersList.value = _customersList.value.map {
                if (it.id == updated.id) updated else it
            }
        }
    }

    fun loginDriver(mobile: String) {
        val existing = _driversList.value.find { it.mobile == mobile }
        if (existing != null) {
            _currentDriver.value = existing
        } else {
            val newDrv = _currentDriver.value.copy(mobile = mobile)
            _currentDriver.value = newDrv
        }
        _isDriverLoggedIn.value = true
    }

    fun updateDriverProfile(
        name: String,
        mobile: String,
        vehicleNumber: String,
        vehicleType: String,
        licenseNumber: String,
        upiId: String
    ) {
        val updated = _currentDriver.value.copy(
            name = name,
            mobile = mobile,
            vehicleNumber = vehicleNumber,
            vehicleType = vehicleType,
            licenseNumber = licenseNumber,
            upiId = upiId
        )
        _currentDriver.value = updated
        _driversList.value = _driversList.value.map {
            if (it.id == updated.id) updated else it
        }
    }

    fun toggleDriverOnline(online: Boolean) {
        val updated = _currentDriver.value.copy(isOnline = online)
        _currentDriver.value = updated
        _driversList.value = _driversList.value.map {
            if (it.id == updated.id) updated else it
        }
    }

    fun toggleDriverDocument(docName: String) {
        val currentDocs = _currentDriver.value.documents.toMutableMap()
        currentDocs[docName] = !(currentDocs[docName] ?: false)
        _currentDriver.value = _currentDriver.value.copy(documents = currentDocs)
    }

    fun loginAdmin(username: String, pass: String): Boolean {
        return if (username == "admin" && pass == "admin123") {
            _isAdminLoggedIn.value = true
            true
        } else {
            false
        }
    }

    fun logoutAdmin() {
        _isAdminLoggedIn.value = false
    }

    // --- Booking Workflows ---
    fun calculateEstimatedFare(vehicleId: String, distanceKm: Int = 10): Int {
        val v = _vehicleTypes.value.find { it.id == vehicleId } ?: _vehicleTypes.value.first()
        return v.baseFare + (distanceKm * v.perKmRate)
    }

    fun createBooking(
        pickup: String,
        drop: String,
        vehicleType: String,
        goodsDescription: String,
        fare: Int,
        distanceKm: Int = 10
    ): Booking {
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val dateStr = sdf.format(Date())
        val randomNum = (1000..9999).random()
        val customer = _currentCustomer.value ?: Customer("CUST-101", "Ramesh Sharma", "9876543210", "")

        val newBooking = Booking(
            bookingId = "LG-$randomNum",
            customerId = customer.id,
            customerName = customer.name,
            customerPhone = customer.mobile,
            pickup = pickup,
            drop = drop,
            vehicleType = vehicleType,
            goodsDescription = goodsDescription.ifBlank { "Household / Commercial Goods" },
            fare = fare,
            status = BookingStatus.SEARCHING_DRIVER,
            date = dateStr,
            distanceKm = distanceKm
        )

        _bookings.value = listOf(newBooking) + _bookings.value

        // update customer booking count
        val updatedCust = customer.copy(bookingCount = customer.bookingCount + 1)
        _currentCustomer.value = updatedCust
        _customersList.value = _customersList.value.map { if (it.id == customer.id) updatedCust else it }

        return newBooking
    }

    // Driver accepts booking -> Driver Assigned
    fun acceptBookingByDriver(bookingId: String) {
        val driver = _currentDriver.value
        _bookings.value = _bookings.value.map { b ->
            if (b.bookingId == bookingId) {
                b.copy(
                    driverId = driver.id,
                    driverName = driver.name,
                    driverPhone = driver.mobile,
                    driverVehicleNumber = driver.vehicleNumber,
                    status = BookingStatus.DRIVER_ASSIGNED
                )
            } else b
        }
    }

    // Driver advances status: Driver Arriving
    fun driverArriving(bookingId: String) {
        _bookings.value = _bookings.value.map { b ->
            if (b.bookingId == bookingId) {
                b.copy(status = BookingStatus.DRIVER_ARRIVING)
            } else b
        }
    }

    // Driver starts trip -> Trip Started
    fun startTripByDriver(bookingId: String) {
        _bookings.value = _bookings.value.map { b ->
            if (b.bookingId == bookingId) {
                b.copy(status = BookingStatus.TRIP_STARTED)
            } else b
        }
    }

    // Driver completes trip -> Trip Completed
    fun completeTripByDriver(bookingId: String) {
        var tripFare = 0
        _bookings.value = _bookings.value.map { b ->
            if (b.bookingId == bookingId) {
                tripFare = b.fare
                b.copy(status = BookingStatus.TRIP_COMPLETED)
            } else b
        }

        if (tripFare > 0) {
            val updatedDriver = _currentDriver.value.copy(
                todayEarnings = _currentDriver.value.todayEarnings + tripFare,
                weeklyEarnings = _currentDriver.value.weeklyEarnings + tripFare,
                totalEarnings = _currentDriver.value.totalEarnings + tripFare,
                completedTrips = _currentDriver.value.completedTrips + 1
            )
            _currentDriver.value = updatedDriver
            _driversList.value = _driversList.value.map {
                if (it.id == updatedDriver.id) updatedDriver else it
            }
        }
    }

    // Customer cancels booking
    fun cancelBooking(bookingId: String) {
        _bookings.value = _bookings.value.map { b ->
            if (b.bookingId == bookingId) {
                b.copy(status = BookingStatus.CANCELLED)
            } else b
        }
    }

    // Admin updates booking status directly (for testing/demo)
    fun adminUpdateBookingStatus(bookingId: String, newStatus: BookingStatus) {
        _bookings.value = _bookings.value.map { b ->
            if (b.bookingId == bookingId) {
                b.copy(status = newStatus)
            } else b
        }
    }

    // Admin verifies driver
    fun adminUpdateDriverStatus(driverId: String, status: DriverVerificationStatus) {
        _driversList.value = _driversList.value.map { d ->
            if (d.id == driverId) d.copy(verificationStatus = status) else d
        }
        if (_currentDriver.value.id == driverId) {
            _currentDriver.value = _currentDriver.value.copy(verificationStatus = status)
        }
    }

    // Admin updates base pricing
    fun adminUpdateBaseFare(vehicleId: String, newBaseFare: Int, newPerKm: Int) {
        _vehicleTypes.value = _vehicleTypes.value.map { v ->
            if (v.id == vehicleId) v.copy(baseFare = newBaseFare, perKmRate = newPerKm) else v
        }
    }
}
