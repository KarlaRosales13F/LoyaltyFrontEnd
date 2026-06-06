package com.ute.shopapi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ute.shopapi.data.controller.AuthController
import com.ute.shopapi.data.controller.PuntosController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToCompras: () -> Unit,
    onNavigateToRecompensas: () -> Unit,
    onNavigateToAdmin: () -> Unit,
    onNavigateToTienda: () -> Unit,
    onNavigateToPerfil: () -> Unit,
    authController: AuthController,
    puntosController: PuntosController
) {
    val currentUser by authController.currentUser.collectAsState()
    val puntos by puntosController.puntos.collectAsState()

    // Admin Access Check
    val isStaff = currentUser?.isStaff == true

    LaunchedEffect(currentUser) {
        currentUser?.id?.let {
            puntosController.fetchPuntos(it)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("LOYALTEE SPORTS", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    IconButton(onClick = onNavigateToPerfil) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "¡Hola, ${currentUser?.email?.split("@")?.get(0)?.uppercase() ?: "ATLETA"}!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            // Sports Point Card with Gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("PUNTOS ACUMULADOS", color = Color.White, style = MaterialTheme.typography.labelLarge)
                    Text(
                        "${puntos?.puntosAcumulados ?: 0}",
                        color = Color.White,
                        style = MaterialTheme.typography.displayLarge
                    )
                    Surface(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(
                            "NIVEL: ${puntos?.nivelActual?.uppercase() ?: "BRONCE"}",
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Large Action Buttons
            Button(
                onClick = onNavigateToTienda,
                modifier = Modifier.fillMaxWidth().height(80.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(28.dp))
                Spacer(Modifier.width(12.dp))
                Text("IR A LA TIENDA", fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = onNavigateToCompras,
                    modifier = Modifier.weight(1f).height(100.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurfaceVariant)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(32.dp))
                        Text("COMPRAS", fontWeight = FontWeight.Bold)
                    }
                }
                
                Button(
                    onClick = onNavigateToRecompensas,
                    modifier = Modifier.weight(1f).height(100.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Redeem, contentDescription = null, modifier = Modifier.size(32.dp))
                        Text("CANJES", fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            if (isStaff) {
                Button(
                    onClick = onNavigateToAdmin,
                    modifier = Modifier.fillMaxWidth().height(56.dp).padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Settings, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("PANEL DE ADMINISTRADOR", fontWeight = FontWeight.Black)
                }
            }
            
            Text(
                "ENTRENA • COMPRA • GANA",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                letterSpacing = 4.sp
            )
        }
    }
}
