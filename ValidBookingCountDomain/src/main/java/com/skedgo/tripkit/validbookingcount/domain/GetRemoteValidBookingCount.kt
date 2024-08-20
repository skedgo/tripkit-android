package com.skedgo.tripkit.validbookingcount.domain

import io.reactivex.Observable
//import com.skedgo.tripkit.viewaccount.domain.HasUserToken
import javax.inject.Inject

/**
 * Gotta check user token existence. Otherwise, it results in
 * errors of missing user token at server side.
 */
open class GetRemoteValidBookingCount @Inject constructor(
    /*private val hasUserToken: HasUserToken,*/
    private val validBookingCountRepository: ValidBookingCountRepository
) {
    open fun execute(): Observable<Int> = validBookingCountRepository.getRemoteValidBookingCount()
}
