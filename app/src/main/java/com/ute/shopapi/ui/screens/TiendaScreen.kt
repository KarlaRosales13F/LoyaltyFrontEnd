package com.ute.shopapi.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ute.shopapi.data.controller.*
import com.ute.shopapi.data.model.Producto
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiendaScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCart: () -> Unit,
    cartController: CartController,
) {
    // Local list of sports products for the "Store" experience
    val productosSports = listOf(
        Producto(1, "Zapatos de Running", "Amortiguación premium para asfalto", 120.0),
        Producto(2, "Camiseta NIKE", "Tejido transpirable Dry-Fit", 35.0),
        Producto(3, "Pantalones Cortos", "Flexibilidad total para tus rutinas", 25.0),
        Producto(4, "Gorra Performance", "Protección solar y estilo", 18.0),
        Producto(5, "Bolso de Gimnasio", "Espacio amplio para todo tu equipo", 55.0),
        Producto(6, "Calzas de Compresión", "Mejora tu circulación al entrenar", 45.0)
    )

    val cartItems by cartController.items.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("TIENDA DEPORTIVA", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    BadgedBox(
                        badge = {
                            if (cartItems.isNotEmpty()) {
                                Badge { Text(cartItems.sumOf { it.quantity }.toString()) }
                            }
                        }
                    ) {
                        IconButton(onClick = onNavigateToCart) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f).padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(productosSports) { producto ->
                    ProductoStoreCard(producto) {
                        // Convert Producto to Recompensa format for the shared CartController logic
                        // We reuse the cart logic for simplicity
                        val simulatedRecompensa = com.ute.shopapi.data.model.Recompensa(
                            id = producto.id,
                            nombre = producto.nombre,
                            descripcion = producto.descripcion,
                            puntosNecesarios = producto.precio.toInt(), // Simulating pts as price for the cart
                            stock = producto.stock,
                            estado = "activo"
                        )
                        cartController.addToCart(simulatedRecompensa)
                        scope.launch {
                            snackbarHostState.showSnackbar("Añadido: ${producto.nombre}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoStoreCard(producto: Producto, onAddToCart: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAddToCart() },
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), modifier = Modifier.size(40.dp))
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            Text(producto.nombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
            Text("$${producto.precio}", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
            
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(
                    onClick = onAddToCart,
                    modifier = Modifier.size(32.dp).background(MaterialTheme.colorScheme.primary, CircleShape)
                ) {
                    Icon(Icons.Default.AddShoppingCart, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
