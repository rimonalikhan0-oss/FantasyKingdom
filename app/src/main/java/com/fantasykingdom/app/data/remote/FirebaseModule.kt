package com.fantasykingdom.app.data.remote

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** DataStore instance used for local app preferences (theme, notification opt-in, etc). */
val Context.dataStore by preferencesDataStore(name = "fantasy_kingdom_prefs")

/**
 * Hilt module wiring up the three Firebase products this app uses:
 * Authentication, Firestore, and Storage. Centralizing instance creation
 * here means every repository gets the same singleton instances instead of
 * each calling FirebaseAuth.getInstance() ad hoc.
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()
}
