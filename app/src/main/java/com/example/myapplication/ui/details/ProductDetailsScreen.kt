package com.example.myapplication.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import kotlinx.coroutines.launch
import com.example.myapplication.data.repository.FlowerRepositoryImpl
import com.example.myapplication.model.Flower
import com.example.myapplication.model.FlowerDetails
import com.example.myapplication.model.MockData
import com.example.myapplication.ui.viewmodel.CartViewModel
import kotlinx.coroutines.flow.firstOrNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    productId: Int,
    cartViewModel: CartViewModel,
    onBackClick: () -> Unit = {},
    onAddToCart: () -> Unit = {},
    onCartClick: () -> Unit = {}
) {
    val repository = remember { FlowerRepositoryImpl() }
    var flowerDetails by remember { mutableStateOf<FlowerDetails?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Загрузка данных при запуске
    LaunchedEffect(productId) {
        isLoading = true
        flowerDetails = repository.getFlowerDetailsById(productId).firstOrNull()
        isLoading = false
        println("ProductDetailsScreen: загружен товар с id=$productId, найден=${flowerDetails != null}")
    }

    if (isLoading) {
        // Экран загрузки
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color(0xFFE91E63)
            )
        }
        return
    }

    val flower = flowerDetails ?: MockData.flowerDetails.first()

    // Логируем текущее состояние корзины
    val cartItemCount by cartViewModel.cartItemCount.collectAsState()
    println("ProductDetailsScreen: cartItemCount = $cartItemCount")

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Детали товара",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFE91E63),
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
                },
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
            // Emoji цветка (большое как в каталоге)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(Color(0xFFF8BBD9)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = flower.emoji,
                    style = MaterialTheme.typography.displayLarge
                )
            }

            // Основная карточка с информацией
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
                    modifier = Modifier.padding(20.dp)
                ) {
                    // Название
                    Text(
                        text = flower.name,
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Категория (как в каталоге и корзине)
                    Badge(
                        containerColor = Color(0xFFE91E63)
                    ) {
                        Text(
                            text = flower.category,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Цена (такой же стиль как в корзине)
                    Text(
                        text = "Цена",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "%.0f ₽".format(flower.price),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFFE91E63),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Кнопка "В корзину" (такая же как в каталоге)
                    Button(
                        onClick = {
                            // Конвертируем FlowerDetails в Flower для добавления в корзину
                            val flowerForCart = Flower(
                                id = flower.id,
                                name = flower.name,
                                description = flower.description,
                                price = flower.price,
                                imageRes = flower.imageRes,
                                category = flower.category,
                                emoji = flower.emoji
                            )
                            cartViewModel.addToCart(flowerForCart)
                            onAddToCart()
                        },
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
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "В корзину",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Карточка с описанием
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Описание",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFFC2185B),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text(
                        text = flower.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Justify
                    )
                }
            }

            // Карточка с характеристиками (в стиле корзины)
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
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Характеристики",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFFC2185B),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Характеристики в виде строк как в корзине
                    CharacteristicRow(
                        title = "Свежесть",
                        value = flower.freshness
                    )
                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.LightGray
                    )
                    CharacteristicRow(
                        title = "Доставка",
                        value = flower.deliveryTime
                    )
                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.LightGray
                    )
                    CharacteristicRow(
                        title = "Упаковка",
                        value = flower.packaging
                    )
                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.LightGray
                    )
                    CharacteristicRow(
                        title = "Сезон",
                        value = flower.season
                    )
                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.LightGray
                    )
                    CharacteristicRow(
                        title = "Происхождение",
                        value = flower.origin
                    )
                }
            }

            // Карточка с инструкциями по уходу
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
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Уход за цветами",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFFC2185B),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text(
                        text = flower.careInstructions,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }
    }
}

@Composable
fun CharacteristicRow(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFFE91E63),
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductDetailsScreenPreview() {
    MaterialTheme {
        // Для превью используем статичные данные, без загрузки из репозитория
        val flowerDetails = MockData.flowerDetails.find { it.id == 1 } ?: MockData.flowerDetails.first()
        val cartViewModel = androidx.lifecycle.viewmodel.compose.viewModel<CartViewModel>()

        ProductDetailsScreenStatic(
            flowerDetails = flowerDetails,
            cartViewModel = cartViewModel,
            onBackClick = { println("Back clicked") },
            onAddToCart = { println("Add to cart clicked") },
            onCartClick = { println("Cart clicked") }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductDetailsScreenPreview2() {
    MaterialTheme {
        // Для превью используем статичные данные, без загрузки из репозитория
        val flowerDetails = MockData.flowerDetails.find { it.id == 2 } ?: MockData.flowerDetails.first()
        val cartViewModel = androidx.lifecycle.viewmodel.compose.viewModel<CartViewModel>()

        ProductDetailsScreenStatic(
            flowerDetails = flowerDetails,
            cartViewModel = cartViewModel,
            onBackClick = { println("Back clicked") },
            onAddToCart = { println("Add to cart clicked") },
            onCartClick = { println("Cart clicked") }
        )
    }
}

// Вспомогательная функция для превью с статичными данными
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreenStatic(
    flowerDetails: FlowerDetails,
    cartViewModel: CartViewModel,
    onBackClick: () -> Unit = {},
    onAddToCart: () -> Unit = {},
    onCartClick: () -> Unit = {}
) {
    val flower = flowerDetails

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Детали товара",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFE91E63),
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
                },
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
            // Emoji цветка (большое как в каталоге)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(Color(0xFFF8BBD9)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = flower.emoji,
                    style = MaterialTheme.typography.displayLarge
                )
            }

            // Основная карточка с информацией
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
                    modifier = Modifier.padding(20.dp)
                ) {
                    // Название
                    Text(
                        text = flower.name,
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Категория (как в каталоге и корзине)
                    Badge(
                        containerColor = Color(0xFFE91E63)
                    ) {
                        Text(
                            text = flower.category,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Цена (такой же стиль как в корзине)
                    Text(
                        text = "Цена",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "%.0f ₽".format(flower.price),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFFE91E63),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Кнопка "В корзину" (такая же как в каталоге)
                    Button(
                        onClick = {
                            // Конвертируем FlowerDetails в Flower для добавления в корзину
                            val flowerForCart = Flower(
                                id = flower.id,
                                name = flower.name,
                                description = flower.description,
                                price = flower.price,
                                imageRes = flower.imageRes,
                                category = flower.category,
                                emoji = flower.emoji
                            )
                            cartViewModel.addToCart(flowerForCart)
                            onAddToCart()
                        },
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
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "В корзину",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Карточка с описанием
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Описание",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFFC2185B),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text(
                        text = flower.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Justify
                    )
                }
            }

            // Карточка с характеристиками (в стиле корзины)
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
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Характеристики",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFFC2185B),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Характеристики в виде строк как в корзине
                    CharacteristicRow(
                        title = "Свежесть",
                        value = flower.freshness
                    )
                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.LightGray
                    )
                    CharacteristicRow(
                        title = "Доставка",
                        value = flower.deliveryTime
                    )
                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.LightGray
                    )
                    CharacteristicRow(
                        title = "Упаковка",
                        value = flower.packaging
                    )
                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.LightGray
                    )
                    CharacteristicRow(
                        title = "Сезон",
                        value = flower.season
                    )
                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.LightGray
                    )
                    CharacteristicRow(
                        title = "Происхождение",
                        value = flower.origin
                    )
                }
            }

            // Карточка с инструкциями по уходу
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
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Уход за цветами",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFFC2185B),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text(
                        text = flower.careInstructions,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }
    }
}