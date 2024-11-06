package com.skedgo.tripkit.bookingproviders

import com.skedgo.tripkit.BookingAction
import com.skedgo.tripkit.ExternalActionParams
import io.reactivex.Observable

interface BookingResolver {
    fun performExternalActionAsync(params: ExternalActionParams): Observable<BookingAction>

    fun getTitleForExternalAction(externalAction: String): String?

    companion object {
        const val UBER: Int = 0
        const val LYFT: Int = UBER + 1
        const val FLITWAYS: Int = LYFT + 1
        const val GOCATCH: Int = FLITWAYS + 1
        const val INGOGO: Int = GOCATCH + 1
        const val MTAXI: Int = INGOGO + 1
        const val SMS: Int = MTAXI + 1
        const val OTHERS: Int = SMS + 1
    }
}