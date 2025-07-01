package com.example.ridemanagementhmi.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ridemanagementhmi.model.Ride
import com.example.ridemanagementhmi.repository.RideRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale
import org.json.JSONObject

class MainViewModel(app: Application, private val repo: RideRepository) : AndroidViewModel(app) {
    var rides by mutableStateOf(listOf<Ride>())
    var selectedRide by mutableStateOf<Ride?>(null)
    var showRideRequest by mutableStateOf(false)
    var showMapScreen by mutableStateOf(false)
    var pickupAddress by mutableStateOf("Fetching location...")
    var showHome by mutableStateOf(true)
    var showSettings by mutableStateOf(false)
    var enabledApps by mutableStateOf(mutableMapOf<String, Boolean>())
    var showHomeMapScreen by mutableStateOf(false)

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(app)

    private val mapplsRestApiKey = "b9930f54f59b5d54d4ebe585ec5ef39f"

    val allApps = listOf(
        "Food Delivery", "eCart", "Rapido", "Grocery", "Pharmacy", "Taxi", "Laundry", "Parcel", "Courier", "Bike Rental"
    )

    init {
        // No auto-navigation or timer
        allApps.forEach { enabledApps[it] = true }
    }

    fun onAppSelected() {
        fetchCurrentLocation()
    }

    @SuppressLint("MissingPermission")
    private fun fetchCurrentLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    viewModelScope.launch(Dispatchers.IO) {
                        val address = getAddressFromMappls(location.latitude, location.longitude)
                            ?: getAddressFromLocation(location)
                            ?: "Hitech City"
                        pickupAddress = address
                        val ridesList = listOf(
                            Ride("1", pickupAddress, "Madhapur", 50.0),
                            Ride("2", pickupAddress, "Madhapur", 60.0),
                            Ride("3", pickupAddress, "Madhapur", 70.0)
                        )
                        rides = ridesList
                        selectedRide = ridesList.first()
                        showRideRequest = true
                    }
                } else {
                    pickupAddress = "Hitech City"
                    val ridesList = listOf(
                        Ride("1", pickupAddress, "Madhapur", 50.0),
                        Ride("2", pickupAddress, "Madhapur", 60.0),
                        Ride("3", pickupAddress, "Madhapur", 70.0)
                    )
                    rides = ridesList
                    selectedRide = ridesList.first()
                    showRideRequest = true
                }
            }.addOnFailureListener {
                pickupAddress = "Hitech City"
                val ridesList = listOf(
                    Ride("1", pickupAddress, "Madhapur", 50.0),
                    Ride("2", pickupAddress, "Madhapur", 60.0),
                    Ride("3", pickupAddress, "Madhapur", 70.0)
                )
                rides = ridesList
                selectedRide = ridesList.first()
                showRideRequest = true
            }
        }
    }

    private fun getAddressFromMappls(lat: Double, lng: Double): String? {
        return try {
            val url = URL("https://apis.mappls.com/advancedmaps/v1/$mapplsRestApiKey/rev_geocode?lat=$lat&lng=$lng")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.connectTimeout = 5000
            conn.readTimeout = 5000
            val responseCode = conn.responseCode
            if (responseCode == 200) {
                val stream = conn.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(stream)
                val results = json.optJSONObject("results")
                results?.optString("formatted_address")
            } else null
        } catch (e: Exception) {
            null
        }
    }

    private fun getAddressFromLocation(location: Location): String? {
        return try {
            val geocoder = Geocoder(getApplication(), Locale.getDefault())
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                address.getAddressLine(0)
            } else null
        } catch (e: Exception) {
            null
        }
    }

    fun acceptRide() {
        showRideRequest = false
        showMapScreen = true
    }

    fun rejectRide(ride: Ride) {
        rides = rides.filter { it.id != ride.id }
        if (rides.isEmpty()) {
            showRideRequest = false
            showMapScreen = false
            selectedRide = null
        } else {
            selectedRide = rides.first()
        }
    }

    fun goToHome() {
        showHome = true
        showSettings = false
        showRideRequest = false
        showMapScreen = false
        showHomeMapScreen = false
        ensureRidesPopulated()
    }

    private fun ensureRidesPopulated() {
        if (rides.isEmpty()) {
            val defaultPickup = "Hitech City"
            val ridesList = listOf(
                Ride("1", defaultPickup, "Madhapur", 50.0),
                Ride("2", defaultPickup, "Madhapur", 60.0),
                Ride("3", defaultPickup, "Madhapur", 70.0)
            )
            rides = ridesList
            selectedRide = ridesList.first()
        }
    }

    fun goToSettings() {
        showHome = false
        showSettings = true
        showRideRequest = false
        showMapScreen = false
    }

    fun goToHomeMap() {
        showHome = false
        showSettings = false
        showRideRequest = false
        showMapScreen = false
        showHomeMapScreen = true
    }

    fun toggleAppEnabled(app: String) {
        enabledApps = enabledApps.toMutableMap().apply {
            this[app] = !(this[app] ?: true)
        }
    }

    fun selectRide(ride: Ride) {
        selectedRide = ride
    }
} 