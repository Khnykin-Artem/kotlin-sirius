package com.example.myapplication.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.MockData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onBackClick: () -> Unit = {},
    onCheckoutClick: () -> Unit = {}
) {
    val cartItems = remember { MockData.flowers.take(3).toMutableStateList() }
    val totalPrice = remember(cartItems) { cartItems.sumOf { it.price } }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Корзина",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFE91E63), // Розовый цвет
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        containerColor = Color(0xFFFCE4EC) // Светло-розовый фон
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🛒", // Иконка корзины
                            style = MaterialTheme.typography.displayLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text(
                            text = "Корзина пуста",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color(0xFFC2185B) // Темно-розовый
                        )
                        Text(
                            text = "Добавьте товары из каталога",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            } else {
                // Счетчик товаров
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Text(
                        text = "Товаров в корзине: ${cartItems.size}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        color = Color(0xFFE91E63)
                    )
                }

                // Список товаров
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cartItems) { item ->
                        CartItem(
                            flower = item,
                            onRemove = { cartItems.remove(item) }
                        )
                    }
                }

                // Панель итого и оформления
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        // Итого
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Итого:",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = "%.0f ₽".format(totalPrice),
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color(0xFFE91E63), // Розовый
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Скидка (пример)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Скидка:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                            Text(
                                text = "0 ₽",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // К оплате
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "К оплате:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = "%.0f ₽".format(totalPrice),
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color(0xFFE91E63),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Кнопка оформления
                        Button(
                            onClick = onCheckoutClick,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE91E63)
                            ),
                            shape = MaterialTheme.shapes.large,
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 8.dp,
                                pressedElevation = 4.dp
                            )
                        ) {
                            Text(
                                text = "Оформить заказ",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Кнопка продолжить покупки
                        TextButton(
                            onClick = onBackClick,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Продолжить покупки",
                                color = Color(0xFFE91E63)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItem(
    flower: com.example.myapplication.model.Flower,
    onRemove: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Emoji цветка
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFFF8BBD9), shape = MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = flower.emoji,
                    style = MaterialTheme.typography.displayMedium
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Информация о товаре
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = flower.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = flower.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFE91E63)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "%.0f ₽".format(flower.price),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFFE91E63),
                    fontWeight = FontWeight.Bold
                )
            }

            // Кнопка удаления
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить",
                    tint = Color(0xFFF44336) // Красный
                )
            }
        }
    }
}

// Превью функции
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CartScreenPreview() {
    MaterialTheme {
        CartScreen(
            onBackClick = { println("Back clicked") },
            onCheckoutClick = { println("Checkout clicked") }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EmptyCartScreenPreview() {
    MaterialTheme {
        CartScreen(
            onBackClick = { println("Back clicked") },
            onCheckoutClick = { println("Checkout clicked") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CartItemPreview() {
    MaterialTheme {
        CartItem(
            flower = com.example.myapplication.model.Flower(
                id = 1,
                name = "Красные розы",
                description = "Букет из 25 свежих красных роз",
                price = 2500.0,
                imageRes = android.R.drawable.ic_menu_report_image,
                category = "Розы",
                emoji = "🌹"
            ),
            onRemove = { println("Remove clicked") }
        )
    }
}