package com.example.apptanjunglanjut

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

// 🔽 Tambahkan ini
import com.example.apptanjunglanjut.ui.screens.*
import com.example.apptanjunglanjut.ui.screens.transaction.TransactionsScreen

enum class BottomTab { Dashboard, Transactions, DataDesa }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(viewModel: MainViewModel) {
    var selected by remember { mutableStateOf(BottomTab.Dashboard) }

    Scaffold(
        topBar = {
            Box {
                // 🔹 Background Image
                Image(
                    painter = painterResource(id = R.drawable.bg_header),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(120.dp), // tinggi background
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0x80000000), Color.Transparent)
                            )
                        )
                )


                // 🔹 TopAppBar overlay di atas gambar
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 40.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logo_no_text),
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.White, CircleShape)
                            )

                            Text(
                                text = "SITAJA",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White // penting karena background gelap
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White
                    )
                )
            }
        },


    bottomBar = {
            NavigationBar(modifier = Modifier.animateContentSize()) {
                NavigationBarItem(
                    selected = selected == BottomTab.Dashboard,
                    onClick = { selected = BottomTab.Dashboard },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                    label = { Text("Dashboard") }
                )
                NavigationBarItem(
                    selected = selected == BottomTab.Transactions,
                    onClick = { selected = BottomTab.Transactions },
                    icon = { Icon(Icons.Default.History, contentDescription = "Transaksi") },
                    label = { Text("Transaksi") }
                )
                NavigationBarItem(
                    selected = selected == BottomTab.DataDesa,
                    onClick = { selected = BottomTab.DataDesa },
                    icon = { Icon(Icons.Default.Inventory, contentDescription = "Data Desa") },
                    label = { Text("Data Desa") }
                )
            }
        }
    ) { innerPadding ->
        Crossfade(targetState = selected, modifier = Modifier.padding(innerPadding)) { screen ->
            when (screen) {
                BottomTab.Dashboard -> DashboardScreen(viewModel = viewModel)
                BottomTab.Transactions -> TransactionsScreen(viewModel = viewModel)
                BottomTab.DataDesa -> DataDesaScreen(viewModel = viewModel)
            }
        }
    }
}
