package com.example.myapplication.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.example.myapplication.model.MockData

class CatalogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Важно для правильной работы жизненного цикла Compose во Fragment
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                CatalogScreen(
                    flowers = MockData.flowers,
                    categories = MockData.flowers.map { it.category }.distinct(),
                    selectedCategory = null,
                    onCartClick = {
                        // TODO: Перейти в корзину
                        println("Корзина clicked")
                    },
                    onProductClick = { productId ->
                        // TODO: Перейти к деталям товара
                        println("Товар clicked: $productId")
                    },
                    onCategorySelect = { category ->
                        // Фильтрация по категории (пока просто логируем)
                        println("Выбрана категория: $category")
                    }
                )
            }
        }
    }
}