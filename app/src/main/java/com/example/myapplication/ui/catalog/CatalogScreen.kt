package com.example.myapplication.ui.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.Flower
import com.example.myapplication.model.MockData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    onCartClick: () -> Unit = {},
    onProductClick: (Int) -> Unit = {}
) {
    val flowers = MockData.flowers
    val categories = flowers.map { it.category }.distinct()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Цветочный магазин",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFE91E63),
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = onCartClick) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Корзина",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        containerColor = Color(0xFFFCE4EC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Категории
            Text(
                text = "Категории",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp),
                color = Color(0xFFC2185B)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(categories) { category ->
                    Card(
                        onClick = { /* Фильтрация по категории */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Emoji категории
                            Text(
                                text = getCategoryEmoji(category, flowers),
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = category,
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Товары
                item {
                    Text(
                        text = "Популярные товары",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Color(0xFFC2185B)
                    )
                }

                items(flowers) { flower ->
                    FlowerCard(
                        flower = flower,
                        onProductClick = { onProductClick(flower.id) }
                    )
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
    onProductClick: () -> Unit
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
                    onClick = { /* Добавить в корзину */ },
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

// Превью функции
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CatalogScreenPreview() {
    MaterialTheme {
        CatalogScreen(
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
            onProductClick = { println("Flower clicked") }
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
            onProductClick = { println("Flower clicked") }
        )
    }
}