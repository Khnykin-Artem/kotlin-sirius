
package com.example.myapplication.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.usecase.FlowerUseCase
import com.example.myapplication.model.Flower
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val flowerUseCase: FlowerUseCase
) : ViewModel() {

    private val _flowers = MutableStateFlow<List<Flower>>(emptyList())
    val flowers: StateFlow<List<Flower>> = _flowers.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    init {
        loadFlowers()
        loadCategories()
    }

    private fun loadFlowers() {
        viewModelScope.launch {
            flowerUseCase.getFlowers().collectLatest { flowers ->
                _flowers.value = flowers
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            flowerUseCase.getCategories().collectLatest { categories ->
                _categories.value = categories
            }
        }
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
        viewModelScope.launch {
            if (category != null) {
                flowerUseCase.getFlowersByCategory(category).collectLatest { flowers ->
                    _flowers.value = flowers
                }
            } else {
                flowerUseCase.getFlowers().collectLatest { flowers ->
                    _flowers.value = flowers
                }
            }
        }
    }
}