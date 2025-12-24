package com.example.myapplication.data.repository

import com.example.myapplication.model.CartItem
import com.example.myapplication.model.Flower
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

interface CartRepository {
    fun getCartItems(): Flow<List<CartItem>>
    fun getCartItemCount(): Flow<Int>
    fun getTotalPrice(): Flow<Double>
    suspend fun addToCart(flower: Flower, quantity: Int = 1)
    suspend fun removeFromCart(flowerId: Int)
    suspend fun updateQuantity(flowerId: Int, quantity: Int)
    suspend fun clearCart()
    fun isInCart(flowerId: Int): Flow<Boolean>
}

class CartRepositoryImpl : CartRepository {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    private val cartItems = _cartItems.asStateFlow()

    override fun getCartItems(): Flow<List<CartItem>> = cartItems

    override fun getCartItemCount(): Flow<Int> = cartItems.map { items ->
        items.sumOf { it.quantity }
    }

    override fun getTotalPrice(): Flow<Double> = cartItems.map { items ->
        items.sumOf { it.totalPrice }
    }

    override suspend fun addToCart(flower: Flower, quantity: Int) {
        println("CartRepository.addToCart: ${flower.name}, quantity: $quantity")
        val currentItems = _cartItems.value.toMutableList()
        val existingItem = currentItems.find { it.flower.id == flower.id }

        if (existingItem != null) {
            // Обновляем количество существующего товара
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + quantity)
            val index = currentItems.indexOf(existingItem)
            currentItems[index] = updatedItem
            println("CartRepository: обновлено количество товара ${flower.name} до ${updatedItem.quantity}")
        } else {
            // Добавляем новый товар
            currentItems.add(CartItem(flower, quantity))
            println("CartRepository: добавлен новый товар ${flower.name}")
        }

        _cartItems.value = currentItems
        println("CartRepository: текущее количество товаров в корзине: ${currentItems.sumOf { it.quantity }}")
    }

    override suspend fun removeFromCart(flowerId: Int) {
        val currentItems = _cartItems.value.toMutableList()
        currentItems.removeAll { it.flower.id == flowerId }
        _cartItems.value = currentItems
    }

    override suspend fun updateQuantity(flowerId: Int, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(flowerId)
            return
        }

        val currentItems = _cartItems.value.toMutableList()
        val existingItem = currentItems.find { it.flower.id == flowerId }

        if (existingItem != null) {
            val updatedItem = existingItem.copy(quantity = quantity)
            val index = currentItems.indexOf(existingItem)
            currentItems[index] = updatedItem
            _cartItems.value = currentItems
        }
    }

    override suspend fun clearCart() {
        _cartItems.value = emptyList()
    }

    override fun isInCart(flowerId: Int): Flow<Boolean> = cartItems.map { items ->
        items.any { it.flower.id == flowerId }
    }
}
