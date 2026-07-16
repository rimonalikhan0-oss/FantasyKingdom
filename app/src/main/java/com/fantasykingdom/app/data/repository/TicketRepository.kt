package com.fantasykingdom.app.data.repository

import com.fantasykingdom.app.data.model.Ticket
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Backs feature #6 (Ticket Booking) and feature #7 (QR Code Ticket).
 * Tickets are stored per-user at /users/{uid}/tickets/{ticketId}; the
 * [Ticket.qrPayload] field is what gets encoded into the QR bitmap on the
 * ticket detail screen.
 */
@Singleton
class TicketRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private fun ticketsCollection(uid: String) =
        firestore.collection("users").document(uid).collection("tickets")

    suspend fun bookTicket(ticket: Ticket): Ticket {
        val id = UUID.randomUUID().toString()
        val qrPayload = "FANTASYKINGDOM-TICKET:$id:${ticket.userId}:${ticket.visitDate}"
        val finalTicket = ticket.copy(id = id, qrPayload = qrPayload)
        ticketsCollection(ticket.userId).document(id).set(finalTicket).await()
        return finalTicket
    }

    suspend fun getTicketsForUser(uid: String): List<Ticket> =
        ticketsCollection(uid).get().await().toObjects(Ticket::class.java)

    suspend fun getTicket(uid: String, ticketId: String): Ticket? =
        ticketsCollection(uid).document(ticketId).get().await().toObject(Ticket::class.java)
}
