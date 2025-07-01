package com.example.ridemanagementhmi.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import org.koin.androidx.compose.getViewModel
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

@Composable
fun RideRequestScreen(
    rides: List<Ride>,
    onAccept: (Ride) -> Unit,
    onReject: (Ride) -> Unit
) {
    val viewModel: com.example.ridemanagementhmi.viewmodel.MainViewModel = getViewModel()
    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF232526),
                        Color(0xFF0F2027),
                        Color.Black
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Incoming Ride Requests",
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(rides) { ride ->
                    Card(
                        modifier = Modifier
                            .height(300.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = androidx.compose.material3.CardDefaults.elevatedCardElevation(defaultElevation = 12.dp),
                        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFF181A1B))
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
                                    tint = Color(0xFF00B0FF),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Pickup: ${ride.pickup}",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                                    color = Color.White,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Place,
                                    contentDescription = "Drop",
                                    tint = Color(0xFF00E676),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Drop: ${ride.drop}",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                                    color = Color.White,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                            Text(
                                "Cost: \t${ride.cost}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                                color = Color(0xFF00E676),
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