package com.ute.shopapi.data.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.shopapi.data.model.*
import com.ute.shopapi.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminController : ViewModel() {
    private val _recompensas = MutableStateFlow<List<Recompensa>>(emptyList())
    val recompensas: StateFlow<List<Recompensa>> = _recompensas

    private val _devoluciones = MutableStateFlow<List<Devolucion>>(emptyList())
    val devoluciones: StateFlow<List<Devolucion>> = _devoluciones

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _pagination = MutableStateFlow<PaginatedResponse<Recompensa>?>(null)
    val pagination: StateFlow<PaginatedResponse<Recompensa>?> = _pagination

    fun fetchRecompensas(url: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = if (url == null) {
                    RetrofitClient.recompensasRoutes.getRecompensas()
                } else {
                    RetrofitClient.recompensasRoutes.getRecompensasPage(url)
                }
                
                if (response.isSuccessful) {
                    val body = response.body()
                    _pagination.value = body
                    _recompensas.value = body?.data ?: emptyList()
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchDevoluciones() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = RetrofitClient.devolucionesRoutes.getDevoluciones()
                if (response.isSuccessful) {
                    _devoluciones.value = response.body()?.data ?: emptyList()
                } else {
                    _error.value = "Error al cargar devoluciones: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun actualizarEstadoDevolucion(id: Int, nuevoEstado: String) {
        viewModelScope.launch {
            _error.value = null
            try {
                val response = RetrofitClient.devolucionesRoutes.updateDevolucion(id, DevolucionUpdateRequest(nuevoEstado))
                if (response.isSuccessful) {
                    fetchDevoluciones()
                } else {
                    _error.value = "Error al actualizar: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage}"
            }
        }
    }

    fun createRecompensa(recompensa: Recompensa, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = RetrofitClient.recompensasRoutes.createRecompensa(recompensa)
                if (response.isSuccessful) {
                    fetchRecompensas()
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _error.value = "Error del servidor: ${errorBody ?: response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
