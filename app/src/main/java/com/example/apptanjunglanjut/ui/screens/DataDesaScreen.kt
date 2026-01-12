package com.example.apptanjunglanjut.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.apptanjunglanjut.MainViewModel
import com.example.apptanjunglanjut.data.entity.AssetEntity
import com.example.apptanjunglanjut.data.entity.IncentiveEntity
import com.example.apptanjunglanjut.data.entity.BudgetEntity
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataDesaScreen(viewModel: MainViewModel) {

    val assets = viewModel.assets.collectAsState(initial = emptyList())
    val incentives = viewModel.incentives.collectAsState(initial = emptyList())
    val budgets = viewModel.budgets.collectAsState(initial = emptyList())

    // STATE INPUT DIALOG
    var showDialog by remember { mutableStateOf(false) }
    var isEditMode by remember { mutableStateOf(false) }
    var editItemId by remember { mutableStateOf(0) }
    var sectionType by remember { mutableStateOf("") }

    var name by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf(TextFieldValue("")) }
    var rawAmount by remember { mutableStateOf("") }

    // STATE DETAIL
    var showDetail by remember { mutableStateOf(false) }
    var detailTitle by remember { mutableStateOf("") }
    var detailAmount by remember { mutableStateOf(0L) }
    var detailNote by remember { mutableStateOf("") }
    var detailIcon by remember { mutableStateOf(Icons.Default.Inventory2) }
    var detailType by remember { mutableStateOf("") }
    var detailId by remember { mutableStateOf(0) }

    // STATE DELETE CONFIRMATION
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var deleteId by remember { mutableStateOf(0) }
    var deleteType by remember { mutableStateOf("") }

    fun resetForm() {
        name = ""
        note = ""
        amount = TextFieldValue("")
        rawAmount = ""
        isEditMode = false
        editItemId = 0
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        item {
            Text(
                "Data Desa 💼",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                "Kelola aset, insentif, dan anggaran desa dengan mudah.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // ================== SECTION: ASET ==================
        item {
            SectionBlock(
                title = "Daftar Aset Desa",
                description = "Semua aset milik desa",
                items = assets.value,
                icon = Icons.Default.Inventory2,
                onAdd = {
                    sectionType = "ASSET"
                    resetForm()
                    showDialog = true
                },
                onDeleteAll = {
                    viewModel.deleteAllAssets()
                },
                onItemClick = { item ->
                    val a = item as AssetEntity
                    detailId = a.id
                    detailTitle = a.name
                    detailAmount = a.value
                    detailNote = a.note
                    detailIcon = Icons.Default.Inventory2
                    detailType = "ASSET"
                    showDetail = true
                }
            )
        }

        // ================== SECTION: INSENTIF ==================
        item {
            SectionBlock(
                title = "Daftar Insentif",
                description = "Insentif untuk aparat desa",
                items = incentives.value,
                icon = Icons.Default.Money,
                onAdd = {
                    sectionType = "INCENTIVE"
                    resetForm()
                    showDialog = true
                },
                onDeleteAll = {
                    viewModel.deleteAllIncentives()
                },
                onItemClick = { item ->
                    val i = item as IncentiveEntity
                    detailId = i.id
                    detailTitle = i.name
                    detailAmount = i.amount
                    detailNote = i.note
                    detailIcon = Icons.Default.Money
                    detailType = "INCENTIVE"
                    showDetail = true
                }
            )
        }

        // ================== SECTION: BUDGET ==================
        item {
            SectionBlock(
                title = "Daftar Anggaran",
                description = "Anggaran desa",
                items = budgets.value,
                icon = Icons.Default.AccountBalance,
                onAdd = {
                    sectionType = "BUDGET"
                    resetForm()
                    showDialog = true
                },
                onDeleteAll = {
                    viewModel.deleteAllBudgets()
                },
                onItemClick = { item ->
                    val b = item as BudgetEntity
                    detailId = b.id
                    detailTitle = b.name
                    detailAmount = b.amount
                    detailNote = b.note
                    detailIcon = Icons.Default.AccountBalance
                    detailType = "BUDGET"
                    showDetail = true
                }
            )
        }
    }

    // INPUT DIALOG (ADD / EDIT)
    if (showDialog) {

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(if (isEditMode) "Edit Data" else "Tambah Data")
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nama") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = amount,
                        onValueChange = { newValue ->
                            val clean = newValue.text.replace("[^\\d]".toRegex(), "")
                            rawAmount = clean
                            val formatted = if (clean.isNotEmpty()) {
                                NumberFormat.getNumberInstance(Locale("in", "ID"))
                                    .format(clean.toLong())
                            } else ""
                            amount = TextFieldValue(
                                formatted,
                                selection = TextRange(formatted.length)
                            )
                        },
                        label = { Text("Jumlah (Rp)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text("Keterangan (opsional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amt = rawAmount.toLongOrNull() ?: 0L

                        if (isEditMode) {
                            when (sectionType) {
                                "ASSET" -> viewModel.updateAsset(editItemId, name, amt, note)
                                "INCENTIVE" -> viewModel.updateIncentive(editItemId, name, amt, note)
                                "BUDGET" -> viewModel.updateBudget(editItemId, name, amt, note)
                            }
                        } else {
                            when (sectionType) {
                                "ASSET" -> viewModel.insertAsset(name, amt, note)
                                "INCENTIVE" -> viewModel.insertIncentive(name, amt, note)
                                "BUDGET" -> viewModel.insertBudget(name, amt, note)
                            }
                        }

                        showDialog = false
                        resetForm()
                    }
                ) {
                    Text(if (isEditMode) "Simpan Perubahan" else "Tambah")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    // DETAIL DIALOG
    if (showDetail) {
        AlertDialog(
            onDismissRequest = { showDetail = false },
            title = { Text("Detail Data") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {

                    Text(detailTitle, style = MaterialTheme.typography.titleLarge)
                    Text(
                        "Rp ${NumberFormat.getNumberInstance(Locale("in","ID")).format(detailAmount)}",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )

                    if (detailNote.isNotBlank()) {
                        Text("Keterangan:")
                        Text(detailNote)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Masuk ke edit
                        isEditMode = true
                        sectionType = detailType
                        editItemId = detailId
                        name = detailTitle
                        note = detailNote
                        rawAmount = detailAmount.toString()
                        amount = TextFieldValue(
                            NumberFormat.getNumberInstance(Locale("in","ID")).format(detailAmount)
                        )
                        showDetail = false
                        showDialog = true
                    }
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Edit")
                }
            },
            dismissButton = {
                Row {

                    OutlinedButton(
                        onClick = {
                            deleteId = detailId
                            deleteType = detailType
                            showDeleteConfirm = true
                            showDetail = false
                        }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Hapus")
                    }

                    Spacer(Modifier.width(8.dp))

                    OutlinedButton(onClick = { showDetail = false }) {
                        Text("Tutup")
                    }
                }
            }
        )
    }

    // CONFIRM DELETE SINGLE ITEM
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Hapus Data") },
            text = { Text("Yakin ingin menghapus data ini?") },
            confirmButton = {
                Button(
                    onClick = {
                        when (deleteType) {
                            "ASSET" -> viewModel.deleteAsset(deleteId)
                            "INCENTIVE" -> viewModel.deleteIncentive(deleteId)
                            "BUDGET" -> viewModel.deleteBudget(deleteId)
                        }
                        showDeleteConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteConfirm = false }) {
                    Text("Batal")
                }
            }
        )
    }

}

/* ======================================================================================= */
/* REUSABLE COMPONENTS */
/* ======================================================================================= */

@Composable
fun <T> SectionBlock(
    title: String,
    description: String,
    items: List<T>,
    icon: ImageVector,
    onAdd: () -> Unit,
    onDeleteAll: () -> Unit,
    onItemClick: (T) -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF386641).copy(alpha = 0.85f),
            contentColor = Color(0xFF386641)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFFFFFFFF)   // warna hijau gelap, bisa ganti sesukamu
                    )
                    Text(
                        description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFFFFFFF)
                    )
                }

                Row {
                    IconButton(onClick = onAdd) {
                        Icon(Icons.Default.Add, null, tint = Color(0xFFFFFFFF))
                    }
                    IconButton(onClick = onDeleteAll) {
                        Icon(Icons.Default.DeleteSweep, null, tint = MaterialTheme.colorScheme.error)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            items.forEach { item ->
                val (t, amt, note) = when (item) {
                    is AssetEntity -> Triple(item.name, item.value, item.note)
                    is IncentiveEntity -> Triple(item.name, item.amount, item.note)
                    is BudgetEntity -> Triple(item.name, item.amount, item.note)
                    else -> Triple("Unknown", 0L, "")
                }

                DataCard(
                    icon = icon,
                    title = t,
                    amount = amt,
                    note = note,
                    onClick = { onItemClick(item) }
                )

                Spacer(Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun DataCard(
    icon: ImageVector,
    title: String,
    amount: Long,
    note: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp)  // ★ Tambah padding card di sini
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),               // ★ Sudut lebih elegan
        elevation = CardDefaults.cardElevation(1.dp) ,    // ★ Tambah depth lebih lembut
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF8D4) // warna #FFF8D4
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),                        // ★ Padding isi card lebih lapang
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(34.dp)
            )

            Spacer(Modifier.width(14.dp))

            Column {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )

                Text(
                    "Rp ${NumberFormat.getNumberInstance(Locale("in","ID")).format(amount)}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )

                if (note.isNotBlank()) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        note,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

