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

    fun fetchCompras() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.comprasRoutes.getCompras()
                if (response.isSuccessful) {
                    _compras.value = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createCompra(request: CompraRequest) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.comprasRoutes.createCompra(request)
                if (response.isSuccessful) {
                    fetchCompras() // Refresh list
                }
            } catch (e: Exception) {}
        }
    }
}
