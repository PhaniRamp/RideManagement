package com.example.ridemanagementhmi.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.CarRepair
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ridemanagementhmi.model.Ride
import com.example.ridemanagementhmi.viewmodel.MainViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen(viewModel: MainViewModel = getViewModel()) {
    when {
        viewModel.showHomeMapScreen -> {
            BackHandler { viewModel.goToHome() }
            MapOnlyScreen()
        }
        viewModel.showMapScreen -> {
            BackHandler {
                viewModel.showMapScreen = false
                viewModel.showRideRequest = true
            }
            MapWithRidesScreen(
                rides = viewModel.rides,
                onAccept = { /* Optionally handle accept on map screen */ },
                onReject = { ride -> viewModel.rejectRide(ride) }
            )
        }
        viewModel.showRideRequest && viewModel.rides.isNotEmpty() -> RideRequestScreen(
            rides = viewModel.rides,
            onAccept = { viewModel.acceptRide() },
            onReject = { ride -> viewModel.rejectRide(ride) }
        )
        viewModel.showSettings -> AppGridScreen(viewModel)
        viewModel.showHome -> HomeScreen(viewModel)
        else -> HomeScreen(viewModel)
    }
}

@Composable
fun MapOnlyScreen() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val viewModel: MainViewModel = getViewModel()
    androidx.compose.material3.Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(Modifier.fillMaxSize()) {
            androidx.compose.ui.viewinterop.AndroidView(
                factory = { ctx: android.content.Context ->
                    com.mappls.sdk.maps.MapView(ctx).apply {
                        onCreate(null)
                        getMapAsync(object : com.mappls.sdk.maps.OnMapReadyCallback {
                            override fun onMapReady(mapplsMap: com.mappls.sdk.maps.MapplsMap) {
                                // Optionally set style or camera
                            }
                            override fun onMapError(p0: Int, p1: String?) {}
                        })
                    }
                },
                modifier = androidx.compose.ui.Modifier.fillMaxSize()
            )
            // Bottom Home Button
            androidx.compose.material3.Button(
                onClick = { viewModel.goToHome() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
                    .height(56.dp)
                    .width(160.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
            ) {
                androidx.compose.material3.Icon(
                    imageVector = androidx.compose.material.icons.Icons.Filled.CarRepair,
                    contentDescription = "Home"
                )
                Spacer(Modifier.width(8.dp))
                Text("Home")
            }
        }
    }
} 