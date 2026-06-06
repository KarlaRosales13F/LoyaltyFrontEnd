package com.ute.shopapi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ute.shopapi.data.controller.PuntosController
import com.ute.shopapi.data.controller.RecompensasController
import com.ute.shopapi.data.model.Recompensa

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecompensasScreen(
    onNavigateBack: () -> Unit,
    recompensasController: RecompensasController,
    puntosController: PuntosController
) {
    val recompensas by recompensasController.recompensas.collectAsState()
    val puntos by puntosController.puntos.collectAsState()

    LaunchedEffect(Unit) {
        recompensasController.fetchRecompensas()
    }

    Scaffold(
        topBar = { 
            TopAppBar(
                title = { Text("CANJE DE RECOMPENSAS", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            ) 
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Card(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("TUS PUNTOS", style = MaterialTheme.typography.labelLarge)
                    Text(
                        "${puntos?.puntosAcumulados ?: 0} PTS",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(recompensas) { recompensa ->
                    RecompensaItemRow(
                        recompensa = recompensa,
                        canAfford = (puntos?.puntosAcumulados ?: 0) >= recompensa.puntosNecesarios,
                        onCanjear = { 
                            recompensasController.canjearRecompensa(recompensa.id) { 
                                recompensasController.fetchRecompensas() 
                            } 
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RecompensaItemRow(recompensa: Recompensa, canAfford: Boolean, onCanjear: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(1f)) {
                Text(recompensa.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                recompensa.descripcion?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall)
                }
                Text("${recompensa.puntosNecesarios} PUNTOS", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = onCanjear,
                enabled = canAfford && recompensa.stock > 0,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("CANJEAR")
            }
        }
    }
}
