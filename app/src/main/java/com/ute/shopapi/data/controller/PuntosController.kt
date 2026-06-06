package com.ute.shopapi.data.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.shopapi.data.model.PuntosFidelizacion
import com.ute.shopapi.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PuntosController : ViewModel() {
    private val _puntos = MutableStateFlow<PuntosFidelizacion?>(null)
    val puntos: StateFlow<PuntosFidelizacion?> = _puntos

    fun fetchPuntos(userId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.puntosRoutes.getPuntos(userId)
                if (response.isSuccessful) {
                    _puntos.value = response.body()
                }
            } catch (e: Exception) {
            }
        }
    }
}
