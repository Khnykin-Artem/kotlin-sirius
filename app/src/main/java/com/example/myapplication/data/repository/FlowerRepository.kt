package com.example.myapplication.data.repository

import com.example.myapplication.model.Flower
import com.example.myapplication.model.FlowerDetails
import com.example.myapplication.model.MockData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface FlowerRepository {
    fun getFlowers(): Flow<List<Flower>>
    fun getFlowerById(id: Int): Flow<Flower?>
    fun getFlowerDetailsById(id: Int): Flow<FlowerDetails?>
    fun getCategories(): Flow<List<String>>
}

class FlowerRepositoryImpl : FlowerRepository {
    override fun getFlowers(): Flow<List<Flower>> = flow {
        // Имитация задержки сети
        delay(500)
        emit(MockData.flowers)
    }

    override fun getFlowerById(id: Int): Flow<Flower?> = flow {
        delay(200)
        emit(MockData.flowers.find { it.id == id })
    }

    override fun getFlowerDetailsById(id: Int): Flow<FlowerDetails?> = flow {
        // Имитация более долгой загрузки детальной информации (API call)
        delay(800)
        emit(MockData.flowerDetails.find { it.id == id })
    }

    override fun getCategories(): Flow<List<String>> = flow {
        delay(300)
        emit(MockData.flowers.map { it.category }.distinct())
    }
}