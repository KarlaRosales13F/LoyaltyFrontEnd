package com.ute.shopapi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ute.shopapi.data.controller.AdminController
import com.ute.shopapi.data.controller.ComprasController
import com.ute.shopapi.data.model.CompraRequest
import com.ute.shopapi.data.model.Recompensa

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiendaScreen(
    onNavigateBack: () -> Unit,
    adminController: AdminController,
    comprasController: ComprasController
) {
    val recompensas by adminController.recompensas.collectAsState()
    val pagination by adminController.pagination.collectAsState()
    val isLoading by adminController.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        adminController.fetchRecompensas()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("TIENDA DEPORTIVA", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = { adminController.fetchRecompensas() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refrescar")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (isLoading && recompensas.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.weight(1f).padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (recompensas.isEmpty()) {
                        item {
                            Text("La tienda está vacía.", modifier = Modifier.padding(16.dp))
                        }
                    }
                    items(recompensas) { recompensa ->
                        RecompensaCard(recompensa) {
                            comprasController.createCompra(CompraRequest(metodoPago = "Tarjeta", total = 100.0))
                        }
                    }
                }

                // Pagination Row
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { pagination?.previous?.let { adminController.fetchRecompensas(it) } },
                        enabled = pagination?.previous != null && !isLoading
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        Text("ATRÁS")
                    }

                    Text("Pág ${pagination?.page ?: 1}", style = MaterialTheme.typography.labelLarge)

                    TextButton(
                        onClick = { pagination?.next?.let { adminController.fetchRecompensas(it) } },
                        enabled = pagination?.next != null && !isLoading
                    ) {
                        Text("MÁS")
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Composable
fun RecompensaCard(recompensa: Recompensa, onBuy: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(recompensa.nombre, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(recompensa.descripcion ?: "", style = MaterialTheme.typography.bodySmall, maxLines = 2)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${recompensa.puntosNecesarios} PTS", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                IconButton(onClick = onBuy) {
                    Icon(Icons.Default.AddShoppingCart, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
            }
            Text("STOCK: ${recompensa.stock}", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        }
    }
}
