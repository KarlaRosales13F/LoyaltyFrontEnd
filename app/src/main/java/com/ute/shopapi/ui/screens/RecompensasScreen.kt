package com.ute.shopapi.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ute.shopapi.data.controller.PuntosController
import com.ute.shopapi.data.controller.RecompensasController
import com.ute.shopapi.data.model.PuntosFidelizacion
import com.ute.shopapi.data.model.Recompensa
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecompensasScreen(
    onNavigateBack: () -> Unit,
    recompensasController: RecompensasController,
    puntosController: PuntosController
) {
    val recompensas by recompensasController.recompensas.collectAsState()
    val puntos by puntosController.puntos.collectAsState()
    val isLoading by recompensasController.isLoading.collectAsState()
    val error by recompensasController.error.collectAsState()
    
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    LaunchedEffect(Unit) {
        recompensasController.fetchRecompensas()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { 
            TopAppBar(
                title = { Text("CANJEAR", fontWeight = FontWeight.Bold, letterSpacing = 2.sp) },
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
            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(24.dp), style = MaterialTheme.typography.bodySmall)
            }

            Surface(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                shadowElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("TUS PUNTOS", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), letterSpacing = 2.sp)
                    Text(
                        (puntos?.puntosDisponibles ?: 0).toString(),
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            if (isLoading && recompensas.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(strokeWidth = 2.dp, color = MaterialTheme.colorScheme.primary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(recompensas) { recompensa ->
                        val currentBalance = puntos?.puntosDisponibles ?: 0
                        RecompensaItemRow(
                            recompensa = recompensa,
                            canAfford = currentBalance >= recompensa.puntosNecesarios,
                            isLoading = isLoading,
                            onCanjear = { 
                                scope.launch { snackbarHostState.showSnackbar("Procesando canje...") }
                                recompensasController.canjearRecompensa(recompensa.id) { puntosActualizados: PuntosFidelizacion ->
                                    scope.launch { snackbarHostState.showSnackbar("¡Canje exitoso!") }
                                    recompensasController.fetchRecompensas() 
                                    // Update points immediately from response
                                    puntosController.updatePuntosLocally(puntosActualizados)
                                } 
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecompensaItemRow(recompensa: Recompensa, canAfford: Boolean, isLoading: Boolean, onCanjear: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 2.dp
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(recompensa.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                recompensa.descripcion?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("${recompensa.puntosNecesarios} PUNTOS", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = onCanjear,
                enabled = canAfford && (recompensa.stock > 0) && !isLoading,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Canjear", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}
