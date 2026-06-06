package com.ute.shopapi.data.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ute.shopapi.data.model.User
import com.ute.shopapi.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserController : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun fetchProfile() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.userRoutes.getProfile()
                if (response.isSuccessful) {
                    _user.value = response.body()
                }
            } catch (e: Exception) {
            }
        }
    }
}
