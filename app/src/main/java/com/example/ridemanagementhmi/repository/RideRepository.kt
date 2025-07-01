package com.example.ridemanagementhmi.repository

import com.example.ridemanagementhmi.model.Ride

class RideRepository {
    fun getRides(): List<Ride> = listOf(
        Ride("1", "Current Location", "Madhapur", 50.0)
    )
} 