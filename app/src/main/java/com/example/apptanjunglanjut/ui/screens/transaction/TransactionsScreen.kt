    package com.example.apptanjunglanjut.ui.screens.transaction

    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.foundation.shape.CircleShape
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.foundation.text.KeyboardOptions
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.*
    import androidx.compose.material3.*
    import androidx.compose.runtime.*
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.clip
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.text.TextRange
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.text.input.KeyboardType
    import androidx.compose.ui.text.input.TextFieldValue
    import androidx.compose.ui.unit.dp
    import com.example.apptanjunglanjut.MainViewModel
    import com.example.apptanjunglanjut.TransactionType
    import java.text.NumberFormat
    import java.util.*

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TransactionsScreen(viewModel: MainViewModel) {
        var tabIndex by remember { mutableStateOf(0) }
        val tabs = listOf("Kas Masuk", "Kas Keluar", "Operasional")
        val tabIcons = listOf(Icons.Default.ArrowDownward, Icons.Default.ArrowUpward, Icons.Default.Build)

        val selectedType = when (tabIndex) {
            0 -> TransactionType.IN
            1 -> TransactionType.OUT
            else -> TransactionType.OPER
        }

        val items by viewModel.getTransactionsByType(selectedType).collectAsState(initial = emptyList())

        var showDialog by remember { mutableStateOf(false) }
        var showDeleteConfirm by remember { mutableStateOf(false) }

        // --- State Dialog Detail ---
        var showDetailDialog by remember { mutableStateOf(false) }
        var selectedTitle by remember { mutableStateOf("") }
        var selectedAmount by remember { mutableStateOf(0L) }
        var selectedDescription by remember { mutableStateOf("") }
        var selectedTypeForDialog by remember { mutableStateOf(TransactionType.IN) }
        var selectedId by remember { mutableStateOf(0) }
        var showDeleteSingleConfirm by remember { mutableStateOf(false) }


        // --- State Dialog Edit ---
        var showEditDialog by remember { mutableStateOf(false) }

        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            modifier = Modifier.fillMaxSize()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                // --- Tab Menu ---
                TabRow(
                    selectedTabIndex = tabIndex,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = tabIndex == index,
                            onClick = { tabIndex = index },
                            text = { Text(title) },
                            icon = { Icon(tabIcons[index], contentDescription = title) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Daftar ${tabs[tabIndex]}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // --- Tombol Aksi ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = { showDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Tambah Transaksi")
                    }

                    TextButton(
                        onClick = { showDeleteConfirm = true },
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = "Hapus Semua", tint = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Hapus Semua")
                    }
                }

                // --- Dialog Konfirmasi Hapus Semua ---
                if (showDeleteConfirm) {
                    AlertDialog(
                        onDismissRequest = { showDeleteConfirm = false },
                        title = { Text("Konfirmasi Hapus") },
                        text = {
                            Text("Hapus semua data di ${selectedType.displayName}? Tindakan ini tidak dapat dibatalkan.")
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    viewModel.deleteAllTransactions(selectedType)
                                    showDeleteConfirm = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text("Ya, Hapus Semua")
                            }
                        },
                        dismissButton = {
                            OutlinedButton(onClick = { showDeleteConfirm = false }) {
                                Text("Batal")
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // --- Daftar Transaksi ---
                if (items.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Belum ada transaksi untuk kategori ini.")
                    }
                } else {
                    LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                        items(items) { tx ->
                            TransactionItem(
                                title = tx.title,
                                amount = tx.amount,
                                description = tx.description,
                                type = selectedType,
                                onClick = {
                                    selectedTitle = tx.title
                                    selectedAmount = tx.amount
                                    selectedDescription = tx.description
                                    selectedTypeForDialog = selectedType
                                    selectedId = tx.id
                                    showDetailDialog = true
                                }
                            )
                        }
                    }
                }
            } // Akhir Column

            // --- Dialog Tambah Transaksi ---
            if (showDialog) {
                AddTransactionDialog(
                    type = selectedType,
                    onDismiss = { showDialog = false },
                    onSave = { title, amount, description ->
                        viewModel.insertTransaction(title, amount, description, selectedType)
                        showDialog = false
                    }
                )
            }

            // --- Dialog Detail Transaksi ---
            if (showDetailDialog) {
                val (icon, tintColor) = when (selectedTypeForDialog) {
                    TransactionType.IN -> Pair(Icons.Default.ArrowDownward, Color(0xFF2E7D32))
                    TransactionType.OUT -> Pair(Icons.Default.ArrowUpward, Color(0xFFD32F2F))
                    TransactionType.OPER -> Pair(Icons.Default.AccountBalanceWallet, Color(0xFF1565C0))
                }

                AlertDialog(
                    onDismissRequest = { showDetailDialog = false },
                    title = { Text("Detail Transaksi") },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                            // --- Bagian konten detail ---
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    color = tintColor.copy(alpha = 0.15f),
                                    shape = CircleShape,
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = tintColor,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                                Text(
                                    selectedTitle,
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                                )
                            }

                            Text("Jumlah", style = MaterialTheme.typography.labelMedium)
                            Text(
                                "Rp ${NumberFormat.getNumberInstance(Locale("in","ID")).format(selectedAmount)}",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                color = tintColor
                            )

                            if (selectedDescription.isNotBlank()) {
                                Text("Keterangan", style = MaterialTheme.typography.labelMedium)
                                Text(selectedDescription)
                            }

                            // --- Tombol Aksi (Edit & Hapus) ---
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        showDeleteSingleConfirm = true
                                    },

                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = null)
                                    Spacer(Modifier.width(4.dp))
                                    Text("Hapus")
                                }

                                Button(onClick = {
                                    showDetailDialog = false
                                    showEditDialog = true
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = null)
                                    Spacer(Modifier.width(4.dp))
                                    Text("Edit")
                                }
                            }

                            // --- Tombol Tutup di bawah sendiri ---
                            Button(
                                onClick = { showDetailDialog = false },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Tutup")
                            }
                        }
                    },
                    confirmButton = { /* kosongkan */ },
                    dismissButton = { /* kosongkan */ }
                )

            }
            // --- Dialog Konfirmasi Hapus Satu Transaksi ---
            if (showDeleteSingleConfirm) {
                AlertDialog(
                    onDismissRequest = { showDeleteSingleConfirm = false },
                    title = { Text("Hapus Transaksi") },
                    text = {
                        Text("Apakah Anda yakin ingin menghapus transaksi \"${selectedTitle}\"?")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.deleteTransactionById(selectedId)
                                showDeleteSingleConfirm = false
                                showDetailDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Hapus")
                        }
                    },
                    dismissButton = {
                        OutlinedButton(onClick = { showDeleteSingleConfirm = false }) {
                            Text("Batal")
                        }
                    }
                )
            }


            // --- Dialog Edit Transaksi ---
            if (showEditDialog) {
                EditTransactionDialog(
                    type = selectedTypeForDialog,
                    initialTitle = selectedTitle,
                    initialAmount = selectedAmount,
                    initialDescription = selectedDescription,
                    onDismiss = { showEditDialog = false },
                    onSave = { title, amount, description ->
                        viewModel.updateTransaction(
                            id = selectedId,
                            title = title,
                            amount = amount,
                            description = description,
                            type = selectedTypeForDialog
                        )
                        showEditDialog = false
                    }
                )
            }
        } // Akhir Surface
    }



    // --- Dialog Tambah Transaksi ---
    @Composable
    fun AddTransactionDialog(
        type: TransactionType,
        onDismiss: () -> Unit,
        onSave: (String, Long, String) -> Unit
    ) {
        var title by remember { mutableStateOf("") }

        var amount by remember { mutableStateOf(TextFieldValue("")) }
        var rawAmount by remember { mutableStateOf("") }

        var description by remember { mutableStateOf("") }
        var showError by remember { mutableStateOf(false) }

        var isTitleValid by remember(title) { mutableStateOf(title.isNotBlank()) }
        var isAmountValid by remember(rawAmount) { mutableStateOf(rawAmount.isNotBlank()) }
        var isDescriptionValid by remember(description) { mutableStateOf(description.isNotBlank()) }
        val allFilled = isTitleValid && isAmountValid

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Tambah ${type.displayName}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                            isTitleValid = it.isNotBlank()
                        },
                        label = { Text("Judul Transaksi") },
                        leadingIcon = { Icon(Icons.Default.Edit, null) },
                        isError = showError && !isTitleValid,
                        supportingText = {
                            if (showError && !isTitleValid) {
                                Text("Judul wajib diisi", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = amount,
                        onValueChange = { newValue ->
                            val cleanValue = newValue.text.replace("[^\\d]".toRegex(), "")
                            rawAmount = cleanValue
                            isAmountValid = cleanValue.isNotBlank()

                            val formatted = if (cleanValue.isNotEmpty()) {
                                try {
                                    val number = cleanValue.toLong()
                                    NumberFormat.getNumberInstance(Locale("in", "ID"))
                                        .format(number)
                                } catch (e: NumberFormatException) {
                                    ""
                                }
                            } else ""

                            amount = TextFieldValue(
                                text = formatted,
                                selection = TextRange(formatted.length)
                            )
                        },
                        label = { Text("Jumlah (Rp)") },
                        leadingIcon = { Icon(Icons.Default.Payments, contentDescription = null) },
                        isError = showError && !isAmountValid,
                        supportingText = {
                            if (showError && !isAmountValid) {
                                Text("Jumlah wajib diisi", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = {
                            description = it
                            isDescriptionValid = it.isNotBlank()
                        },
                        label = { Text("Keterangan (Opsional)") },
                        leadingIcon = { Icon(Icons.Default.Description, null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isTitleValid = title.isNotBlank()
                        isAmountValid = rawAmount.isNotBlank()

                        if (isTitleValid && isAmountValid) {
                            onSave(title, rawAmount.toLongOrNull() ?: 0L, description)
                        } else {
                            showError = true
                        }
                    },
                    enabled = allFilled
                ) {
                    Text("Tambah")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = onDismiss) { Text("Batal") }
            }
        )
    }

    // --- Dialog Edit Transaksi ---
    @Composable
    fun EditTransactionDialog(
        type: TransactionType,
        initialTitle: String,
        initialAmount: Long,
        initialDescription: String,
        onDismiss: () -> Unit,
        onSave: (String, Long, String) -> Unit
    ) {
        // Format awal amount
        val formattedInitialAmount = remember(initialAmount) {
            if (initialAmount > 0) {
                NumberFormat.getNumberInstance(Locale("in", "ID")).format(initialAmount)
            } else ""
        }

        var title by remember { mutableStateOf(initialTitle) }

        var amount by remember {
            mutableStateOf(
                TextFieldValue(
                    text = formattedInitialAmount,
                    selection = TextRange(formattedInitialAmount.length)
                )
            )
        }
        var rawAmount by remember { mutableStateOf(if (initialAmount > 0) initialAmount.toString() else "") }

        var description by remember { mutableStateOf(initialDescription) }
        var showError by remember { mutableStateOf(false) }

        var isTitleValid by remember(title) { mutableStateOf(title.isNotBlank()) }
        var isAmountValid by remember(rawAmount) { mutableStateOf(rawAmount.isNotBlank()) }
        var isDescriptionValid by remember(description) { mutableStateOf(description.isNotBlank()) }
        val allFilled = isTitleValid && isAmountValid

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Edit ${type.displayName}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                            isTitleValid = it.isNotBlank()
                        },
                        label = { Text("Judul Transaksi") },
                        leadingIcon = { Icon(Icons.Default.Edit, null) },
                        isError = showError && !isTitleValid,
                        supportingText = {
                            if (showError && !isTitleValid) {
                                Text("Judul wajib diisi", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = amount,
                        onValueChange = { newValue ->
                            val cleanValue = newValue.text.replace("[^\\d]".toRegex(), "")
                            rawAmount = cleanValue
                            isAmountValid = cleanValue.isNotBlank()

                            val formatted = if (cleanValue.isNotEmpty()) {
                                try {
                                    val number = cleanValue.toLong()
                                    NumberFormat.getNumberInstance(Locale("in", "ID"))
                                        .format(number)
                                } catch (e: NumberFormatException) {
                                    ""
                                }
                            } else ""

                            amount = TextFieldValue(
                                text = formatted,
                                selection = TextRange(formatted.length)
                            )
                        },
                        label = { Text("Jumlah (Rp)") },
                        leadingIcon = { Icon(Icons.Default.Payments, contentDescription = null) },
                        isError = showError && !isAmountValid,
                        supportingText = {
                            if (showError && !isAmountValid) {
                                Text("Jumlah wajib diisi", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = {
                            description = it
                            isDescriptionValid = it.isNotBlank()
                        },
                        label = { Text("Keterangan (Opsional)") },
                        leadingIcon = { Icon(Icons.Default.Description, null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isTitleValid = title.isNotBlank()
                        isAmountValid = rawAmount.isNotBlank()

                        if (isTitleValid && isAmountValid) {
                            onSave(title, rawAmount.toLongOrNull() ?: 0L, description)
                        } else {
                            showError = true
                        }
                    },
                    enabled = allFilled
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = onDismiss) { Text("Batal") }
            }
        )
    }
