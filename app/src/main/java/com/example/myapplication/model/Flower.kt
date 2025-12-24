package com.example.myapplication.model

data class Flower(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageRes: Int,
    val category: String,
    val emoji: String
)

data class FlowerDetails(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageRes: Int,
    val category: String,
    val emoji: String,
    val freshness: String = "Максимальная",
    val deliveryTime: String = "В течение 2 часов",
    val packaging: String = "Подарочная",
    val season: String = "Круглый год",
    val careInstructions: String = "Хранить в прохладном месте, вдали от прямых солнечных лучей",
    val origin: String = "Экологически чистые цветы из Голландии"
)

object MockData {
    val flowers = listOf(
        Flower(
            id = 1,
            name = "Красные розы",
            description = "Букет из 25 свежих красных роз",
            price = 2500.0,
            imageRes = android.R.drawable.ic_menu_report_image,
            category = "Розы",
            emoji = "🌹"
        ),
        Flower(
            id = 2,
            name = "Белые лилии",
            description = "Элегантные белые лилии в подарочной упаковке",
            price = 1800.0,
            imageRes = android.R.drawable.ic_menu_report_image,
            category = "Лилии",
            emoji = "💮"
        ),
        Flower(
            id = 3,
            name = "Фиалки",
            description = "Нежные фиалки в горшочке",
            price = 1200.0,
            imageRes = android.R.drawable.ic_menu_report_image,
            category = "Горшечные",
            emoji = "🌸"
        ),
        Flower(
            id = 4,
            name = "Тюльпаны",
            description = "Яркие разноцветные тюльпаны в вазе",
            price = 2200.0,
            imageRes = android.R.drawable.ic_menu_report_image,
            category = "Тюльпаны",
            emoji = "🌷"
        ),
        Flower(
            id = 5,
            name = "Герберы",
            description = "Солнечные герберы разных цветов",
            price = 1600.0,
            imageRes = android.R.drawable.ic_menu_report_image,
            category = "Герберы",
            emoji = "🌼"
        )
    )

    val flowerDetails = listOf(
        FlowerDetails(
            id = 1,
            name = "Красные розы",
            description = "Роскошный букет из 25 свежих красных роз с длинными стеблями. Идеальный выбор для романтического подарка или особого случая.",
            price = 2500.0,
            imageRes = android.R.drawable.ic_menu_report_image,
            category = "Розы",
            emoji = "🌹",
            freshness = "Максимальная",
            deliveryTime = "В течение 2 часов",
            packaging = "Подарочная коробка с лентами",
            season = "Круглый год",
            careInstructions = "Хранить в прохладном месте, менять воду ежедневно, срезать стебли под углом",
            origin = "Экологически чистые розы из Колумбии"
        ),
        FlowerDetails(
            id = 2,
            name = "Белые лилии",
            description = "Элегантные белые лилии в стильной подарочной упаковке. Символ чистоты и невинности.",
            price = 1800.0,
            imageRes = android.R.drawable.ic_menu_report_image,
            category = "Лилии",
            emoji = "💮",
            freshness = "Максимальная",
            deliveryTime = "В течение 3 часов",
            packaging = "Прозрачная пленка с декоративными элементами",
            season = "Весна-Осень",
            careInstructions = "Удалить пыльцу с тычинок, хранить вдали от фруктов",
            origin = "Свежие лилии из Голландии"
        ),
        FlowerDetails(
            id = 3,
            name = "Фиалки",
            description = "Нежные фиалки в красивом керамическом горшочке. Отличный выбор для дома или офиса.",
            price = 1200.0,
            imageRes = android.R.drawable.ic_menu_report_image,
            category = "Горшечные",
            emoji = "🌸",
            freshness = "Максимальная",
            deliveryTime = "В течение 1 часа",
            packaging = "Декоративный горшок в подарочной коробке",
            season = "Круглый год",
            careInstructions = "Поливать умеренно, держать в полутени, удобрять раз в месяц",
            origin = "Комнатные фиалки из питомника"
        ),
        FlowerDetails(
            id = 4,
            name = "Тюльпаны",
            description = "Яркие разноцветные тюльпаны в стильной вазе. Весеннее настроение в вашем доме.",
            price = 2200.0,
            imageRes = android.R.drawable.ic_menu_report_image,
            category = "Тюльпаны",
            emoji = "🌷",
            freshness = "Максимальная",
            deliveryTime = "В течение 2 часов",
            packaging = "Стеклянная ваза с декоративными элементами",
            season = "Весна",
            careInstructions = "Менять воду ежедневно, держать в прохладном месте",
            origin = "Свежие тюльпаны из Голландии"
        ),
        FlowerDetails(
            id = 5,
            name = "Герберы",
            description = "Солнечные герберы разных цветов в яркой композиции. Радость и позитив в каждом лепестке.",
            price = 1600.0,
            imageRes = android.R.drawable.ic_menu_report_image,
            category = "Герберы",
            emoji = "🌼",
            freshness = "Максимальная",
            deliveryTime = "В течение 2 часов",
            packaging = "Цветная подарочная упаковка",
            season = "Круглый год",
            careInstructions = "Подрезать стебли, менять воду через день",
            origin = "Герберы из Кении"
        )
    )
}