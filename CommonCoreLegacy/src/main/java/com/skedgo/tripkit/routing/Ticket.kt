package com.skedgo.tripkit.routing

// TODO double check if it's something we can remove and re-use the Ticket model from QuickBooking.kt
data class Ticket(
    val cost: Double = 0.0,
    val exchange: Double = 0.0,
    val fareID: String? = null,
    val name: String,
    val price: Double = 0.0,
    val purchasedTickets: List<PurchasedTicket>? = null,
    val value: Long
)

data class PurchasedTicket(
    val id: String? = null,
    val status: String? = null,
    val ticketURL: String? = null
)