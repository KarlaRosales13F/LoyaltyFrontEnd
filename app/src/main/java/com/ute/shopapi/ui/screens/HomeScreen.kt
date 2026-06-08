package com.ute.shopapi.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ute.shopapi.data.controller.AuthController
import com.ute.shopapi.data.controller.ComprasController
import com.ute.shopapi.data.controller.PuntosController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToCompras: () -> Unit,
    onNavigateToRecompensas: () -> Unit,
    onNavigateToAdmin: () -> Unit,
    onNavigateToTienda: () -> Unit,
    onNavigateToPerfil: () -> Unit,
    onNavigateToOfertas: () -> Unit,
    authController: AuthController,
    puntosController: PuntosController,
    comprasController: ComprasController,
) {
    val currentUser by authController.currentUser.collectAsState()
    val puntos by puntosController.puntos.collectAsState()
    val compras by comprasController.compras.collectAsState()
    val isStaff = currentUser?.isStaff == true

    LaunchedEffect(currentUser) {
        currentUser?.id?.let {
            puntosController.fetchPuntos(it)
            comprasController.fetchCompras()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "LOYALTY",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                },
                actions = {
                    IconButton(onClick = { 
                        currentUser?.id?.let { puntosController.fetchPuntos(it) }
                        comprasController.fetchCompras()
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refrescar")
                    }
                    if (isStaff) {
                        IconButton(onClick = onNavigateToAdmin) {
                            Icon(Icons.Default.Settings, contentDescription = "Admin")
                        }
                    }
                    IconButton(onClick = onNavigateToPerfil) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Virtual Membership Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .height(200.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(MaterialTheme.colorScheme.primary, Color(0xFF333333))
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("ELITE PASS", color = Color.White.copy(alpha = 0.6f), style = MaterialTheme.typography.labelMedium, letterSpacing = 2.sp)
                            Icon(Icons.Default.WifiTethering, contentDescription = null, tint = Color.White.copy(alpha = 0.4f))
                        }
                        
                        Column {
                            Text(
                                currentUser?.firstName?.uppercase() ?: "ATLETA",
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                "ID: ${currentUser?.id ?: "0000"}",
                                color = Color.White.copy(alpha = 0.5f),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                            Column {
                                Text("NIVEL ACTUAL", color = Color.White.copy(alpha = 0.5f), style = MaterialTheme.typography.labelSmall)
                                Text(
                                    puntos?.nivelCliente?.uppercase() ?: "BRONCE",
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Icon(Icons.Default.AllInclusive, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(32.dp))
                        }
                    }
                }
            }

            // Points Summary
            item {
                Surface(
                    modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("SALDO DE PUNTOS", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                            Text("${puntos?.puntosDisponibles ?: 0}", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Black)
                        }
                        Button(
                            onClick = onNavigateToRecompensas,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Icon(Icons.Default.Stars, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("USAR PUNTOS", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Explorer Sections
            item {
                SectionHeader(title = "EXPLORAR SERVICIOS")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item { ServiceItem("TIENDA", Icons.Default.ShoppingCart, onNavigateToTienda) }
                    item { ServiceItem("COMPRAS", Icons.Default.History, onNavigateToCompras) }
                    item { ServiceItem("OFERTAS", Icons.Default.LocalOffer, onNavigateToOfertas) }
                }
            }

            // Level Progress Section
            item {
                SectionHeader(title = "PROGRESO DE NIVEL")
                Surface(
                    modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        val currentPoints = puntos?.puntosAcumulados ?: 0
                        val (nextLevel, nextLevelPoints) = when {
                            currentPoints < 800 -> "PLATA" to 800
                            currentPoints < 2000 -> "ORO" to 2000
                            currentPoints < 5000 -> "PLATINO" to 5000
                            else -> "MÁXIMO" to currentPoints
                        }
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Hacia nivel $nextLevel", style = MaterialTheme.typography.labelMedium)
                            val remaining = if (nextLevelPoints > currentPoints) nextLevelPoints - currentPoints else 0
                            Text("$remaining pts faltantes", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                        }
                        
                        Spacer(Modifier.height(12.dp))
                        
                        val progress = if (nextLevel == "MÁXIMO") 1f else (currentPoints.toFloat() / nextLevelPoints)
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }

            // Recent Activity Section
            item {
                SectionHeader(title = "ACTIVIDAD RECIENTE")
            }

            if (compras.isEmpty()) {
                item {
                    Text(
                        "No hay actividad reciente.",
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }
            } else {
                items(compras.take(3)) { compra ->
                    Surface(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp).fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        shadowElevation = 1.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(20.dp))
                            }
                            Spacer(Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Compra #${compra.id}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                Text(compra.fechaCompra.split("T")[0], style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                            }
                            Text("+$${compra.total}", fontWeight = FontWeight.Black, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(40.dp)) }
        }
    }
}

@Composable
fun ServiceItem(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.size(width = 110.dp, height = 120.dp).clickable { onClick() },
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(28.dp))
            Spacer(Modifier.height(12.dp))
            Text(label, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, letterSpacing = 1.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
    }
}
