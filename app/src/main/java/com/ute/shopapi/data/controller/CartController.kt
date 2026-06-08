package com.ute.shopapi.data.controller

import androidx.lifecycle.ViewModel
import com.ute.shopapi.data.model.Recompensa
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CartItem(
    val recompensa: Recompensa,
    val quantity: Int = 1
)

class CartController : ViewModel() {
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    fun addToCart(recompensa: Recompensa) {
        val currentList = _items.value.toMutableList()
        val existingIndex = currentList.indexOfFirst { it.recompensa.id == recompensa.id }
        
        if (existingIndex != -1) {
            val item = currentList[existingIndex]
            currentList[existingIndex] = item.copy(quantity = item.quantity + 1)
        } else {
            currentList.add(CartItem(recompensa))
        }
        _items.value = currentList
    }

    fun removeFromCart(recompensaId: Int) {
        _items.value = _items.value.filter { it.recompensa.id != recompensaId }
    }

    fun clearCart() {
        _items.value = emptyList()
    }

    fun getTotal(): Double {
        return _items.value.sumOf { it.recompensa.puntosNecesarios.toDouble() * it.quantity }
    }
}
