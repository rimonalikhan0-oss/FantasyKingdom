package com.fantasykingdom.app.data.model

enum class TicketType { ONE_DAY, TWO_DAY, ANNUAL_PASS, VIP_EXPRESS }

/** Firestore document shape for /users/{uid}/tickets/{ticketId}. */
data class Ticket(
    val id: String = "",
    val userId: String = "",
    val type: TicketType = TicketType.ONE_DAY,
    val visitDate: Long = System.currentTimeMillis(),
    val guestCount: Int = 1,
    val totalPrice: Double = 0.0,
    val qrPayload: String = "", // unique signed string encoded into the QR
    val isRedeemed: Boolean = false,
    val purchasedAt: Long = System.currentTimeMillis()
)
