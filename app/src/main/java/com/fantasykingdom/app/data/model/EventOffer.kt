package com.fantasykingdom.app.data.model

enum class PromoKind { EVENT, OFFER }

/** Firestore document shape for /events_offers/{itemId}. */
data class EventOffer(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val kind: PromoKind = PromoKind.EVENT,
    val startDate: Long = System.currentTimeMillis(),
    val endDate: Long = System.currentTimeMillis(),
    val discountPercent: Int = 0
)
