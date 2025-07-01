package com.example.ridemanagementhmi.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ridemanagementhmi.model.Ride
import com.example.ridemanagementhmi.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CarRepair
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.Color
import org.koin.androidx.compose.getViewModel

@Composable
fun RideRequestScreen(
    rides: List<Ride>,
    onAccept: (Ride) -> Unit,
    onReject: (Ride) -> Unit
) {
    val viewModel: com.example.ridemanagementhmi.viewmodel.MainViewModel = getViewModel()
    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Incoming Ride Requests",
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                rides.forEach { ride ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(300.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Pickup",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Pickup: ${ride.pickup}",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Place,
                                    contentDescription = "Drop",
                                    tint = Color(0xFF388E3C)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Drop: ${ride.drop}",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                            Text(
                                "Cost: ₹${ride.cost}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            Row(
                                modifier = Modifier.padding(top = 8.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(onClick = { onAccept(ride) }, modifier = Modifier.padding(end = 8.dp)) {
                                    Text("Accept", fontSize = 16.sp)
                                }
                                Button(onClick = { onReject(ride) }) {
                                    Text("Reject", fontSize = 16.sp)
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        // Bottom Home Button
        Button(
            onClick = { viewModel.goToHome() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .height(56.dp)
                .width(160.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(Icons.Default.CarRepair, contentDescription = "Home")
            Spacer(Modifier.width(8.dp))
            Text("Home")
        }
    }
} 