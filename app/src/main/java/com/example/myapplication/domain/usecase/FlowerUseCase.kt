package com.example.myapplication.domain.usecase

import com.example.myapplication.data.repository.FlowerRepository
import com.example.myapplication.model.Flower
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FlowerUseCase @Inject constructor(
    private val repository: FlowerRepository
) {
    fun getFlowers(): Flow<List<Flower>> = repository.getFlowers()

    fun getFlowerById(id: Int): Flow<Flower?> = repository.getFlowerById(id)

    fun getCategories(): Flow<List<String>> = repository.getCategories()

    fun getFlowersByCategory(category: String): Flow<List<Flower>> =
        repository.getFlowers().map { flowers ->
            flowers.filter { it.category == category }
        }
}