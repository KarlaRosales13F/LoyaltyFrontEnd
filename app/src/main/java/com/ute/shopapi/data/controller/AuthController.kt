package com.ute.shopapi.data.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.shopapi.data.model.LoginRequest
import com.ute.shopapi.data.model.RegisterRequest
import com.ute.shopapi.data.model.User
import com.ute.shopapi.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthController : ViewModel() {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun login(request: LoginRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = RetrofitClient.authRoutes.login(request)
                if (response.isSuccessful) {
                    val auth = response.body()
                    auth?.access?.let { 
                        RetrofitClient.setToken(it)
                        fetchMe(onSuccess)
                    } ?: onSuccess()
                } else {
                    _error.value = "Error al iniciar sesión: Credenciales inválidas"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun fetchMe(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.authRoutes.getMe()
                if (response.isSuccessful) {
                    _currentUser.value = response.body()
                    onSuccess()
                }
            } catch (e: Exception) {
                _error.value = "Error al obtener perfil: ${e.localizedMessage}"
            }
        }
    }

    fun register(request: RegisterRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            // Ensure username is set to email if not provided, as Django often requires it
            val finalRequest = if (request.username.isEmpty()) {
                request.copy(username = request.email)
            } else {
                request
            }
            try {
                val response = RetrofitClient.authRoutes.register(finalRequest)
                if (response.isSuccessful) {
                    val auth = response.body()
                    auth?.access?.let { 
                        RetrofitClient.setToken(it)
                        fetchMe(onSuccess)
                    } ?: onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _error.value = "Error: ${errorBody ?: "Datos inválidos o usuario ya existe"}"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
