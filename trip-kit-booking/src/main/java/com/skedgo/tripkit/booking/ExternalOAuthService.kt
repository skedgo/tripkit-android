package com.skedgo.tripkit.booking

import io.reactivex.Observable

interface ExternalOAuthService {
    fun getAccessToken(
        form: BookingForm,
        code: String,
        grantType: String,
        callback: String
    ): Observable<ExternalOAuth>
}
