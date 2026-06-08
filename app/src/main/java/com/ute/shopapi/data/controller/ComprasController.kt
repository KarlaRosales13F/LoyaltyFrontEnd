package com.ute.shopapi.data.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.shopapi.data.model.Compra
import com.ute.shopapi.data.model.CompraRequest
import com.ute.shopapi.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ComprasController : ViewModel() {
    private val _compras = MutableStateFlow<List<Compra>>(emptyList())
    val compras: StateFlow<List<Compra>> = _compras

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun clearData() {
        _compras.value = emptyList()
        _error.value = null
    }

    fun fetchCompras() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = RetrofitClient.comprasRoutes.getCompras()
                if (response.isSuccessful) {
                    _compras.value = response.body()?.data ?: emptyList()
                } else {
                    _error.value = "Error al cargar compras: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de red: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createCompra(request: CompraRequest, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = RetrofitClient.comprasRoutes.createCompra(request)
                if (response.isSuccessful) {
                    fetchCompras() 
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _error.value = "Error al comprar: ${errorBody ?: response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
