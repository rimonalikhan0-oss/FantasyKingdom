package com.fantasykingdom.app.data.repository

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Thin wrapper over Firebase Storage, used to upload/replace the user's
 * profile photo (feature #12) and to resolve download URLs for content
 * images (rides, gallery, restaurants) referenced by Firestore documents.
 */
@Singleton
class StorageRepository @Inject constructor(
    private val storage: FirebaseStorage
) {
    suspend fun uploadProfilePhoto(uid: String, localUri: Uri): String {
        val ref = storage.reference.child("profile_photos/$uid.jpg")
        ref.putFile(localUri).await()
        return ref.downloadUrl.await().toString()
    }
}
