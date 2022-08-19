package com.skedgo.tripkit.booking.quickbooking

data class QuickBookResponse(
        val action: Action,
        val form: List<Form>,
        val refreshURLForSourceObject: String,
        val title: String,
        val type: String,
        val paymentOptions: List<PaymentOption>?,
        val review: List<Review>?
)

data class Form(
        val fields: List<Field>,
        val title: String
)

data class Field(
        val id: String,
        val keyboardType: String,
        val method: String,
        val readOnly: Boolean,
        val refresh: Boolean,
        val sidetitle: String,
        val title: String,
        val type: String,
        val value: String
)

data class Action(
        val done: Boolean,
        val title: String
)

data class PaymentOption(
        val currency: String,
        val description: String,
        val fullPrice: Int,
        val url: String
)

data class Review(
        val arrive: String,
        val currency: String,
        val depart: String,
        val destination: Destination,
        val mode: String,
        val origin: Origin,
        val price: Double,
        val provider: Provider,
        val tickets: List<Ticket>
) {
    fun getPriceWithCurrency(): String {
        val symbol = if (currency == "USD") "$" else currency
        return String.format("%s%.2f", symbol, price / 100.0)
    }
}

data class Destination(
        val address: String,
        val lat: Double,
        val lng: Double
)

data class Origin(
        val address: String,
        val lat: Double,
        val lng: Double
)

data class Provider(
        val subtitle: String,
        val title: String
)
