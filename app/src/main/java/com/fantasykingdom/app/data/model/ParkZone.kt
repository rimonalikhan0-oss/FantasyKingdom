package com.fantasykingdom.app.data.model

/** A labeled pin on the interactive park map (feature #9). */
data class ParkZone(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val xPercent: Float = 0f, // position on the map image, 0..1
    val yPercent: Float = 0f,
    val icon: String = "castle"
)
