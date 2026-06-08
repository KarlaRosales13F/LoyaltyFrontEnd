package com.ute.shopapi.data.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.shopapi.data.model.CanjeRequest
import com.ute.shopapi.data.model.PuntosFidelizacion
import com.ute.shopapi.data.model.Recompensa
import com.ute.shopapi.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecompensasController : ViewModel() {
    private val _recompensas = MutableStateFlow<List<Recompensa>>(emptyList())
    val recompensas: StateFlow<List<Recompensa>> = _recompensas

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchRecompensas() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = RetrofitClient.recompensasRoutes.getRecompensas()
                if (response.isSuccessful) {
                    _recompensas.value = response.body()?.data ?: emptyList()
                } else {
                    _error.value = "Error al cargar: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun canjearRecompensa(recompensaId: Int, onSuccess: (PuntosFidelizacion) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.recompensasRoutes.canjearRecompensa(CanjeRequest(recompensaId))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.puntosActualizados != null) {
                        onSuccess(body.puntosActualizados)
                    } else {
                        _error.value = "Error: Respuesta del servidor incompleta."
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _error.value = "Error al canjear: ${errorBody ?: "Puntos insuficientes"}"
                }
            } catch (e: Exception) {
                _error.value = "Error en el canje: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
