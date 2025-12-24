package com.example.myapplication.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.cart.CartScreen
import com.example.myapplication.ui.details.ProductDetailsScreen
import com.example.myapplication.ui.viewmodel.CartViewModel

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
                val cartViewModel: CartViewModel = viewModel()
                CatalogNavigation(cartViewModel)
            }
        }
    }
}

@Composable
fun CatalogNavigation(cartViewModel: CartViewModel) {
    var currentScreen by remember { mutableStateOf<CatalogScreen>(CatalogScreen.Catalog) }

    when (currentScreen) {
        is CatalogScreen.Catalog -> {
            CatalogScreen(
                cartViewModel = cartViewModel,
                onCartClick = {
                    // Перейти в корзину
                    currentScreen = CatalogScreen.Cart
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
                cartViewModel = cartViewModel,
                onBackClick = {
                    // Вернуться к каталогу
                    currentScreen = CatalogScreen.Catalog
                },
                onAddToCart = {
                    // Добавление в корзину обрабатывается внутри ProductDetailsScreen
                    println("Товар добавлен в корзину")
                },
                onCartClick = {
                    // Перейти в корзину
                    currentScreen = CatalogScreen.Cart
                }
            )
        }
        is CatalogScreen.Cart -> {
            CartScreen(
                cartViewModel = cartViewModel,
                onBackClick = {
                    // Вернуться к каталогу
                    currentScreen = CatalogScreen.Catalog
                },
                onCheckoutClick = {
                    // TODO: Обработка оформления заказа
                    println("Оформить заказ")
                }
            )
        }
    }
}

sealed class CatalogScreen {
    object Catalog : CatalogScreen()
    data class ProductDetails(val productId: Int) : CatalogScreen()
    object Cart : CatalogScreen()
}