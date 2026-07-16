package com.fantasykingdom.app.data.model

/** Firestore document shape for /restaurants/{restaurantId}. */
data class Restaurant(
    val id: String = "",
    val name: String = "",
    val cuisine: String = "",
    val imageUrl: String = "",
    val rating: Float = 4.5f,
    val priceLevel: Int = 2, // 1..4 ($ .. $$$$)
    val zone: String = "",
    val menuHighlights: List<String> = emptyList()
)
