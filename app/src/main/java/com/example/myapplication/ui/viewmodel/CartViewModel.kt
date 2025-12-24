package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.CartRepository
import com.example.myapplication.model.CartItem
import com.example.myapplication.model.Flower
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository = com.example.myapplication.data.repository.CartRepositoryImpl()
) : ViewModel() {

    val cartItems: StateFlow<List<CartItem>> = cartRepository.getCartItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartItemCount: StateFlow<Int> = cartRepository.getCartItemCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalPrice: StateFlow<Double> = cartRepository.getTotalPrice()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun addToCart(flower: Flower, quantity: Int = 1) {
        println("CartViewModel.addToCart: ${flower.name}, quantity: $quantity")
        viewModelScope.launch {
            cartRepository.addToCart(flower, quantity)
            println("CartViewModel: товар добавлен в репозиторий")
        }
    }

    fun removeFromCart(flowerId: Int) {
        viewModelScope.launch {
            cartRepository.removeFromCart(flowerId)
        }
    }

    fun updateQuantity(flowerId: Int, quantity: Int) {
        viewModelScope.launch {
            cartRepository.updateQuantity(flowerId, quantity)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }

    fun isInCart(flowerId: Int): StateFlow<Boolean> = cartRepository.isInCart(flowerId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
}
