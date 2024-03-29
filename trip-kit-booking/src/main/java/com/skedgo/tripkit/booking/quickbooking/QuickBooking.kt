package com.skedgo.tripkit.booking.quickbooking

import com.google.gson.annotations.SerializedName
import com.skedgo.tripkit.common.model.BookingConfirmationInputNew
import com.skedgo.tripkit.common.model.BookingConfirmationInputOptions
import com.skedgo.tripkit.common.model.BookingConfirmationNotes

data class QuickBooking(
    val bookingTitle: String,
    val bookingURL: String,
    val bookingURLIsDeepLink: Boolean,
    val count: Int,
    val index: Int,
    val input: List<Input>,
    val title: String,
    val tripUpdateURL: String,
    @SerializedName("fares") val tickets: List<Ticket>,
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
    val maxValue: Int
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
                0
            )

    }

    fun setValuesFromId() {
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

            value == type.getDefaultValueByType(title) -> {
                value = ""
            }

            values == listOf(type.getDefaultValueByType(title)) -> {
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

data class Ticket(
    val id: String,
    val currency: String,
    val description: String,
    val name: String,
    val price: Double,
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

data class PurchasedTicket(
    val id: String,
    val validFromTimestamp: String?,
    val validUntilTimestamp: String?,
    val ticketURL: String?,
    val activateURL: String?,
    val ticketExpirationTimestamp: String,
    val purchasedTimestamp: String,
    val fare: Ticket,
    val status: String,
    val actions: List<PurchasedTicketAction>
)

data class PurchasedTicketAction(
    val title: String,
    val type: String,
    val confirmation: PurchasedTicketConfirmation,
    val internalURL: String
)

data class PurchasedTicketConfirmation(
    val message: String,
    val confirmActionTitle: String,
    val abortActionTitle: String
)
