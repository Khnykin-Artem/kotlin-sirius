package com.example.myapplication.ui.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.Flower
import com.example.myapplication.model.MockData
import com.example.myapplication.ui.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    cartViewModel: CartViewModel,
    onCartClick: () -> Unit = {},
    onProductClick: (Int) -> Unit = {}
) {
    val cartItemCount by cartViewModel.cartItemCount.collectAsState()
    val cartItems by cartViewModel.cartItems.collectAsState()

    // Логируем изменения корзины
    LaunchedEffect(cartItemCount) {
        println("CatalogScreen: cartItemCount изменился на $cartItemCount")
        println("CatalogScreen: товары в корзине: ${cartItems.map { "${it.flower.name} x${it.quantity}" }}")
    }
    val flowers = MockData.flowers
    val categories = flowers.map { it.category }.distinct()
    val popularFlowers = flowers.filter { it.isPopular }

    var selectedCategory by remember { mutableStateOf<String?>(null) }
    val displayedFlowers = selectedCategory?.let { category ->
        flowers.filter { it.category == category }
    } ?: popularFlowers

    Scaffold(
        topBar = {
            if (selectedCategory != null) {
                TopAppBar(
                    title = {
                        Text(
                            selectedCategory ?: "Популярные товары",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFE91E63),
                        titleContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(onClick = { selectedCategory = null }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Назад",
                                tint = Color.White
                            )
                        }
                    }
            )
            } else {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            selectedCategory ?: "Популярные товары",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color(0xFFE91E63),
                        titleContentColor = Color.White
                    ),
                    actions = {
                        val cartItemCount by cartViewModel.cartItemCount.collectAsState()
                        BadgedBox(
                            badge = {
                                if (cartItemCount > 0) {
                                    Badge(
                                        containerColor = Color.White,
                                        contentColor = Color(0xFFE91E63)
                                    ) {
                                        Text(
                                            text = cartItemCount.toString(),
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                            }
                        ) {
                            IconButton(onClick = onCartClick) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Корзина",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                )
            }
        },
        containerColor = Color(0xFFFCE4EC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Список товаров (популярные или по категории)
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                items(displayedFlowers) { flower ->
                    FlowerCard(
                        flower = flower,
                        onProductClick = { onProductClick(flower.id) },
                        onAddToCart = { cartViewModel.addToCart(flower) }
                    )
                }
            }

            // Категории
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Категории",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFFC2185B),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    categories.chunked(3).forEach { rowCategories ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowCategories.forEach { category ->
                                CategoryChip(
                                    category = category,
                                    emoji = getCategoryEmoji(category, flowers),
                                    isSelected = category == selectedCategory,
                                    onClick = {
                                        selectedCategory = if (selectedCategory == category) null else category
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            // Заполняем пустые места если категорий меньше 3 в ряду
                            repeat(3 - rowCategories.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                        if (rowCategories != categories.takeLast(3)) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

// Функция для получения emoji категории
private fun getCategoryEmoji(category: String, flowers: List<Flower>): String {
    return flowers.firstOrNull { it.category == category }?.emoji ?: "🌸"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlowerCard(
    flower: Flower,
    onProductClick: () -> Unit,
    onAddToCart: () -> Unit = {}
) {
    Card(
        onClick = onProductClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Верхняя часть с emoji и категорией
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Emoji цветка
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color(0xFFF8BBD9), shape = MaterialTheme.shapes.medium),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = flower.emoji,
                        style = MaterialTheme.typography.displayMedium
                    )
                }

                // Категория
                Badge(
                    modifier = Modifier.padding(start = 8.dp),
                    containerColor = Color(0xFFE91E63)
                ) {
                    Text(
                        text = flower.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Название
            Text(
                text = flower.name,
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Описание
            Text(
                text = flower.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Цена и кнопка
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "%.0f ₽".format(flower.price),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFFE91E63)
                )

                Button(
                    onClick = onAddToCart,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE91E63)
                    )
                ) {
                    Text("В корзину")
                }
            }
        }
    }
}

@Composable
fun PopularFlowerCard(
    flower: Flower,
    onProductClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(200.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Emoji цветка
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Color(0xFFF8BBD9), shape = MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = flower.emoji,
                    style = MaterialTheme.typography.displayMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Название
            Text(
                text = flower.name,
                style = MaterialTheme.typography.titleSmall,
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Категория
            Text(
                text = flower.category,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFE91E63),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Цена
            Text(
                text = "%.0f ₽".format(flower.price),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFE91E63),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            // Кнопка в корзину (маленькая)
            Button(
                onClick = onAddToCart,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE91E63)
                )
            ) {
                Text(
                    text = "В корзину",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
fun CategoryChip(
    category: String,
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) Color(0xFFE91E63) else Color(0xFFF8BBD9),
        shadowElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = category,
                style = MaterialTheme.typography.bodySmall,
                color = if (isSelected) Color.White else Color(0xFFE91E63),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Превью функции
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CatalogScreenPreview() {
    MaterialTheme {
        val cartViewModel = androidx.lifecycle.viewmodel.compose.viewModel<com.example.myapplication.ui.viewmodel.CartViewModel>()
        CatalogScreen(
            cartViewModel = cartViewModel,
            onCartClick = { println("Cart clicked") },
            onProductClick = { id -> println("Product $id clicked") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FlowerCardPreview() {
    MaterialTheme {
        FlowerCard(
            flower = Flower(
                id = 1,
                name = "Красные розы",
                description = "Букет из 25 свежих красных роз",
                price = 2500.0,
                imageRes = android.R.drawable.ic_menu_report_image,
                category = "Розы",
                emoji = "🌹"
            ),
            onProductClick = { println("Flower clicked") },
            onAddToCart = { println("Add to cart clicked") }
        )
    }
}

@Preview(showBackground = true, widthDp = 350)
@Composable
fun FlowerCardSmallPreview() {
    MaterialTheme {
        FlowerCard(
            flower = Flower(
                id = 2,
                name = "Белые лилии",
                description = "Элегантные белые лилии в подарочной упаковке",
                price = 1800.0,
                imageRes = android.R.drawable.ic_menu_report_image,
                category = "Лилии",
                emoji = "💮"
            ),
            onProductClick = { println("Flower clicked") },
            onAddToCart = { println("Add to cart clicked") }
        )
    }
}