package com.skedgo.tripkit.booking.quickbooking

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.skedgo.tripkit.common.model.booking.confirmation.BookingConfirmationInputNew
import com.skedgo.tripkit.common.model.booking.confirmation.BookingConfirmationInputOptions
import com.skedgo.tripkit.common.model.booking.confirmation.BookingConfirmationNotes
import com.skedgo.tripkit.data.database.booking.ticket.TicketEntity
import com.skedgo.tripkit.extensions.fromJson

data class QuickBooking(
    val bookingTitle: String,
    val bookingURL: String,
    val bookingURLIsDeepLink: Boolean,
    val count: Int,
    val index: Int,
    val input: List<Input>,
    val title: String,
    val tripUpdateURL: String,
    @SerializedName("fares") val fares: List<Fare>? = emptyList(),
    val billingEnabled: Boolean,
    val riders: List<Rider>
)

data class Option(
    val id: String,
    val title: String,
    val timestamp: String? = null,
    val provider: String? = null
) {
    companion object {
        fun parseBookingConfirmationInputOptions(opt: BookingConfirmationInputOptions): Option {
            return Option(opt.id(), opt.title())
        }

        fun parseBookingConfirmationNotes(opt: BookingConfirmationNotes): Option {
            return Option(opt.timestamp(), opt.text(), opt.timestamp(), opt.provider())
        }
    }
}

data class Input(
    val id: String,
    val options: List<Option>?,
    val required: Boolean,
    val title: String,
    val type: String,
    var raw_value: String,
    var value: String?,
    var values: List<String>?,
    val minValue: Int,
    val maxValue: Int,
    var urlValue: String?
) {
    companion object {
        fun parse(bookingConfirmationInput: BookingConfirmationInputNew): Input =
            Input(
                bookingConfirmationInput.id(),
                bookingConfirmationInput.options().map {
                    Option.parseBookingConfirmationInputOptions(it)
                },
                bookingConfirmationInput.required(),
                bookingConfirmationInput.title(),
                bookingConfirmationInput.type(),
                "",
                bookingConfirmationInput.value(),
                bookingConfirmationInput.values(),
                0,
                0,
                null
            )

    }

    fun setValuesFromId(defaultActionTitle: String) {
        when {
            options != null -> {
                value = value?.run { options.firstOrNull { it.title == this }?.id ?: "" }
                values = values?.run {
                    val data = mutableListOf<String>()
                    forEach { value ->
                        options.firstOrNull { it.title == value }?.id?.let {
                            data.add(it)
                        }
                    }
                    data
                }
            }

            value == type.getDefaultValueByType(title, defaultActionTitle = defaultActionTitle) -> {
                value = ""
            }

            values == listOf(type.getDefaultValueByType(title, defaultActionTitle = defaultActionTitle)) -> {
                values = emptyList()
            }
        }
    }

    fun setValuesFromTitle() {
        value = value?.run { options?.firstOrNull { it.id == this }?.title }
        values = values?.run {
            val data = mutableListOf<String>()
            forEach { value ->
                data.add(options?.firstOrNull { it.id == value }?.title ?: "")
            }
            data
        }
    }
}

// Renamed from "Ticket" to "Fare" since as per checking in the response, it's
// identified as fare for the ticket.
data class Fare(
    val id: String,
    val currency: String = "",
    val description: String,
    val name: String,
    val price: Double = 0.0,
    var value: Long?,
    val max: Int? = null,
    val riders: List<Rider>,
    val status: String,
    val type: String
)

data class Rider(
    val id: String,
    val name: String,
    val description: String
)

// Renamed from "PurchasedTicket" to "Ticket" since as per checking in the response, it's
// identified as a ticket.
data class Ticket(
    val id: String,
    val validFromTimestamp: String?,
    val validUntilTimestamp: String?,
    val ticketURL: String?,
    val activateURL: String?,
    val ticketExpirationTimestamp: String?,
    val purchasedTimestamp: String,
    val fare: Fare,
    val status: String?,
    val actions: List<TicketAction>? = emptyList(),
    val qrCode: String? = null
) {

    companion object {
        fun TicketEntity.toTicket(): Ticket {
            val gson = Gson()
            return Ticket(
                id = id,
                validFromTimestamp = validFromTimestamp,
                validUntilTimestamp = validUntilTimestamp,
                ticketURL = ticketURL,
                activateURL = activateURL,
                ticketExpirationTimestamp = ticketExpirationTimestamp,
                purchasedTimestamp = purchasedTimestamp,
                fare = gson.fromJson(fareJson),
                status = status,
                actions = ticketActionsJson?.let { gson.fromJson(it) } ?: emptyList(),
                qrCode = qrCode
            )
        }

        fun Ticket.toEntity(userId: String? = null): TicketEntity {
            val gson = Gson()
            return TicketEntity(
                id = id,
                validFromTimestamp = validFromTimestamp,
                validUntilTimestamp = validUntilTimestamp,
                ticketURL = ticketURL,
                activateURL = activateURL,
                ticketExpirationTimestamp = ticketExpirationTimestamp,
                purchasedTimestamp = purchasedTimestamp,
                fareJson = gson.toJson(fare),
                status = status,
                ticketActionsJson = if (actions.isNullOrEmpty()) "" else gson.toJson(actions),
                qrCode = qrCode,
                userId = userId
            )
        }
    }

    fun getTicketStatus(): String {
        return qrCode?.let { "Show QR Code" } ?: status.orEmpty()
    }

    fun isTypeQRCode() = !qrCode.isNullOrBlank()

    enum class Status(val type: String) {
        INACTIVE("inactive"),
        UNACTIVATED("unactivated");

        companion object {
            fun getStatus(type: String): Status? {
                return values().firstOrNull { it.type.equals(type, ignoreCase = true) }
            }
        }
    }
}

data class TicketAction(
    val title: String,
    val type: String,
    val confirmation: TicketConfirmation,
    val internalURL: String
)

data class TicketConfirmation(
    val message: String,
    val confirmActionTitle: String,
    val abortActionTitle: String
)
