package com.fantasykingdom.app.data.repository

import com.fantasykingdom.app.data.model.EventOffer
import com.fantasykingdom.app.data.model.GalleryImage
import com.fantasykingdom.app.data.model.ParkZone
import com.fantasykingdom.app.data.model.Restaurant
import com.fantasykingdom.app.data.model.Ride
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Read-heavy repository for all the "park content" collections stored in
 * Firestore: rides (feature #5), restaurants (feature #8), events/offers
 * (feature #10), gallery images (feature #11) and map zones (feature #9).
 *
 * All content is seeded/managed from the Firebase console or an admin tool;
 * this app is a read-only consumer of it.
 */
@Singleton
class ParkRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getRides(): List<Ride> =
        firestore.collection("rides").get().await().toObjects(Ride::class.java)

    suspend fun getRide(id: String): Ride? =
        firestore.collection("rides").document(id).get().await().toObject(Ride::class.java)

    suspend fun getRestaurants(): List<Restaurant> =
        firestore.collection("restaurants").get().await().toObjects(Restaurant::class.java)

    suspend fun getEventsAndOffers(): List<EventOffer> =
        firestore.collection("events_offers").get().await().toObjects(EventOffer::class.java)

    suspend fun getGalleryImages(): List<GalleryImage> =
        firestore.collection("gallery").get().await().toObjects(GalleryImage::class.java)

    suspend fun getParkZones(): List<ParkZone> =
        firestore.collection("map_zones").get().await().toObjects(ParkZone::class.java)
}
