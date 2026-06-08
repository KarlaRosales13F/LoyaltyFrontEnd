package com.ute.shopapi.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ute.shopapi.data.controller.*
import com.ute.shopapi.ui.screens.*

@Composable
fun AppNavigation(
    authController: AuthController,
    comprasController: ComprasController,
    puntosController: PuntosController,
    recompensasController: RecompensasController,
    adminController: AdminController,
    cartController: CartController,
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("home") { popUpTo("login") { inclusive = true } } },
                onNavigateToRegister = { navController.navigate("register") },
                controller = authController
            )
        }
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate("home") { popUpTo("login") { inclusive = true } } },
                onNavigateToLogin = { navController.navigate("login") },
                controller = authController
            )
        }
        composable("home") {
            HomeScreen(
                onNavigateToCompras = { navController.navigate("compras") },
                onNavigateToRecompensas = { navController.navigate("recompensas") },
                onNavigateToAdmin = { navController.navigate("admin") },
                onNavigateToTienda = { navController.navigate("tienda") },
                onNavigateToPerfil = { navController.navigate("perfil") },
                onNavigateToOfertas = { navController.navigate("ofertas") },
                authController = authController,
                puntosController = puntosController,
                comprasController = comprasController
            )
        }
        composable("ofertas") {
            OfertasScreen(
                onNavigateBack = { navController.popBackStack() },
                adminController = adminController
            )
        }
        composable("tienda") {
            TiendaScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCart = { navController.navigate("cart") },
                cartController = cartController
            )
        }
        composable("cart") {
            CartScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToHome = { navController.navigate("home") { popUpTo("home") { inclusive = true } } },
                cartController = cartController,
                comprasController = comprasController,
                puntosController = puntosController,
                authController = authController
            )
        }
        composable("perfil") {
            PerfilScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogout = { 
                    navController.navigate("login") { popUpTo("home") { inclusive = true } }
                },
                authController = authController
            )
        }
        composable("admin") {
            AdminScreen(
                onNavigateBack = { navController.popBackStack() },
                adminController = adminController
            )
        }
        composable("compras") {
            ComprasScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTienda = { navController.navigate("tienda") },
                controller = comprasController
            )
        }
        composable("recompensas") {
            RecompensasScreen(
                onNavigateBack = { navController.popBackStack() },
                recompensasController = recompensasController,
                puntosController = puntosController
            )
        }
    }
}
