package com.example.myapplication.data.repository

import com.example.myapplication.model.Flower
import com.example.myapplication.model.MockData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface FlowerRepository {
    fun getFlowers(): Flow<List<Flower>>
    fun getFlowerById(id: Int): Flow<Flower?>
    fun getCategories(): Flow<List<String>>
}

class FlowerRepositoryImpl : FlowerRepository {
    override fun getFlowers(): Flow<List<Flower>> = flow {
        emit(MockData.flowers)
    }

    override fun getFlowerById(id: Int): Flow<Flower?> = flow {
        emit(MockData.flowers.find { it.id == id })
    }

    override fun getCategories(): Flow<List<String>> = flow {
        emit(MockData.flowers.map { it.category }.distinct())
    }
}