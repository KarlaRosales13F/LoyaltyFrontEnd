package com.ute.shopapi.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ute.shopapi.data.controller.*
import com.ute.shopapi.data.model.CompraRequest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    cartController: CartController,
    comprasController: ComprasController,
    puntosController: PuntosController,
    authController: AuthController
) {
    val items by cartController.items.collectAsState()
    val currentUser by authController.currentUser.collectAsState()
    var deliveryMethod by remember { mutableStateOf("retiro") } 
    var paymentMethod by remember { mutableStateOf("tarjeta") }
    var address by remember { mutableStateOf("") }
    
    val total = items.sumOf { it.recompensa.puntosNecesarios.toDouble() * it.quantity }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MI CARRITO", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        if (items.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("El carrito está vacío")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text("RESUMEN DE PEDIDO", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                
                items.forEach { item ->
                    CartItemRow(item, onRemove = { cartController.removeFromCart(item.recompensa.id) })
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                Text("MÉTODO DE ENTREGA", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = deliveryMethod == "retiro",
                        onClick = { deliveryMethod = "retiro" },
                        label = { Text("Retiro en tienda") },
                        leadingIcon = { Icon(Icons.Default.Store, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = deliveryMethod == "envio",
                        onClick = { deliveryMethod = "envio" },
                        label = { Text("Envío a domicilio") },
                        leadingIcon = { Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        modifier = Modifier.weight(1f)
                    )
                }

                if (deliveryMethod == "envio") {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("DIRECCIÓN DE ENVÍO") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        placeholder = { Text("Calle, Número, Ciudad...") }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("MÉTODO DE PAGO", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("tarjeta", "efectivo", "paypal").forEach { method ->
                        FilterChip(
                            selected = paymentMethod == method,
                            onClick = { paymentMethod = method },
                            label = { Text(method.uppercase()) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("TOTAL A PAGAR", fontWeight = FontWeight.Bold)
                        Text("$${total}", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleLarge)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (deliveryMethod == "envio" && address.isBlank()) {
                            scope.launch { snackbarHostState.showSnackbar("Por favor ingresa una dirección") }
                            return@Button
                        }
                        
                        comprasController.createCompra(
                            request = CompraRequest(
                                metodoPago = paymentMethod, 
                                total = total,
                                direccion = if (deliveryMethod == "envio") address else "Retiro en Local",
                                estado = "en local"
                            ),
                            onSuccess = {
                                scope.launch {
                                    snackbarHostState.showSnackbar("¡Tu compra fue realizada con éxito!")
                                    currentUser?.id?.let { puntosController.fetchPuntos(it) }
                                    cartController.clearCart()
                                    onNavigateToHome()
                                }
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("FINALIZAR COMPRA", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem, onRemove: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.recompensa.nombre, fontWeight = FontWeight.Bold)
                Text("${item.quantity} x $${item.recompensa.puntosNecesarios}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
            Text("$${item.recompensa.puntosNecesarios.toDouble() * item.quantity}", fontWeight = FontWeight.Bold)
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Close, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(20.dp))
            }
        }
    }
}
