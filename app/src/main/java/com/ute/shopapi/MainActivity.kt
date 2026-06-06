package com.ute.shopapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.ute.shopapi.data.controller.*
import com.ute.shopapi.ui.AppNavigation
import com.ute.shopapi.ui.theme.ShopapiTheme

class MainActivity : ComponentActivity() {
    private val authController: AuthController by viewModels()
    private val comprasController: ComprasController by viewModels()
    private val puntosController: PuntosController by viewModels()
    private val recompensasController: RecompensasController by viewModels()
    private val adminController: AdminController by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopapiTheme {
                AppNavigation(
                    authController = authController,
                    comprasController = comprasController,
                    puntosController = puntosController,
                    recompensasController = recompensasController,
                    adminController = adminController,
                )
            }
        }
    }
}
