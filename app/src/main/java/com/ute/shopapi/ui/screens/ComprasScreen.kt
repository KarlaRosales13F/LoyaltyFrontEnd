package com.ute.shopapi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ute.shopapi.data.controller.ComprasController
import com.ute.shopapi.data.model.Compra

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComprasScreen(
    onNavigateBack: () -> Unit,
    controller: ComprasController
) {
    val compras by controller.compras.collectAsState()
    val isLoading by controller.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        controller.fetchCompras()
    }

    Scaffold(
        topBar = { 
            TopAppBar(
                title = { Text("HISTORIAL DE COMPRAS", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            ) 
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(compras) { compra ->
                    CompraItemRow(compra)
                }
            }
        }
    }
}

@Composable
fun CompraItemRow(compra: Compra) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("ORDEN #${compra.id}", fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium)
                Text("$${compra.total}", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("FECHA: ${compra.fechaCompra.split("T")[0]}", style = MaterialTheme.typography.bodySmall)
            Text("MÉTODO: ${compra.metodoPago.uppercase()}", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
        }
    }
}
