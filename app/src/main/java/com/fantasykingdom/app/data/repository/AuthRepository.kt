package com.fantasykingdom.app.data.repository

import com.fantasykingdom.app.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/** Simple sealed result wrapper so ViewModels don't need to catch exceptions themselves. */
sealed class AuthResult {
    data class Success(val user: User) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

/**
 * Wraps Firebase Authentication (email/password) + writes the companion
 * profile document to Firestore under /users/{uid}. Backs feature #2
 * (Login & Sign Up) and feeds feature #12 (User Profile).
 */
@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    val currentUserId: String? get() = auth.currentUser?.uid
    val isLoggedIn: Boolean get() = auth.currentUser != null

    suspend fun signUp(fullName: String, email: String, password: String): AuthResult {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return AuthResult.Error("Account creation failed.")
            val user = User(uid = uid, fullName = fullName, email = email)
            firestore.collection("users").document(uid).set(user).await()
            AuthResult.Success(user)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Sign up failed. Please try again.")
        }
    }

    suspend fun login(email: String, password: String): AuthResult {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return AuthResult.Error("Login failed.")
            val snapshot = firestore.collection("users").document(uid).get().await()
            val user = snapshot.toObject(User::class.java) ?: User(uid = uid, email = email)
            AuthResult.Success(user)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Invalid email or password.")
        }
    }

    suspend fun sendPasswordReset(email: String): AuthResult {
        return try {
            auth.sendPasswordResetEmail(email).await()
            AuthResult.Success(User(email = email))
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Could not send reset email.")
        }
    }

    fun signOut() = auth.signOut()

    suspend fun fetchCurrentUser(): User? {
        val uid = currentUserId ?: return null
        return firestore.collection("users").document(uid).get().await()
            .toObject(User::class.java)
    }
}
