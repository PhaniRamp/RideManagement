package com.example.ridemanagementhmi.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.ridemanagementhmi.viewmodel.MainViewModel
import org.koin.androidx.compose.getViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CarRepair
import androidx.compose.material3.Button
import androidx.compose.material3.Icon


@Composable
fun AppGridScreen(viewModel: MainViewModel = getViewModel()) {
    val apps = viewModel.allApps
    val enabledApps = viewModel.enabledApps
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
        LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.fillMaxSize()) {
            items(apps) { app ->
                val isEnabled = enabledApps[app] == true
                Card(
                    modifier = Modifier
                        .padding(12.dp)
                        .border(2.dp, if (isEnabled) Color(0xFF00E676) else Color.Gray, RoundedCornerShape(16.dp))
                        .clickable(enabled = isEnabled) { if (isEnabled) viewModel.onAppSelected() },
                    shape = RoundedCornerShape(16.dp),
                    elevation = androidx.compose.material3.CardDefaults.elevatedCardElevation(defaultElevation = 12.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFF181A1B))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            app,
                            fontSize = 20.sp,
                            color = if (isEnabled) Color.White else Color.Gray,
                            fontWeight = if (isEnabled) androidx.compose.ui.text.font.FontWeight.Bold else null
                        )
                        Switch(
                            checked = isEnabled,
                            onCheckedChange = { viewModel.toggleAppEnabled(app) }
                        )
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
            Icon(Icons.Default.CarRepair, contentDescription = "Home")
            Spacer(Modifier.width(8.dp))
            Text("Home")
        }
    }
} 