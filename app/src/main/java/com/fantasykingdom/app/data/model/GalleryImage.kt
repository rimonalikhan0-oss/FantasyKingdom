package com.fantasykingdom.app.data.model

/** Firestore document shape for /gallery/{imageId}, backed by a Storage URL. */
data class GalleryImage(
    val id: String = "",
    val imageUrl: String = "",
    val caption: String = "",
    val category: String = "General"
)
