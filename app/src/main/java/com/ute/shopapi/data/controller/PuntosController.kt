package com.ute.shopapi.data.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.shopapi.data.model.AcumularPuntosRequest
import com.ute.shopapi.data.model.PuntosFidelizacion
import com.ute.shopapi.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PuntosController : ViewModel() {
    private val _puntos = MutableStateFlow<PuntosFidelizacion?>(null)
    val puntos: StateFlow<PuntosFidelizacion?> = _puntos

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchPuntos(userId: Int) {
        viewModelScope.launch {
            try {
                // Intentamos obtener la lista de puntos del usuario logueado
                val response = RetrofitClient.puntosRoutes.getPuntosList()
                if (response.isSuccessful) {
                    val body = response.body()
                    // Buscamos el registro que pertenece al usuario
                    _puntos.value = body?.data?.firstOrNull()
                    _error.value = null
                } else {
                    val errorBody = response.errorBody()?.string()
                    _error.value = "Error: ${errorBody ?: response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Conexión: ${e.localizedMessage}"
            }
        }
    }

    fun updatePuntosLocally(updatedPuntos: PuntosFidelizacion) {
        _puntos.value = updatedPuntos
    }

    fun sumarPuntos(cantidad: Int, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.puntosRoutes.acumularPuntos(AcumularPuntosRequest(cantidad))
                if (response.isSuccessful) {
                    _puntos.value = response.body()
                    _error.value = null
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _error.value = "Error al sumar: ${errorBody ?: response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error red al sumar: ${e.localizedMessage}"
            }
        }
    }
}
