package com.ute.shopapi.data.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.shopapi.data.model.*
import com.ute.shopapi.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoyaltyController : ViewModel() {
    private val _compras = MutableStateFlow<List<Compra>>(emptyList())
    val compras: StateFlow<List<Compra>> = _compras

    private val _puntos = MutableStateFlow<PuntosFidelizacion?>(null)
    val puntos: StateFlow<PuntosFidelizacion?> = _puntos

    private val _recompensas = MutableStateFlow<List<Recompensa>>(emptyList())
    val recompensas: StateFlow<List<Recompensa>> = _recompensas

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchCompras() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.comprasRoutes.getCompras()
                if (response.isSuccessful) {
                    _compras.value = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchPuntos(userId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.puntosRoutes.getPuntos(userId)
                if (response.isSuccessful) {
                    _puntos.value = response.body()
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun fetchRecompensas() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.recompensasRoutes.getRecompensas()
                if (response.isSuccessful) {
                    _recompensas.value = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun canjearRecompensa(recompensaId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.recompensasRoutes.canjearRecompensa(CanjeRequest(recompensaId))
                if (response.isSuccessful) {
                    onSuccess()
                    // Refresh points after redemption
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
