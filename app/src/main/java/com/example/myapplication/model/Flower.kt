package com.example.myapplication.model

data class Flower(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageRes: Int,
    val category: String
)

object MockData {
    val flowers = listOf(
        Flower(
            id = 1,
            name = "Красные розы",
            description = "Букет из 25 свежих красных роз",
            price = 2500.0,
            imageRes = android.R.drawable.ic_menu_report_image,
            category = "Розы"
        ),
        Flower(
            id = 2,
            name = "Белые лилии",
            description = "Элегантные белые лилии в подарочной упаковке",
            price = 1800.0,
            imageRes = android.R.drawable.ic_menu_report_image,
            category = "Лилии"
        ),
        Flower(
            id = 3,
            name = "Фиалки",
            description = "Нежные фиалки в горшочке",
            price = 1200.0,
            imageRes = android.R.drawable.ic_menu_report_image,
            category = "Горшечные"
        )
    )
}