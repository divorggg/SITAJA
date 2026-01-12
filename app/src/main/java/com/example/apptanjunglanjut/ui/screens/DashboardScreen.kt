package com.example.apptanjunglanjut.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue // <-- IMPORT TAMBAHAN
import androidx.compose.runtime.mutableStateOf // <-- IMPORT TAMBAHAN
import androidx.compose.runtime.remember // <-- IMPORT TAMBAHAN
import androidx.compose.runtime.setValue // <-- IMPORT TAMBAHAN
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.apptanjunglanjut.MainViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class) // <-- TAMBAHAN
@Composable
fun DashboardScreen(viewModel: MainViewModel) {
    val assets = viewModel.assets.collectAsState(initial = emptyList())
    val transactions = viewModel.transactions.collectAsState(initial = emptyList())
    val incentives = viewModel.incentives.collectAsState(initial = emptyList())
    val budget = viewModel.budgets.collectAsState(initial = emptyList())

    val totalIn = transactions.value.filter { it.type == "IN" }.sumOf { it.amount }
    val totalOut = transactions.value.filter { it.type == "OUT" }.sumOf { it.amount }
    val totalOper = transactions.value.filter { it.type == "OPER" }.sumOf { it.amount }

    val totalBudget = budget.value.sumOf { it.amount }

    // --- State Dialog Detail --- (BARU)
    var showDetailDialog by remember { mutableStateOf(false) }
    var selectedTitle by remember { mutableStateOf("") }
    var selectedAmount by remember { mutableStateOf(0L) }
    var selectedNote by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf(Icons.Default.Inventory2) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {

        // 👋 Header Modern & Engaging
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .animateContentSize(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF386641).copy(alpha = 0.85f)
                ),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "👋 Halo, User",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "Selamat datang di Sistem Informasi\nTanjung Lanjut (SITAJA).",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White
                            )
                        )
                        Text(
                            text = "Kelola dan pantau keuangan desa Anda secara mudah dan real-time.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White
                            )
                        )
                    }


                }
            }
        }


        // 📊 Ringkasan Keuangan
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "Ringkasan Keuangan",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        SummaryCard(
                            title = "Total Anggaran",
                            amount = totalBudget,
                            icon = Icons.Default.PieChart,
                            modifier = Modifier.weight(1f)
                        )
                        SummaryCard(
                            title = "Operasional",
                            amount = totalOper,
                            icon = Icons.Default.Business,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        SummaryCard(
                            title = "Kas Masuk",
                            amount = totalIn,
                            icon = Icons.Default.TrendingUp,
                            modifier = Modifier.weight(1f)
                        )
                        SummaryCard(
                            title = "Kas Keluar",
                            amount = totalOut,
                            icon = Icons.Default.TrendingDown,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // 🏠 Aset Desa
        if (assets.value.isNotEmpty()) {
            item {
                SectionTitle("Aset Desa")
            }
            items(assets.value) { asset ->
                DataCard(
                    icon = Icons.Default.Inventory2,
                    title = asset.name,
                    amount = asset.value,
                    note = asset.note,
                    onClick = { // <-- TAMBAHAN
                        selectedTitle = asset.name
                        selectedAmount = asset.value
                        selectedNote = asset.note
                        selectedIcon = Icons.Default.Inventory2
                        showDetailDialog = true
                    }
                )
            }
        }

        // 💰 Insentif
        if (incentives.value.isNotEmpty()) {
            item {
                SectionTitle("Insentif")
            }
            items(incentives.value) { ins ->
                DataCard(
                    icon = Icons.Default.Money,
                    title = ins.name,
                    amount = ins.amount,
                    note = ins.note,
                    onClick = { // <-- TAMBAHAN
                        selectedTitle = ins.name
                        selectedAmount = ins.amount
                        selectedNote = ins.note
                        selectedIcon = Icons.Default.Money
                        showDetailDialog = true
                    }
                )
            }
        }

        // 📅 Anggaran
        if (budget.value.isNotEmpty()) {
            item {
                SectionTitle("Anggaran")
            }
            items(budget.value) { bud ->
                DataCard(
                    icon = Icons.Default.AccountBalance,
                    title = bud.name,
                    amount = bud.amount,
                    note = bud.note,
                    onClick = { // <-- TAMBAHAN
                        selectedTitle = bud.name
                        selectedAmount = bud.amount
                        selectedNote = bud.note
                        selectedIcon = Icons.Default.AccountBalance
                        showDetailDialog = true
                    }
                )
            }
        }
    }

    // ------------------ DIALOG DETAIL (BARU) ------------------
    if (showDetailDialog) {
        AlertDialog(
            onDismissRequest = { showDetailDialog = false },
            title = { Text("Detail Data") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Bagian Header (Ikon dan Judul)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            shape = CircleShape,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = selectedIcon,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Text(selectedTitle, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Bagian Jumlah (Amount)
                    Text(
                        text = "Jumlah",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Rp ${
                            NumberFormat.getNumberInstance(Locale("in", "ID")).format(selectedAmount)
                        }",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Bagian Keterangan (Note)
                    if (selectedNote.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Keterangan",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = selectedNote,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showDetailDialog = false }) {
                    Text("Tutup")
                }
            }
        )
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        modifier = Modifier.padding(top = 12.dp)
    )
}

@Composable
private fun SummaryCard(
    title: String,
    amount: Long,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .animateContentSize(animationSpec = tween(250)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF8D4) // warna #FFF8D4
        ),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF1F7D53).copy(alpha = 0.15f),
                    modifier = Modifier.size(34.dp),
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Text(
                text = "Rp ${NumberFormat.getNumberInstance(Locale("in", "ID")).format(amount)}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

