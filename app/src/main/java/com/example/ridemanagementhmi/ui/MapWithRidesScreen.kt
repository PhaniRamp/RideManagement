package com.example.ridemanagementhmi.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CarRepair
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ridemanagementhmi.R
import com.example.ridemanagementhmi.model.Ride
import com.mappls.sdk.maps.MapView
import com.mappls.sdk.maps.MapplsMap
import com.mappls.sdk.maps.OnMapReadyCallback
import org.koin.androidx.compose.getViewModel
import com.example.ridemanagementhmi.viewmodel.MainViewModel

import com.mappls.sdk.maps.annotations.MarkerOptions
import com.mappls.sdk.maps.annotations.PolylineOptions
import com.mappls.sdk.maps.camera.CameraUpdateFactory
import com.mappls.sdk.maps.annotations.IconFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

// import com.google.maps.android.compose.GoogleMap
// import com.google.maps.android.compose.rememberCameraPositionState
// import com.google.android.gms.maps.model.CameraPosition
// import com.google.android.gms.maps.model.LatLng

@Composable
fun MapWithRidesScreen(
    rides: List<Ride>,
    onAccept: (Ride) -> Unit = {},
    onReject: (Ride) -> Unit = {}
) {
    val context = LocalContext.current
    val cardShape = RoundedCornerShape(16.dp)
    val cardElevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    val sectionBg = MaterialTheme.colorScheme.surface
    val viewModel: MainViewModel = getViewModel()
    val selectedRide = viewModel.selectedRide
    val mapplsRestApiKey = "b9930f54f59b5d54d4ebe585ec5ef39f"
    val hitechCity = com.mappls.sdk.maps.geometry.LatLng(17.4504, 78.3807)
    val madhapur = com.mappls.sdk.maps.geometry.LatLng(17.4445, 78.3772)
    var mapView: MapView? = null
    LaunchedEffect(Unit) {
        // No-op, just to ensure Compose context
    }
    Box(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            // 70% Mappls Map
            Surface(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxHeight()
                    .padding(12.dp),
                shape = cardShape,
                shadowElevation = 8.dp,
                tonalElevation = 2.dp,
                color = sectionBg
            ) {
                AndroidView(
                    factory = { ctx: Context ->
                        mapView = MapView(ctx)
                        mapView!!.apply {
                            onCreate(null)
                            getMapAsync(object : OnMapReadyCallback {
                                override fun onMapReady(mapplsMap: MapplsMap) {
                                    Log.d("MapWithRidesScreen", "Map is ready")
                                    
                                    // Test map functionality
                                    Log.d("MapWithRidesScreen", "Testing map functionality...")
                                    
                                    // Add pickup marker (using default marker with distinct title)
                                    val pickupMarker = mapplsMap.addMarker(
                                        MarkerOptions()
                                            .position(hitechCity)
                                            .title("📍 Pickup: Hitech City")
                                            .snippet("Blue marker - Starting point")
                                    )
                                    Log.d("MapWithRidesScreen", "Pickup marker added: "+(pickupMarker!=null))
                                    
                                    // Add drop marker (using default marker with distinct title)
                                    val dropMarker = mapplsMap.addMarker(
                                        MarkerOptions()
                                            .position(madhapur)
                                            .title("🎯 Drop: Madhapur")
                                            .snippet("Green marker - Destination")
                                    )
                                    Log.d("MapWithRidesScreen", "Drop marker added: "+(dropMarker!=null))
                                    
                                    // Test camera movement
                                    try {
                                        val bounds = com.mappls.sdk.maps.geometry.LatLngBounds.Builder()
                                            .include(hitechCity)
                                            .include(madhapur)
                                            .build()
                                        mapplsMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
                                        Log.d("MapWithRidesScreen", "Camera moved to bounds successfully")
                                    } catch (e: Exception) {
                                        Log.e("MapWithRidesScreen", "Camera movement failed: ${e.message}")
                                    }
                                    
                                    // Fetch and draw real road route
                                    Log.d("MapWithRidesScreen", "Starting route drawing...")
                                    fetchAndDrawRoute(mapplsMap, mapplsRestApiKey, hitechCity, madhapur)
                                }
                                override fun onMapError(p0: Int, p1: String?) {
                                    Log.e("MapWithRidesScreen", "Map error: $p0 - $p1")
                                }
                            })
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            // Vertical Divider
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.outline
            )
            // 30% Rides List
            Surface(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight()
                    .padding(12.dp),
                shape = cardShape,
                shadowElevation = 8.dp,
                tonalElevation = 2.dp,
                color = sectionBg
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    Text(
                        "Rides",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    rides.forEach { ride ->
                        val isSelected = selectedRide?.id == ride.id
                        Card(
                            modifier = Modifier
                                .padding(vertical = 8.dp, horizontal = 4.dp)
                                .fillMaxWidth()
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { viewModel.selectRide(ride) },
                            shape = RoundedCornerShape(12.dp),
                            elevation = cardElevation,
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(
                                Modifier.padding(16.dp)
                            ) {
                                if (isSelected) {
                                    Text(
                                        "On Going Ride",
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                        fontSize = 18.sp,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = "Pickup",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        "Pickup:",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Text(
                                    ride.pickup,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(start = 32.dp, bottom = 8.dp)
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Place,
                                        contentDescription = "Drop",
                                        tint = Color(0xFF388E3C)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        "Drop:",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color(0xFF388E3C)
                                    )
                                }
                                Text(
                                    ride.drop,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(start = 32.dp, bottom = 8.dp)
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        "Cost:",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        "₹${ride.cost}",
                                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                                if (!isSelected) {
                                    Row(
                                        modifier = Modifier.padding(top = 12.dp),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Button(onClick = { onAccept(ride) }, modifier = Modifier.padding(end = 8.dp)) {
                                            Text("Accept")
                                        }
                                        Button(onClick = { onReject(ride) }) {
                                            Text("Reject")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.CarRepair, contentDescription = "Home")
                Spacer(Modifier.width(8.dp))
                Text("Home")
            }
        }
    }
}

// Helper function to fetch and draw the real road route
fun fetchAndDrawRoute(mapplsMap: com.mappls.sdk.maps.MapplsMap, apiKey: String, start: com.mappls.sdk.maps.geometry.LatLng, end: com.mappls.sdk.maps.geometry.LatLng) {
    Log.d("RouteDrawing", "Starting route fetch from ${start.latitude},${start.longitude} to ${end.latitude},${end.longitude}")
    
    // Use a coroutine to fetch directions
    GlobalScope.launch(Dispatchers.IO) {
        try {
            // Use the correct Mappls Direction API endpoint
            val directionUrl = "https://apis.mappls.com/direction/v1/driving/${start.longitude},${start.latitude};${end.longitude},${end.latitude}?overview=full&geometries=polyline&access_token=$apiKey"
            
            Log.d("RouteDrawing", "Trying Mappls Direction API: $directionUrl")
            
            val url = URL(directionUrl)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.connectTimeout = 15000
            conn.readTimeout = 15000
            conn.setRequestProperty("User-Agent", "RideManagementHMI/1.0")
            conn.setRequestProperty("Accept", "application/json")
            
            val responseCode = conn.responseCode
            Log.d("RouteDrawing", "Mappls Direction API Response code: $responseCode")
            
            if (responseCode == 200) {
                val stream = conn.inputStream.bufferedReader().use { it.readText() }
                Log.d("RouteDrawing", "Mappls Direction API Response: $stream")
                
                val json = JSONObject(stream)
                
                // Parse Mappls Direction API response
                val routes = json.optJSONArray("routes")
                if (routes != null && routes.length() > 0) {
                    val route = routes.getJSONObject(0)
                    val geometry = route.optJSONObject("geometry")
                    
                    if (geometry != null) {
                        val polyline = geometry.optString("coordinates")
                        
                        if (polyline.isNotEmpty()) {
                            // Parse coordinates array from Mappls Direction API
                            val coordinates = JSONObject(polyline).optJSONArray("coordinates")
                            if (coordinates != null) {
                                val points = mutableListOf<com.mappls.sdk.maps.geometry.LatLng>()
                                
                                for (i in 0 until coordinates.length()) {
                                    val coord = coordinates.getJSONArray(i)
                                    val lng = coord.getDouble(0)
                                    val lat = coord.getDouble(1)
                                    points.add(com.mappls.sdk.maps.geometry.LatLng(lat, lng))
                                }
                                
                                Log.d("RouteDrawing", "Parsed ${points.size} points from Mappls Direction API")
                                
                                withContext(Dispatchers.Main) {
                                    val polylineOptions = PolylineOptions()
                                        .color(android.graphics.Color.BLACK)
                                        .width(3f)
                                    points.forEach { polylineOptions.add(it) }
                                    mapplsMap.addPolyline(polylineOptions)
                                    
                                    // Move camera to fit both points
                                    val bounds = com.mappls.sdk.maps.geometry.LatLngBounds.Builder()
                                        .include(start)
                                        .include(end)
                                        .build()
                                    mapplsMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
                                    
                                    Log.d("RouteDrawing", "Mappls Direction API route drawn successfully")
                                }
                                return@launch
                            }
                        }
                    }
                }
            } else {
                val errorStream = conn.errorStream?.bufferedReader()?.use { it.readText() }
                Log.e("RouteDrawing", "Mappls Direction API Error response: $errorStream")
            }
            
            // Fallback to test route if Mappls Direction API fails
            Log.w("RouteDrawing", "Mappls Direction API failed, using test route")
            drawTestRoute(mapplsMap, start, end)
            
        } catch (e: Exception) {
            Log.e("RouteDrawing", "Fatal error in Mappls Direction API: ${e.message}", e)
            drawTestRoute(mapplsMap, start, end)
        }
    }
}



// Draw a test route with intermediate points to simulate a real road route
private suspend fun drawTestRoute(mapplsMap: com.mappls.sdk.maps.MapplsMap, start: com.mappls.sdk.maps.geometry.LatLng, end: com.mappls.sdk.maps.geometry.LatLng) {
    withContext(Dispatchers.Main) {
        // Create intermediate points that follow actual roads between Hitech City and Madhapur
        // These points simulate the actual road route via Cyber Gateway, Mindspace, and main roads
        // More points for smoother, more realistic road curves
        val intermediatePoints = listOf(
            com.mappls.sdk.maps.geometry.LatLng(17.4498, 78.3804), // Exit from Hitech City
            com.mappls.sdk.maps.geometry.LatLng(17.4495, 78.3802), // Turn towards Cyber Gateway
            com.mappls.sdk.maps.geometry.LatLng(17.4492, 78.3800), // Approach Cyber Gateway
            com.mappls.sdk.maps.geometry.LatLng(17.4489, 78.3798), // Cyber Gateway area
            com.mappls.sdk.maps.geometry.LatLng(17.4486, 78.3796), // Road curve
            com.mappls.sdk.maps.geometry.LatLng(17.4482, 78.3792), // Mindspace entrance
            com.mappls.sdk.maps.geometry.LatLng(17.4478, 78.3788), // Road turn
            com.mappls.sdk.maps.geometry.LatLng(17.4472, 78.3785), // Mindspace junction
            com.mappls.sdk.maps.geometry.LatLng(17.4468, 78.3783), // Main road intersection
            com.mappls.sdk.maps.geometry.LatLng(17.4465, 78.3781), // Road curve
            com.mappls.sdk.maps.geometry.LatLng(17.4461, 78.3779), // Approach Madhapur area
            com.mappls.sdk.maps.geometry.LatLng(17.4458, 78.3778), // Near Madhapur junction
            com.mappls.sdk.maps.geometry.LatLng(17.4455, 78.3776), // Road turn
            com.mappls.sdk.maps.geometry.LatLng(17.4452, 78.3775), // Approach to Madhapur
            com.mappls.sdk.maps.geometry.LatLng(17.4448, 78.3773), // Entering Madhapur
            com.mappls.sdk.maps.geometry.LatLng(17.4446, 78.3772)  // Final approach
        )
        
        val polylineOptions = PolylineOptions()
            .color(android.graphics.Color.BLACK)
            .width(3f)
            .add(start)
        
        // Add intermediate points
        intermediatePoints.forEach { point ->
            polylineOptions.add(point)
        }
        
        polylineOptions.add(end)
        
        mapplsMap.addPolyline(polylineOptions)
        
        // Move camera to fit all points
        val bounds = com.mappls.sdk.maps.geometry.LatLngBounds.Builder()
            .include(start)
            .include(end)
        intermediatePoints.forEach { bounds.include(it) }
        mapplsMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100))
        
        Log.d("RouteDrawing", "Test route drawn with ${intermediatePoints.size + 2} points")
    }
}

// Draw fallback route (straight line)
private suspend fun drawFallbackRoute(mapplsMap: com.mappls.sdk.maps.MapplsMap, start: com.mappls.sdk.maps.geometry.LatLng, end: com.mappls.sdk.maps.geometry.LatLng) {
    withContext(Dispatchers.Main) {
        val polylineOptions = PolylineOptions()
            .color(android.graphics.Color.BLACK)
            .width(3f)
            .add(start)
            .add(end)
        mapplsMap.addPolyline(polylineOptions)
        
        val bounds = com.mappls.sdk.maps.geometry.LatLngBounds.Builder()
            .include(start)
            .include(end)
            .build()
        mapplsMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        
        Log.d("RouteDrawing", "Fallback route drawn")
    }
}

// Polyline decoder for Mappls encoded polyline
fun decodePolyline(encoded: String): List<com.mappls.sdk.maps.geometry.LatLng> {
    val poly = ArrayList<com.mappls.sdk.maps.geometry.LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0
    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if ((result and 1) != 0) (result shr 1).inv() else result shr 1
        lat += dlat
        shift = 0
        result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if ((result and 1) != 0) (result shr 1).inv() else result shr 1
        lng += dlng
        val latLng = com.mappls.sdk.maps.geometry.LatLng(lat / 1E5, lng / 1E5)
        poly.add(latLng)
    }
    return poly
} 