package com.fantasykingdom.app.data.model

/** Firestore document shape for /users/{uid}. */
data class User(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val phone: String = "",
    val fcmToken: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
