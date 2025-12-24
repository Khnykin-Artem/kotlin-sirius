package com.example.myapplication.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.example.myapplication.ui.details.ProductDetailsScreen

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
                CatalogNavigation()
            }
        }
    }
}

@Composable
fun CatalogNavigation() {
    var currentScreen by remember { mutableStateOf<CatalogScreen>(CatalogScreen.Catalog) }

    when (currentScreen) {
        is CatalogScreen.Catalog -> {
            CatalogScreen(
                onCartClick = {
                    // TODO: Перейти в корзину
                    println("Корзина clicked")
                },
                onProductClick = { productId ->
                    // Перейти к деталям товара
                    currentScreen = CatalogScreen.ProductDetails(productId)
                }
            )
        }
        is CatalogScreen.ProductDetails -> {
            val productId = (currentScreen as CatalogScreen.ProductDetails).productId
            ProductDetailsScreen(
                productId = productId,
                onBackClick = {
                    // Вернуться к каталогу
                    currentScreen = CatalogScreen.Catalog
                },
                onAddToCart = {
                    println("Добавлено в корзину: $productId")
                },
                onCartClick = {
                    println("Перейти в корзину")
                }
            )
        }
    }
}

sealed class CatalogScreen {
    object Catalog : CatalogScreen()
    data class ProductDetails(val productId: Int) : CatalogScreen()
}