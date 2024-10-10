package com.skedgo.tripkit.booking.quickbooking

import androidx.annotation.StringDef
import com.google.gson.annotations.SerializedName
import com.skedgo.tripkit.common.util.decimalFormatWithCurrencySymbol
import com.skedgo.tripkit.common.util.factor100
import com.skedgo.tripkit.common.util.nonDecimalFormatWithCurrencySymbol
import com.skedgo.tripkit.routing.ModeInfo

data class QuickBookResponse(
    val action: Action?,
    val form: List<Form>?,
    val refreshURLForSourceObject: String?,
    val title: String?,
    val type: String?,
    val paymentOptions: List<PaymentOption>?,
    val review: List<Review>?,
    val publishableApiKey: String?,
    val ephemeralKey: EphemeralKey?
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
    val id: String?,
    val currency: String,
    val description: String,
    val fullPrice: Int,
    val discountedPrice: Int,
    val newBalance: Int,
    val currentBalance: Int,
    val url: String,
    val method: String,
    val paymentMode: String,
    val sponsorImageURL: String? = null,
    val sponsorTitle: String? = null,
    val sponsorDescription: String? = null,
) {
    fun getCurrentBalanceString(): String {
        val price = currentBalance.toDouble() / 100.0
        return getFormattedPrice(price)
    }

    fun getNewBalanceString(): String {
        val price = newBalance.toDouble() / 100.0
        return getFormattedPrice(price)
    }

    fun getDiscountedPriceString(): String {
        val price = discountedPrice.toDouble() / 100.0
        return getFormattedPrice(price)
    }

    fun getFullPriceString(): String {
        val price = fullPrice.toDouble() / 100.0
        return getFormattedPrice(price)
    }

    fun getDiscountString(): String {
        val discount = (fullPrice.toDouble() - discountedPrice.toDouble()) / 100.0
        return getFormattedPrice(discount)
    }

    fun hasDiscount(): Boolean = discountedPrice > 0

    private fun getFormattedPrice(price: Double): String {
        return if (price == 0.0) {
            ""
        } else if (price.factor100()) {
            price.toInt().nonDecimalFormatWithCurrencySymbol(currency.getCurrencySymbol())
        } else {
            price.decimalFormatWithCurrencySymbol(currency.getCurrencySymbol())
        }
    }

    private fun String.getCurrencySymbol(): String {
        return if (this == "USD") "$"
        else if (this == "AUD") "AU$"
        else this
    }

    @Retention(AnnotationRetention.RUNTIME)
    @StringDef(
        PaymentMode.FREE,
        PaymentMode.WALLET,
        PaymentMode.INTERNAL,
        PaymentMode.EXTERNAL
    )
    annotation class PaymentMode {
        companion object {
            const val FREE = "FREE"
            const val WALLET = "WALLET"
            const val INTERNAL = "INTERNAL"
            const val EXTERNAL = "EXTERNAL"
        }
    }
}

data class Review(
    val arrive: String,
    val currency: String,
    val depart: String,
    val destination: Destination,
    val mode: String,
    val modeInfo: ModeInfo,
    val origin: Origin,
    val price: Double,
    val provider: Provider,
    val fares: List<Fare>?
) {
    fun getPriceWithCurrency(): String {
        val symbol = if (currency == "USD") "$" else currency

        val price = price / 100.0

        return if (price.factor100()) {
            price.toInt().nonDecimalFormatWithCurrencySymbol(symbol)
        } else {
            price.decimalFormatWithCurrencySymbol(symbol)
        }
    }

    fun getFormattedPrice(): Double {
        return price / 100.0
    }
}

data class Destination(
    val address: String?,
    val lat: Double,
    val lng: Double
)

data class Origin(
    val address: String?,
    val lat: Double,
    val lng: Double
)

data class Provider(
    val subtitle: String?,
    val title: String
)

data class EphemeralKey(
    @SerializedName("associated_objects")
    val associatedObjects: List<AssociatedObject>?,
    val created: Long,
    val expires: Long,
    val id: String,
    val livemode: Boolean? = false,
    @SerializedName("object")
    val objectType: String?,
    val secret: String?
)

data class AssociatedObject(
    val id: String,
    val type: String?
)