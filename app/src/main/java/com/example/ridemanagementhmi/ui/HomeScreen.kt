package com.example.ridemanagementhmi.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.CarRepair
import androidx.compose.material.icons.filled.GasMeter
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TireRepair
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Sos
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ridemanagementhmi.viewmodel.MainViewModel
import org.koin.androidx.compose.getViewModel
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items


@Composable
fun HomeScreen(viewModel: MainViewModel = getViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.goToHome()
    }
    val speed = 0f // Always 0 for now
    val tripA = 135.1f
    val odometer = 135.1f
    val battery = 83
    val time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
    val rides = viewModel.rides
    val selectedRide = viewModel.selectedRide
    val surfaceColor = MaterialTheme.colorScheme.surface
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val outlineColor = MaterialTheme.colorScheme.outline
    val backgroundColor = MaterialTheme.colorScheme.background
    Box(
        modifier = Modifier
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
        Row(Modifier.fillMaxSize()) {
            // 70% Dashboard
            Column(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dashboard Row: RPM | Speedometer | Fuel
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // RPM Gauge (left)
                    RpmGauge(rpm = 3200f, maxRpm = 8000f, modifier = Modifier.padding(end = 32.dp))
                    // Speedometer (center)
                    Card(
                        modifier = Modifier.shadow(16.dp, CircleShape),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF181A1B)),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(220.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            SpeedometerGauge(speed = 72f, maxSpeed = 180f)
                        }
                    }
                    // Fuel Gauge (right)
                    FuelGauge(fuel = 0.65f, modifier = Modifier.padding(start = 32.dp))
                }
                // Odometer and Gear Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "12,345 km",
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 32.dp)
                    )
                    GearIndicator(gear = "D")
                }
                // Top Bar
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(time, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.weight(1f))
                    Icon(Icons.Default.SignalCellularAlt, contentDescription = "Signal", tint = Color.White, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.Default.Wifi, contentDescription = "WiFi", tint = Color.White, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.Default.Bluetooth, contentDescription = "Bluetooth", tint = Color.White, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.Default.GpsFixed, contentDescription = "GPS", tint = Color.White, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
            // Vertical Divider
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .padding(vertical = 16.dp),
                color = Color(0xFF424242)
            )
            // 30% Rides List
            LazyColumn(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                val onGoingRide = selectedRide
                val remainingRides = rides.filter { it.id != onGoingRide?.id }
                if (onGoingRide != null) {
                    item {
                        Text(
                            "On Going Ride",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Card(
                            modifier = Modifier
                                .padding(vertical = 8.dp, horizontal = 4.dp)
                                .fillMaxWidth()
                                .shadow(12.dp, RoundedCornerShape(16.dp))
                                .border(
                                    width = 3.dp,
                                    color = Color(0xFF00E676),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable {
                                    viewModel.showHome = false
                                    viewModel.showMapScreen = true
                                },
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF181A1B))
                        ) {
                            Column(Modifier.padding(20.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFF212121),
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = "Pickup",
                                            tint = Color(0xFF00B0FF),
                                            modifier = Modifier.size(24.dp).padding(6.dp)
                                        )
                                    }
                                    Spacer(Modifier.width(12.dp))
                                    Text("Pickup: ${onGoingRide.pickup}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                                Spacer(Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFF212121),
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Place,
                                            contentDescription = "Drop",
                                            tint = Color(0xFF00E676),
                                            modifier = Modifier.size(24.dp).padding(6.dp)
                                        )
                                    }
                                    Spacer(Modifier.width(12.dp))
                                    Text("Drop: ${onGoingRide.drop}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                                Spacer(Modifier.height(8.dp))
                                Text("Cost: \u20b9${onGoingRide.cost}", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF00E676), fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
                if (remainingRides.isNotEmpty()) {
                    item {
                        Text(
                            "New Rides",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF00B0FF),
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                        )
                    }
                    items(remainingRides) { ride ->
                        Card(
                            modifier = Modifier
                                .padding(vertical = 8.dp, horizontal = 4.dp)
                                .fillMaxWidth()
                                .shadow(8.dp, RoundedCornerShape(14.dp))
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFF424242),
                                    shape = RoundedCornerShape(14.dp)
                                ),
                            shape = RoundedCornerShape(14.dp),
                            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF232526))
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFF181A1B),
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = "Pickup",
                                            tint = Color(0xFF00B0FF),
                                            modifier = Modifier.size(20.dp).padding(4.dp)
                                        )
                                    }
                                    Spacer(Modifier.width(10.dp))
                                    Text("Pickup: ${ride.pickup}", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                                }
                                Spacer(Modifier.height(6.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFF181A1B),
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Place,
                                            contentDescription = "Drop",
                                            tint = Color(0xFF00E676),
                                            modifier = Modifier.size(20.dp).padding(4.dp)
                                        )
                                    }
                                    Spacer(Modifier.width(10.dp))
                                    Text("Drop: ${ride.drop}", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                                }
                                Spacer(Modifier.height(6.dp))
                                Text("Cost: \u20b9${ride.cost}", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF00E676))
                                Row(
                                    modifier = Modifier.padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Button(onClick = { viewModel.selectRide(ride); viewModel.acceptRide() }, modifier = Modifier.padding(end = 8.dp)) {
                                        Text("Accept", fontSize = 16.sp)
                                    }
                                    Button(onClick = { viewModel.rejectRide(ride) }) {
                                        Text("Reject", fontSize = 16.sp)
                                    }
                                }
                            }
                        }
                    }
                }
                if (onGoingRide == null && remainingRides.isEmpty()) {
                    item {
                        Text("No rides available.", color = Color.Gray)
                    }
                }
            }
        }
        // Bottom Menu Bar (always visible at the bottom)
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color(0xFF181A1B))
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MenuItem(
                icon = Icons.Default.LocationOn,
                label = "Map",
                color = Color(0xFF00B0FF),
                onClick = { viewModel.goToHomeMap() }
            )
            MenuItem(
                icon = Icons.Default.PlayArrow,
                label = "Music",
                color = Color.White,
                onClick = { /* TODO: Music action */ }
            )
            MenuItem(
                icon = Icons.Default.CarRepair,
                label = "Repair",
                color = Color(0xFF00E676),
                onClick = { /* TODO: Car Repair action */ }
            )
            MenuItem(
                icon = Icons.Default.Settings,
                label = "Settings",
                color = Color.White,
                onClick = { viewModel.goToSettings() }
            )
        }
    }
}

@Composable
fun SpeedometerGauge(speed: Float, maxSpeed: Float) {
    val arcColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
    val needleColor = MaterialTheme.colorScheme.primary
    Box(Modifier.size(220.dp), contentAlignment = Alignment.Center) {
        // Arc background
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val sweep = 240f
            val start = 150f
            drawArc(
                color = arcColor,
                startAngle = start,
                sweepAngle = sweep,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 24f)
            )
        }
        // Needle
        val angle = 150f + (speed / maxSpeed) * 240f
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = size.minDimension / 2.2f
            val centerX = size.width / 2
            val centerY = size.height / 2
            val rad = Math.toRadians(angle.toDouble())
            val x = centerX + radius * cos(rad).toFloat()
            val y = centerY + radius * sin(rad).toFloat()
            drawLine(
                color = needleColor,
                start = androidx.compose.ui.geometry.Offset(centerX, centerY),
                end = androidx.compose.ui.geometry.Offset(x, y),
                strokeWidth = 8f
            )
        }
        // Speed Text
        Text(
            text = "${speed.toInt()} km/h",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = needleColor
        )
    }
}

@Composable
fun RpmGauge(rpm: Float, maxRpm: Float, modifier: Modifier = Modifier) {
    val arcColor = Color(0xFF388E3C).copy(alpha = 0.2f)
    val needleColor = Color(0xFF388E3C)
    Box(modifier.size(100.dp), contentAlignment = Alignment.Center) {
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val sweep = 120f
            val start = 210f
            drawArc(
                color = arcColor,
                startAngle = start,
                sweepAngle = sweep,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 12f)
            )
        }
        // Needle
        val angle = 210f + (rpm / maxRpm) * 120f
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = size.minDimension / 2.5f
            val centerX = size.width / 2
            val centerY = size.height / 2
            val rad = Math.toRadians(angle.toDouble())
            val x = centerX + radius * cos(rad).toFloat()
            val y = centerY + radius * sin(rad).toFloat()
            drawLine(
                color = needleColor,
                start = androidx.compose.ui.geometry.Offset(centerX, centerY),
                end = androidx.compose.ui.geometry.Offset(x, y),
                strokeWidth = 4f
            )
        }
        Text(
            text = "${(rpm/1000).toInt()}k RPM",
            fontSize = 14.sp,
            color = needleColor,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun FuelGauge(fuel: Float, modifier: Modifier = Modifier) {
    val arcColor = Color(0xFF1976D2).copy(alpha = 0.2f)
    val needleColor = Color(0xFF1976D2)
    Box(modifier.size(80.dp), contentAlignment = Alignment.Center) {
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val sweep = 120f
            val start = 210f
            drawArc(
                color = arcColor,
                startAngle = start,
                sweepAngle = sweep,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 10f)
            )
        }
        // Needle
        val angle = 210f + (fuel) * 120f
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = size.minDimension / 2.5f
            val centerX = size.width / 2
            val centerY = size.height / 2
            val rad = Math.toRadians(angle.toDouble())
            val x = centerX + radius * cos(rad).toFloat()
            val y = centerY + radius * sin(rad).toFloat()
            drawLine(
                color = needleColor,
                start = androidx.compose.ui.geometry.Offset(centerX, centerY),
                end = androidx.compose.ui.geometry.Offset(x, y),
                strokeWidth = 3f
            )
        }
        Icon(Icons.Default.GasMeter, contentDescription = "Fuel", tint = needleColor, modifier = Modifier.size(36.dp))
        Text(
            text = "${(fuel*100).toInt()}%",
            fontSize = 12.sp,
            color = needleColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 4.dp)
        )
    }
}

@Composable
fun GearIndicator(gear: String, modifier: Modifier = Modifier) {
    val bgColor = MaterialTheme.colorScheme.secondaryContainer
    val textColor = MaterialTheme.colorScheme.secondary
    Surface(
        shape = CircleShape,
        color = bgColor,
        shadowElevation = 4.dp,
        modifier = modifier.size(48.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = gear,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}

@Composable
fun MenuItem(icon: ImageVector, label: String, color: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp)
    ) {
        Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(28.dp))
        Text(label, color = Color.White, fontSize = 13.sp)
    }
} 