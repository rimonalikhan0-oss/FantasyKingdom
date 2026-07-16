package com.fantasykingdom.app.data.model

enum class RideCategory { THRILL, FAMILY, KIDS, WATER, DARK }

/** Firestore document shape for /rides/{rideId}. */
data class Ride(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val category: RideCategory = RideCategory.FAMILY,
    val heightRequirementCm: Int = 0,
    val waitTimeMinutes: Int = 0,
    val thrillRating: Int = 1, // 1..5
    val isOpen: Boolean = true,
    val zone: String = ""
)
