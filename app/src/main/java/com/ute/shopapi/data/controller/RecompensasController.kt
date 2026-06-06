package com.ute.shopapi.data.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.shopapi.data.model.CanjeRequest
import com.ute.shopapi.data.model.Recompensa
import com.ute.shopapi.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecompensasController : ViewModel() {
    private val _recompensas = MutableStateFlow<List<Recompensa>>(emptyList())
    val recompensas: StateFlow<List<Recompensa>> = _recompensas

    fun fetchRecompensas() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.recompensasRoutes.getRecompensas()
                if (response.isSuccessful) {
                    _recompensas.value = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
            }
        }
    }

    fun canjearRecompensa(recompensaId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.recompensasRoutes.canjearRecompensa(CanjeRequest(recompensaId))
                if (response.isSuccessful) {
                    onSuccess()
                }
            } catch (e: Exception) {
            }
        }
    }
}
