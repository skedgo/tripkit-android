package com.skedgo.tripkit.booking

import com.skedgo.tripkit.common.model.region.Region
import io.reactivex.Observable

/**
 * Extension of [AuthApi] that facilitates fetching providers for a region.
 */
interface AuthService : AuthApi {
    fun fetchProvidersByRegionAsync(
        region: Region,
        mode: String?,
        bsb: Boolean
    ): Observable<List<AuthProvider>>
}