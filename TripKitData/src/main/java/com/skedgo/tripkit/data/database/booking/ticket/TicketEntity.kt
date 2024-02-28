package com.skedgo.tripkit.data.database.booking.ticket

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tickets")
data class TicketEntity(
    @PrimaryKey val id: String,
    val validFromTimestamp: String?,
    val validUntilTimestamp: String?,
    val ticketURL: String?,
    val activateURL: String?,
    val ticketExpirationTimestamp: String?,
    val purchasedTimestamp: String,
    val fareJson: String, // Serialized Fare
    val status: String?,
    val qrCode: String? = null,
    val ticketActionsJson: String?, // Serialized TicketAction
)