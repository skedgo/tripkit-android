package com.skedgo.tripkit.bookingproviders

import android.content.Intent
import android.net.Uri
import com.skedgo.tripkit.BookingAction
import com.skedgo.tripkit.ExternalActionParams
import io.reactivex.Observable

internal class SmsBookingResolver : BookingResolver {
    override fun performExternalActionAsync(params: ExternalActionParams): Observable<BookingAction> {
        val action = BookingAction.builder()
            .bookingProvider(BookingResolver.SMS)
            .hasApp(false)
            .data(createSmsIntentByAction(params.action()))
            .build()
        return Observable.just(action)
    }

    override fun getTitleForExternalAction(externalAction: String): String? {
        return "Send SMS" // TODO: i18n.
    }

    companion object {
        /**
         * @param uri e.g. 'sms:12345' where '12345' is phone number.
         */
        fun createSmsIntentByUri(uri: String?, smsBody: String?): Intent {
            // Put EXTRA_TEXT to avoid missing body on Samsung devices running 5.0.
            return Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse(uri))
                .putExtra("sms_body", smsBody)
                .putExtra(Intent.EXTRA_TEXT, smsBody)
        }

        /**
         * @param action e.g. 'sms:12345?Body goes here'
         * where '12345' is phone number and 'Body goes here' is sms body.
         */
        @JvmStatic
        fun createSmsIntentByAction(action: String): Intent {
            val parts = action.split("\\?".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val uri = if (parts.size > 0) parts[0] else null
            val smsBody = if (parts.size > 1) parts[1] else null
            return createSmsIntentByUri(uri, smsBody)
        }
    }
}