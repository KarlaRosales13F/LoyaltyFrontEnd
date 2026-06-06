package com.ute.shopapi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ute.shopapi.data.controller.AdminController
import com.ute.shopapi.data.model.Recompensa

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    onNavigateBack: () -> Unit,
    adminController: AdminController,
) {
    var currentTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("CATÁLOGO", "DEVOLUCIONES", "VENTAS")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("PANEL DE CONTROL", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        if (currentTab == 0) adminController.fetchRecompensas()
                        else if (currentTab == 1) adminController.fetchDevoluciones()
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refrescar")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            SecondaryTabRow(
                selectedTabIndex = currentTab,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = currentTab == index,
                        onClick = { currentTab = index },
                        text = { Text(title, fontWeight = FontWeight.Bold) }
                    )
                }
            }

            when (currentTab) {
                0 -> AdminCatalogoTab(adminController)
                1 -> AdminDevolucionesTab(adminController)
                2 -> Text("Historial Global de Ventas", modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
fun AdminCatalogoTab(controller: AdminController) {
    val recompensas by controller.recompensas.collectAsState()
    val pagination by controller.pagination.collectAsState()
    val error by controller.error.collectAsState()
    val isLoading by controller.isLoading.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) { controller.fetchRecompensas() }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodySmall)
            }
            
            if (isLoading && recompensas.isEmpty()) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (recompensas.isEmpty() && !isLoading) {
                    item {
                        Box(Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No hay items en el catálogo.", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
                items(recompensas) { item ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.nombre, fontWeight = FontWeight.Bold)
                                Text("Stock: ${item.stock}", style = MaterialTheme.typography.bodySmall)
                            }
                            Text("${item.puntosNecesarios} PTS", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

            // Pagination Controls
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { pagination?.previous?.let { controller.fetchRecompensas(it) } },
                    enabled = pagination?.previous != null && !isLoading
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("ANTERIOR")
                }

                Text("Pág ${pagination?.page ?: 1}", style = MaterialTheme.typography.labelLarge)

                TextButton(
                    onClick = { pagination?.next?.let { controller.fetchRecompensas(it) } },
                    enabled = pagination?.next != null && !isLoading
                ) {
                    Text("SIGUIENTE")
                    Spacer(Modifier.width(4.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                }
            }
            Spacer(modifier = Modifier.height(72.dp)) // Space for FAB
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Default.Add, contentDescription = "Añadir Item")
        }
    }

    if (showAddDialog) {
        AddRewardDialog(
            controller = controller,
            onDismiss = { showAddDialog = false },
            onConfirm = { newItem ->
                controller.createRecompensa(newItem) {
                    showAddDialog = false
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRewardDialog(onDismiss: () -> Unit, onConfirm: (Recompensa) -> Unit, controller: AdminController) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var puntos by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    
    val error by controller.error.collectAsState()
    val isLoading by controller.isLoading.collectAsState()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("NUEVA RECOMPENSA", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                
                error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(vertical = 8.dp), style = MaterialTheme.typography.bodySmall)
                }
                
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("NOMBRE") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("DESCRIPCIÓN") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(value = puntos, onValueChange = { puntos = it }, label = { Text("PUNTOS NECESARIOS") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("STOCK") }, modifier = Modifier.fillMaxWidth())
                
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        val r = Recompensa(
                            id = 0,
                            nombre = nombre,
                            descripcion = descripcion,
                            puntosNecesarios = puntos.toDoubleOrNull()?.toInt() ?: 0,
                            stock = stock.toDoubleOrNull()?.toInt() ?: 0,
                            estado = "activo"
                        )
                        onConfirm(r)
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    else Text("GUARDAR RECOMPENSA", fontWeight = FontWeight.Bold)
                }
                
                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth(), enabled = !isLoading) {
                    Text("CANCELAR", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun AdminDevolucionesTab(controller: AdminController) {
    val devoluciones by controller.devoluciones.collectAsState()
    
    LaunchedEffect(Unit) { controller.fetchDevoluciones() }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(devoluciones) { dev ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Devolución #${dev.id}", fontWeight = FontWeight.Bold)
                    Text("Motivo: ${dev.motivo}")
                    Text("Estado: ${dev.estado.uppercase()}", color = if (dev.estado == "aprobada") Color.Green else Color.Red)
                }
            }
        }
    }
}
