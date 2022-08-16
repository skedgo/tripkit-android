package com.skedgo.tripkit.booking.quickbooking

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
        val tickets: List<Ticket>
)

data class Option(
        val id: String,
        val title: String,
        val timestamp: String? = null,
        val provider: String? = null
) {
  companion object{
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
)

data class Ticket(
    val id: String,
    val currency: String,
    val description: String,
    val name: String,
    val price: Double,
    val value: Long
)